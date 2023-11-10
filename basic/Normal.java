package ir.ac.kntu.os.projFall98.basic;

import ir.ac.kntu.os.projFall98.BaseTask;
import ir.ac.kntu.os.projFall98.Locker;
import ir.ac.kntu.os.projFall98.TaskPriority;

public class Normal extends BaseTask {
    public Normal(int taskId, int burst, TaskPriority priority) {
        super(taskId, burst, priority);
    }

    @Override
    protected void requestCpu() {
        ++Locker.getInstance().normalCounter;
        Locker.getInstance().lock.lock();
        try
        {

            while (Locker.getInstance().cpuBusy || Locker.getInstance().highCounter>0 || Locker.getInstance().normalChecker)
                Locker.getInstance().cpuFreeCondition.await();


            --Locker.getInstance().howManyCores;
            if(Locker.getInstance().howManyCores==0)
                Locker.getInstance().cpuBusy = true;

            ++Locker.getInstance().fiveCounter;
            if(Locker.getInstance().fiveCounter==5)
                Locker.getInstance().normalChecker=true;
        }
        catch (Exception ex)
        {

        }
        finally
        {
            Locker.getInstance().lock.unlock();
        }
    }

    @Override
    protected void releaseCpu() {
        Locker.getInstance().lock.lock();
        try
        {
            ++Locker.getInstance().howManyCores;
            Locker.getInstance().cpuBusy = false;

            --Locker.getInstance().normalCounter;


            Locker.getInstance().cpuFreeCondition.signalAll();


        }
        finally
        {
            Locker.getInstance().lock.unlock();
        }

        completionCleanup();
    }
}
