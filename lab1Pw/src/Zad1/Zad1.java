package Zad1;

import java.lang.*;
import java.util.*;

public class Zad1 extends Thread
{
    String desc;

    public Zad1(String desc)
    {
        this.desc = desc;
    }

    @Override
    public void run()
    {
        int randomNumber;
        try
        {
            for (int i = 0; i < 10; i++)
            {
                System.out.println("Pozdrowienia z watku::" + desc +"::"+ i);
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
