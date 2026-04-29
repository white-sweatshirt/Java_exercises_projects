package zad2;

import java.util.concurrent.TimeUnit;

public class MyThread implements Runnable {
    final Object lock ;
    long startTime = 0;

    MyThread(long start,Object lock) {
        startTime = start;
        this.lock = lock;
    }

    @Override
    public void run() {
        synchronized (lock) {
        System.out.println(System.currentTimeMillis() - startTime + " Thread Runnable wej :" + Thread.currentThread().getName());
        }
        try {
            TimeUnit.SECONDS.sleep((int) (Math.random() * 5 + 1));
            synchronized (lock) {
            System.out.println(System.currentTimeMillis() - startTime + " Thread Runnable wy :" + Thread.currentThread().getName());
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
}
