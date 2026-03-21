package Zad5;

public class Test
{
    final static int N_THREADS = 2;
    final static int SLEEP_TIME = 4500;
    final static int SLEEP_TIME_2 = 2000;

    static void main()
    {
        Thread[] myThreads = new Thread[N_THREADS];
        for (int i = 0; i < N_THREADS; i++)
            myThreads[i] = new MyThread1("superWatek" + i);

        for (int i = 0; i < N_THREADS; i++)
        {
            myThreads[i].start() ;
        }
        killThread(myThreads[0], SLEEP_TIME);
        killThread(myThreads[1], SLEEP_TIME_2);

        System.out.println("Koniec");

    }

    public static void killThread(Thread thread, int lifeTime)
    {
        try
        {
            Thread.sleep(lifeTime);
            thread.interrupt();
            thread.join();
        } catch (InterruptedException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
