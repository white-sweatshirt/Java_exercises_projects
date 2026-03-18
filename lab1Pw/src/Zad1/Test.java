package Zad1;

public class Test
{
    final static int numberOfThreads = 10;

    public static void main(String[] args)
    {
        Thread[] tab1 = new Thread[10];
        for (int i = 0; i < numberOfThreads; i++)
            tab1[i] = new Zad1("superWatek" + i);
        for (int i = 0; i < numberOfThreads; i++)
            tab1[i].start();
        try
        {
            for (int i = 0; i < numberOfThreads; i++)
                tab1[i].join();
        } catch (InterruptedException e)
        {
            System.out.println(e.getMessage());

        }
        finally
        {
            System.out.println("Koniec");
        }
    }
}
