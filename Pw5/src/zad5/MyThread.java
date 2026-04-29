package zad5;

import java.lang.foreign.UnionLayout;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class MyThread implements Runnable {
    private final Barrier barrier;
    private final int id;
    private final Random rand = new Random();

    public MyThread(Barrier barrier, int id) {
        this.barrier = barrier;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            System.out.println("Wątek " + id + " start");
            int sleepTime = 2 + rand.nextInt(3);
            TimeUnit.SECONDS.sleep(sleepTime);
            System.out.println("Wątek " + id + " przed barierą");
            barrier.barrier();
            System.out.println("Wątek " + id + " po barierze - koniec");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}