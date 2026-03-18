package ThreadsStart;

import java.lang.Thread;

public class ChildThread extends Thread
{
    @Override
    public void run()
    {

        iterate();
    }

    public static synchronized void iterate()
    {
        for (int i = 0; i < 100; i++)
            System.out.println("i= " + i);
    }
}
