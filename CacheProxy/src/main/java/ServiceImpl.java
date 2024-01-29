import java.util.LinkedList;
import java.util.List;

public class ServiceImpl implements Service {
    @Override
    @Cache()
    public double doHardWork(String s, int a) {
        System.out.println(s + " " + a);
        return a * 2;
    }
    @Override
    @Cache(identityBy = String.class)
    public double doIdentityByWork(String str, int number) {
        System.out.println(str + " " + number);
       return number;
    }

    @Override
    @Cache(identityBy = String.class, keyName = "data")
    public double doDataWork(String str, int data) {
        System.out.println(str + " " + data);
        return data;
    }

    @Override
    @Cache(identityBy = String.class, keyName = "list", listSize = 2)
    public List<Integer> doListWork(String str, int number) {
        System.out.println(str + " " + number);
        List<Integer> cacheList = new LinkedList<>();
        cacheList.add(number);
        cacheList.add(number);
        cacheList.add(number);
        cacheList.add(number);
        return cacheList;
    }

    @Override
    @Cache(identityBy = String.class, keyName = "file", cacheType = CacheType.FILE)
    public double doFileWork(String str, int number) {
        return number;
    }

    @Override
    @Cache(identityBy = String.class, keyName = "zip", cacheType = CacheType.FILE, zip = true)
    public double doZipWork(String s, int a) {
        return a * 2;
    }


    @Override
    @Cache(identityBy = {String.class, Integer.class}, keyName = "extra name", cacheType = CacheType.FILE, zip = true)
    public double doExtraWork(String s, int a) {
        return a;
    }

}
