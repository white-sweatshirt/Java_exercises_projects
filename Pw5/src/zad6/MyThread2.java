package zad6;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MyThread2 implements Runnable {
    private final Barrier barrier;
    private final int id;
    private final Random rand = new Random();

    public MyThread2(Barrier barrier, int id) {
        this.barrier = barrier;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            System.out.println("Wątek puli 2 " + id + " start");
            int sleepTime = 2 + rand.nextInt(3);
            TimeUnit.SECONDS.sleep(sleepTime);
            System.out.println("Wątek puli 2 " + id + " przed coutdowun");
            barrier.countDown();
            System.out.println("Wątek puli 2 " + id + " po count down - koniec");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
