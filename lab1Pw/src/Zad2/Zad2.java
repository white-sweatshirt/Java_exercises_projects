package Zad2;

import java.lang.Runnable;

public class Zad2 implements Runnable
{
    String name;


    public void run()
    {
        int randomNumber;
        try
        {
            for (int i = 0; i < 10; i++)
            {
                System.out.println("Pozdrowienia z watku::" + Thread.currentThread().getName());
                randomNumber = (int) ((Math.random() * 100) + 100);
                Thread.sleep(randomNumber);
            }
            Thread.sleep(1000);
        } catch (InterruptedException a)
        {
            System.out.println(a.getMessage());
        }
    }
}
