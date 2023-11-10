package ir.ac.kntu.os.projFall98;

public interface OsTask extends Runnable
{
    int getArrivalTime();

    int getStartProcessingTime();

    int getFinishTime();

    int getBurst();

    TaskPriority getPriority();

    boolean isCompleted();

    int getTaskId();
}

