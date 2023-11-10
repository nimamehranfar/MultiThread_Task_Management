package ir.ac.kntu.os.projFall98;

import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.locks.Lock;

public abstract class BaseTask implements OsTask
{
    protected final Logger logger = Logger.getLogger("Task");

    private final int burst;
    private final TaskPriority priority;
    public static Lock lock = new ReentrantLock();

    private final int taskId;

    private int arrivalTime;
    private int startProcessingTime;
    private int finishTime;
    private boolean completed;

    public BaseTask(int taskId, int burst, ir.ac.kntu.os.projFall98.TaskPriority priority)
    {
        this.taskId = taskId;
        this.burst = burst;
        this.priority = priority;

        this.arrivalTime = ir.ac.kntu.os.projFall98.TasksContext.getInstance().getRelativeTime();
    }

    @Override
    public int getArrivalTime()
    {
        return arrivalTime;
    }

    @Override
    public int getStartProcessingTime()
    {
        return startProcessingTime;
    }

    @Override
    public int getFinishTime()
    {
        return finishTime;
    }

    @Override
    public int getBurst()
    {
        return burst;
    }

    @Override
    public ir.ac.kntu.os.projFall98.TaskPriority getPriority()
    {
        return priority;
    }

    @Override
    public boolean isCompleted()
    {
        return completed;
    }

    public int getTaskId()
    {
        return taskId;
    }

    public void run()
    {
        startProcessingTime = TasksContext.getInstance().getRelativeTime();
        
        requestCpu();

        executeTask();

        releaseCpu();
    }

    protected abstract void requestCpu();

    private void executeTask()
    {
        try
        {
            logTabular(TasksContext.getInstance().getRelativeTime(), taskId, priority, arrivalTime, burst, "START");

            Thread.sleep(getBurst());

            logTabular(TasksContext.getInstance().getRelativeTime(), taskId, priority, arrivalTime, burst, "FINISH");

            finishTime = TasksContext.getInstance().getRelativeTime();

            completionCleanup();
        }
        catch (Exception e)
        {
            logger.log(Level.SEVERE, "Task-"+getTaskId(), e);
        }
    }

    protected abstract void releaseCpu();

    private void logTabular(Object... objects)
    {
        StringBuilder builder = new StringBuilder();
        for (Object object : objects)
            builder.append(object).append("\t");

        logger.log(Level.INFO, builder.append("\r\n").toString());
    }

    protected void completionCleanup()
    {
        completed = true;

        TasksContext.getInstance().taskCompleted(this);
    }
}

