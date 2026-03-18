package Zad2;

import java.lang.Thread;

public class Test
{
    final static int N_THREADS = 10;

    public void main()
    {
        Thread[] zad2s = new Thread[N_THREADS];
        Zad2 [] temp=new Zad2[N_THREADS];
        for (int i = 0; i < N_THREADS; i++)
        {
            temp[i]=new Zad2();
            zad2s[i] = new Thread(temp[i], "superWatek" + i);
        }
        try
        {
            for (int i = 0; i < N_THREADS; i++)
                zad2s[i].start();
            for (int i = 0; i < N_THREADS; i++)
                zad2s[i].join();
        } catch (InterruptedException e)
        {
            System.out.println(e.getMessage());
        } finally
        {
            System.out.println("Koniec");
        }
    }

}
