package ThreadsStart;

public class Main
{
    public static void main()
    {
        ChildThread childThread1 = new ChildThread();
        ChildThread childThread2 = new ChildThread();
        childThread2.start();
        childThread1.start();
        try
        {
            childThread1.join();
        } catch (InterruptedException e)
        {
            System.out.println(e.getMessage());
        }
    }

}
