import java.util.List;

public interface Service {
    @Cache()
    double doHardWork(String str, int number);
    @Cache(identityBy = String.class)
    double doIdentityByWork(String str, int number);

    @Cache(identityBy = String.class, keyName = "data")
    double doDataWork(String str, int date);

    @Cache(identityBy = String.class, keyName = "list", listSize = 2)
    List<Integer> doListWork(String str, int number);

    @Cache(identityBy = String.class, keyName = "file", cacheType = CacheType.FILE)
    double doFileWork(String str, int number);

    @Cache(identityBy = String.class, keyName = "zip", cacheType = CacheType.FILE, zip = true)
    double doZipWork(String str, int number);

    @Cache(identityBy = {String.class, Integer.class}, keyName = "extra name", cacheType = CacheType.FILE, zip = true)
    double doExtraWork(String str, int number);
}

