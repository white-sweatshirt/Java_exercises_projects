package Zad4;

public class Test
{
    final static int NUMBER_OF_REPETIONS = 10;
    final static int N_THREADS = 3;

    public static void main()
    {
        MyThread myThread = new MyThread();
        Thread[] threads = new Thread[3];
        threads[0] = new Thread(() ->
        {
            for (int i = 0; i < NUMBER_OF_REPETIONS; i++)
                System.out.println("Pozdrowienia z wątku :: lambda :: " + i);
        });
        threads[1] = new Thread(MyThread::pozdrowienia2);
        threads[2] = new Thread(myThread::pozdrowienia1);
        for (int i = 0; i < N_THREADS; i++)
            threads[i].start();
        try
        {
            for (int i = 0; i < N_THREADS; i++)
                threads[i].join();
        } catch (InterruptedException e)
        {
            System.out.println(e.getMessage());
        } finally
        {
            System.out.println("Koniec");
        }
    }

}
