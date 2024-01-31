import java.lang.reflect.Proxy;



public class CacheProxy<T> {
    @SuppressWarnings("unchecked")
    public static <T> T cache(T object, String newDirectory) {
        var invocationHandler = new CacheHandler(object,newDirectory);
        return (T) Proxy.newProxyInstance(object.getClass().getClassLoader(), object.getClass().getInterfaces(), invocationHandler);
    }
}
