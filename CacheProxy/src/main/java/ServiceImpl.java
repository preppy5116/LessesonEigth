import javax.xml.crypto.Data;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ServiceImpl implements Service {
    @Override
//    @cachePackage.Cache()
    public double doHardWork(String s, int a) {
        System.out.println("doHardWork:"+ s + " " + a);
        return a * 2;
    }
    @Override
//    @cachePackage.Cache(identityBy = String.class)
    public double doIdentityByWork(Date str, int number) {
        System.out.println("doIdentityByWork:" + str + " " + number);
       return number;
    }
    @Override
//    @cachePackage.Cache(identityBy = String.class, keyName = "list", listSize = 2)
    public List<Integer> doListWork(String str, int number) {
        System.out.println("doListWork:" + str + " " + number);
        List<Integer> cacheList = new LinkedList<>();
        cacheList.add(number);
        cacheList.add(number);
        cacheList.add(number);
        cacheList.add(number);
        return cacheList;
    }

    @Override
//    @cachePackage.Cache(identityBy = String.class, keyName = "file", cacheType = cachePackage.CacheType.FILE)
    public double doFileWork(String str, int number) {
        return number;
    }

    @Override
//    @cachePackage.Cache(identityBy = String.class, keyName = "zip", cacheType = cachePackage.CacheType.FILE, zip = true)
    public double doZipWork(String s, int a) {
        return a * 2;
    }


    @Override
//    @cachePackage.Cache(identityBy = {String.class, Integer.class}, keyName = "My name", cacheType = cachePackage.CacheType.FILE, zip = true)
    public double doExtraWork(String s, int a) {
        return a;
    }

}
