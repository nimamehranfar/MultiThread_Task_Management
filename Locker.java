package ir.ac.kntu.os.projFall98;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Locker {

    public Lock lock = new ReentrantLock();
    public Condition cpuFreeCondition = lock.newCondition();
    public static boolean cpuBusy = false;
    private static Locker instance = new Locker();
    public static int highCounter=0;
    public static int normalCounter=0;
    public static int fiveCounter=0;
    public static boolean normalChecker=false;
    public static int howManyCores=1;

    public static Locker getInstance()
    {
        return instance;
    }


}
