package ir.ac.kntu.os.projFall98;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TasksContext
{
    private final Logger logger = Logger.getLogger(TasksContext.class.getName());

    private static TasksContext instance = new TasksContext();

    private final List<OsTask> tasks = new ArrayList<>();
    private final Lock cleanUpLock = new ReentrantLock();
    private final Condition haltCondition = cleanUpLock.newCondition();
    private final long baseTime;
    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private int taskCounter = 0;

    public TasksContext()
    {
        baseTime = System.nanoTime();
    }

    public static TasksContext getInstance()
    {
        return instance;
    }

    public int getRelativeTime()
    {
        long current = System.nanoTime();
        return (int) (current - baseTime);
    }

    public void runTask(OsTask task)
    {
        cleanUpLock.lock();
        try
        {
            tasks.add(task);



            executor.submit(task);

            taskCounter++;
        }
        finally
        {
            cleanUpLock.unlock();
        }
    }

    public void taskCompleted(OsTask task)
    {
        cleanUpLock.lock();
        try
        {
            taskCounter--;

            haltCondition.signalAll();
        }
        finally
        {
            cleanUpLock.unlock();
        }
    }

    public void waitForHalt()
    {
        cleanUpLock.lock();
        try
        {

            while (taskCounter > -3 )
                haltCondition.await(1, TimeUnit.SECONDS);

            //more waiting to ensure all tasks' completion
            Thread.sleep(1000);
        }
        catch (Exception e)
        {
            logger.log(Level.SEVERE, "", e);
        }
        finally
        {
            cleanUpLock.unlock();
        }

    }

    public void halt()
    {
        executor.shutdownNow();
    }
}
