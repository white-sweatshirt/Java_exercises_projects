package Zad6;

public class MyThread extends Thread
{
    String name;
    Licznik whereToIncrament;
    final static int TIMES_TO_INCREMENT = 5000000;

    public MyThread(String name, Licznik a)
    {
        this.whereToIncrament = a;
    }

    public void run()
    {
        for (int i = 0; i < TIMES_TO_INCREMENT; i++)
            whereToIncrament.incNiesynch();
    }

}
