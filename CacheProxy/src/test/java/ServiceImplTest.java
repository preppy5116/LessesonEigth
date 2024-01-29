import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServiceImplTest {
    ServiceImpl service = new ServiceImpl();

    @Test
    void Main() {
        CacheProxy cacheProxy = new CacheProxy();
        Service service1 = cacheProxy.cache(service, "");


        System.out.println("Next test : Default parameters");

        double r1 = service1.doHardWork("work1", 10); //Запись
        double r2 = service1.doHardWork("work1", 5);  //Запись
        double r3 = service1.doHardWork("work1", 5);  //Кэш


        //Игнорирование второго аргумента
        System.out.println();
        System.out.println("First test : choosing parameters");

        double r4 = service1.doIdentityByWork("work1", 10); //Запись
        double r5 = service1.doIdentityByWork("work1", 5);  //Кеш
        double r5_1 = service1.doIdentityByWork("work12", 5); //Запись

        System.out.println();
        System.out.println("Next test : keyName");

        double r6 = service1.doDataWork("work1", 10); //Запись
        double r7 = service1.doDataWork("work1", 5);  //Кэш

        System.out.println();
        System.out.println("Next test : List");

        List<Integer> r8 = service1.doListWork("list1", 10); //Запись
        List<Integer> r9 = service1.doListWork("list1", 5);  //Кэш
        System.out.println(r8.size() + " " + r9.size()); //ограничение размера листа = 2

        System.out.println();
        System.out.println("Next test : File");

        double r10 = service1.doFileWork("work1", 10);
        double r11 = service1.doFileWork("work1", 5);

        System.out.println();
        System.out.println("Next test : Zip");

        double r12 = service1.doZipWork("work1", 10);
        double r13 = service1.doZipWork("work1", 5);
    }

    @Test
    void extraCheck() {
        Service service1 = (Service) CacheProxy.cache(service, "D:/java/CacheProxy/files/");
        System.out.println();
        System.out.println("Extra test");
        double r1 = service1.doExtraWork("work1", 10); //считает результат
        double r2 = service1.doExtraWork("work2", 5);  //считает результат
        double r3 = service1.doExtraWork("work1", 10); //результат из кеша
        double r4 = service1.doExtraWork("work1", 5);

    }
}