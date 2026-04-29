package zad4;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;


public class MyThread implements Callable<Integer> {
    final Object lock;
    int nr = 0;

    MyThread(Object lock, int nr) {
        this.lock = lock;
    }

    @Override
    public Integer call() {
        int result = 0;
        try {
            TimeUnit.SECONDS.sleep((int) (Math.random() * 5 + 1));
            result = (int) (Math.random() * 10 + 1);
            System.out.println(Thread.currentThread().getName() + ":" + result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Integer a = new Integer(result);
        return a;
    }
}