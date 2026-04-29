package zad3;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class MyThread implements Callable<String> {
    final Object lock;
    int nr = 0;

    MyThread(Object lock, int nr) {
        this.lock = lock;
    }

    @Override
    public String call() {

        try {
            TimeUnit.SECONDS.sleep((int) (Math.random() * 5 + 1));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return Thread.currentThread().getName();
    }
}
