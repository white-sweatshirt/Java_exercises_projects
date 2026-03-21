package Zad4;

public class MyThread
{

    final static int NUMBER_OF_REPETIONS = 10;

    public void pozdrowienia1()
    {
        for (int i = 0; i < NUMBER_OF_REPETIONS; i++)
            System.out.println("Pozdrowienia z wątku :: pozdrowienia1 :: " + i);
    }

    public static void pozdrowienia2()
    {
        for (int i = 0; i < NUMBER_OF_REPETIONS; i++)
            System.out.println("Pozdrowienia z wątku :: pozdrowienia2 :: " + i);
    }
}
