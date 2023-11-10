package ir.ac.kntu.os.projFall98;

import ir.ac.kntu.os.projFall98.basic.BasicTask;
import ir.ac.kntu.os.projFall98.basic.High;
import ir.ac.kntu.os.projFall98.basic.Normal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TasksRaceApp
{
    private static int counter = 1;
    private static Random random = new Random(System.currentTimeMillis());

    private static Logger logger;

    public static void main(String[] args)
    {
        startLog();

        // create running context for tasks
        TasksContext context = TasksContext.getInstance();
        try
        {
            System.out.println("How Many Cores?");
            Scanner scr=new Scanner(System.in);
            Locker.getInstance().howManyCores=scr.nextInt();
            scr.close();


            int pick = new Random().nextInt(TaskPriority.values().length);
            OsTask task1 = makeTask(TaskPriority.Normal);
            context.runTask(task1);

            pick=new Random().nextInt(TaskPriority.values().length);
            OsTask task2 = makeTask(TaskPriority.Normal);
            context.runTask(task2);

            pick=new Random().nextInt(TaskPriority.values().length);
            OsTask task3 = makeTask(TaskPriority.Normal);
            context.runTask(task3);

            pick=new Random().nextInt(TaskPriority.values().length);
            OsTask task4 = makeTask(TaskPriority.Normal);
            context.runTask(task4);

            pick=new Random().nextInt(TaskPriority.values().length);
            OsTask task5 = makeTask(TaskPriority.Normal);
            context.runTask(task5);

            pick=new Random().nextInt(TaskPriority.values().length);
            OsTask task6 = makeTask(TaskPriority.Low);
            context.runTask(task6);

            OsTask task7 = makeTask(TaskPriority.Normal);
            context.runTask(task7);

            context.waitForHalt();

            logger.log(Level.INFO, "==== Printing Stat ====\r\n");
            printStat(task1);
            printStat(task2);
            printStat(task3);
            printStat(task4);
            printStat(task5);
            printStat(task6);
            printStat(task7);
        }
        catch (Exception e)
        {
            logger.log(Level.SEVERE, "", e);
        }
        finally
        {
            context.halt();
        }
    }

    private static OsTask makeTask(TaskPriority priority){
        OsTask task=null;
        switch (priority){
            case High: task = new High(counter++, 10, priority); break;
            case Normal: task = new Normal(counter++, 10, priority); break;
            case Low: task = new BasicTask(counter++, 10, priority); break;
        }
        return task;
    }

    private static void startLog()
    {
        try
        {
            InputStream inputStream = TasksRaceApp.class.getClassLoader()
                    .getResourceAsStream("logging.properties");

            if (inputStream == null)
            {
                System.err.println("Cannot find logging config file, is package corrupted??");
                System.exit(2);
            }

            File logDir = new File("logs");
            logDir.mkdir();

            LogManager.getLogManager().readConfiguration(inputStream);

            logger = Logger.getLogger(TasksRaceApp.class.getName());
            logger.log(Level.INFO, "Starting app..\r\n");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    private static void printStat(OsTask task)
    {
        if (task.isCompleted())
        {
            final int waitTime = (task.getStartProcessingTime() - task.getArrivalTime())/1000000;
            logger.log(Level.INFO, "Task-" + task.getTaskId() + " waited for " + waitTime + " ms before completion\r\n");
        }
    }

}
