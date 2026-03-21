package Zad6;

public class Test
{
    public static void main()
    {
        Licznik test1 = new Licznik();
        Thread[] threads = new Thread[10];
        for(int i=0;i<threads.length;i++)
            threads[i]=new MyThread("superWatel"+i,test1);
        for (int i = 0; i < threads.length; i++)
            threads[i].start();
        try
        {
            for (int i = 0; i < threads.length; i++)
                threads[i].join();
        } catch (InterruptedException e)
        {
            System.out.println(e.getMessage());
        }
        System.out.println("Stan licznika : " + test1.get());
    }
}
