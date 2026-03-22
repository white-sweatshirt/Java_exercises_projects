//Carpe Diem
package Zad3;

import java.util.Scanner;
import java.util.function.LongPredicate;

public class Main implements Constans
{


    public static void main()
    {
        Scanner input = new Scanner(System.in);
        Lepport[] threads = new Lepport[nThreads];
        System.out.println("czy chcesz synchronizacje \n 1- tak co innego -nie: ");
        byte a = input.nextByte();
        if(a == 1)
            for (int i = 0; i < nThreads; i++)
                threads[i].setSynchronise(true);
        else
            for (int i = 0; i < nThreads; i++)
                threads[i].setSynchronise(false);
        for (int i = 0; i < nThreads; i++)
            threads[i].start();
        try
        {
            for (int i = 0; i < nThreads; i++)
                threads[i].join();
        } catch (InterruptedException e)
        {
            System.out.println(e.getMessage());
        }
    }
}