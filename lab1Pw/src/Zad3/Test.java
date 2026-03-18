package Zad3;

import java.lang.Thread;

public class Test
{
    final static int N_THREADS = 10;

    public void main()
    {
        Thread[] zad3 = new Thread[N_THREADS];

        for (int i = 0; i < N_THREADS; i++)
        {
            zad3[i] = new Thread(new Zad3(), "superWatek" + i);
        }
        try
        {
            for (int i = 0; i < N_THREADS; i++)
                zad3[i].start();
            for (int i = 0; i < N_THREADS; i++)
                zad3[i].join();
        } catch (InterruptedException e)
        {
            System.out.println(e.getMessage());
        } finally
        {
            System.out.println("Koniec");
        }
    }

}
