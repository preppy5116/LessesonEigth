import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class CacheProxy implements InvocationHandler, Serializable {

    private Map cacheMap;
    private Object object;
    String directory;


    private CacheProxy(Object args, String newDirectory) {
        this.cacheMap = new HashMap();
        this.object = args;
        this.directory = newDirectory;
    }

    /**
     * Кэширкет объект
     *
     * @param o Ссылка на сервис
     * @return кэшированная версия обьекта
     */
    static Object cache(Object o, String newDirectory) {
        Class clazz = o.getClass();
        ClassLoader classLoader = clazz.getClassLoader();
        Class[] interfaces = clazz.getInterfaces();
        Object proxy = Proxy.newProxyInstance(classLoader, interfaces, new CacheProxy(o, newDirectory));
        return proxy;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Cache.class)) {
            return invokeMethod(method, args);
        } else {
            return method.invoke(object, args);
        }
    }

    private Object invokeMethod(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        List<Object> params = Arrays.asList(args);

        Cache cacheAnnotation = method.getAnnotation(Cache.class);
        Class[] identities = cacheAnnotation.identityBy();
        String nameOfKey = cacheAnnotation.keyName();
        CacheType cacheType = cacheAnnotation.cacheType();
        boolean zip = cacheAnnotation.zip();
        List mentionedParams = new ArrayList();

        if (nameOfKey == "") {
            nameOfKey = method.getName();
        }

        for (Object param : params) {
            boolean isMentioned = false;
            for (int i = 0; i < identities.length; i++) {
                if (identities[i] == param.getClass()) {
                    isMentioned = true;
                }
            }
            if (isMentioned) {
                mentionedParams.add(param);
            }
        }

        String fileName = directory +  nameOfKey + ".txt";
        String zipFileName = directory + nameOfKey + ".zip";
        Object vals = null;
        Map cache;

        if (identities[0] == Class.class) {
            mentionedParams = params;
        }

        if (cacheType == CacheType.IN_MEMORY) {
            cache = getCacheValue(nameOfKey);
            vals = cache.get(mentionedParams);
        } else {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
                cache = (HashMap<Object, Object>) ois.readObject();

                vals = cache.get(mentionedParams);
                ois.close();
            } catch (FileNotFoundException e) {
                cache = new HashMap();
            } catch (IOException e) {
                throw new RuntimeException("Problem while closing the file", e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Can't close the file, check that it exists and path to it", e);
            }

        }

        if (vals == null || !cache.containsKey(mentionedParams)) {

            System.out.println("Counting new value");
            vals = method.invoke(object, args);

            if (method.getReturnType() == List.class) {
                List valsList = (List) method.invoke(object, args);
                int maxListSize = cacheAnnotation.listSize();
                if (valsList.size() > maxListSize) {
                    vals = valsList.subList(0, maxListSize);
                }
            }

            cache.put(mentionedParams, vals);

            if (cacheType == CacheType.FILE) {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
                    FileInputStream fis = new FileInputStream(fileName);
                    oos.writeObject(cache);
                    oos.flush();

                    if (zip) {
                        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFileName));
                        ZipEntry entry = new ZipEntry(fileName);
                        zos.putNextEntry(entry);
                        byte[] byteCode = new byte[fis.available()];
                        fis.read(byteCode);
                        zos.write(byteCode);
                        zos.closeEntry();
                        zos.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Problem while closing the file", e);
                }
            }
        } else {
            System.out.println("Take result from cache");
        }
        return vals;
    }

    private Map getCacheValue(String method) {
        Map cache = (Map) cacheMap.get(method);
        if (cache == null) {
            cache = new HashMap();
            cacheMap.put(method, cache);
        }
        return cache;
    }
}
