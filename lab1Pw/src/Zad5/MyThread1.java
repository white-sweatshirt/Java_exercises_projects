package Zad5;

import java.lang.*;

public class MyThread1 extends Thread
{
    final int N_SUBTHREADS = 3;
    public String name;

    MyThread1(String name)
    {
        this.name = name;
    }

    @Override
    public void run()
    {
        Thread[] subThreads = new Thread[N_SUBTHREADS];
        for (int i = 0; i < N_SUBTHREADS; i++)
            subThreads[i] = new Thread(() ->
            {
                long  start1 = 0, start2 = 1, current = 1, temp = 0, j = 0;
                while (!Thread.currentThread().isInterrupted())
                {
                    try
                    {
                        sleep(100);
                    } catch (InterruptedException e)
                    {
                        break;
                    }
                    temp = current;
                    current = start1 + start2;
                    start1 = start2;
                    start2 = temp;
                    j++;
                }
                System.out.println(name + " powtorzenie " + j + " wyliczono " + current);
            });
        for (int i = 0; i < N_SUBTHREADS; i++)
            subThreads[i].start();
        int timeToSleep = (int) ((Math.random() * 500) + 2500);
        for (int j = 0; j < 20; j++)
        {
            try
            {
                sleep(timeToSleep);
                System.out.println(name + ":: spałem przez:" + timeToSleep);
            } catch (InterruptedException e)
            {
                System.out.println("zostalem obudzony");
                for (int i = 0; i < N_SUBTHREADS; i++)
                    subThreads[i].interrupt();
                try
                {
                    for (int i = 0; i < N_SUBTHREADS; i++)
                        subThreads[i].join();
                } catch (InterruptedException ex)
                {
                    System.out.println(ex.getMessage());
                }
                break;
            }
        }
        System.out.println(name + " -KONIEC");
    }
}
