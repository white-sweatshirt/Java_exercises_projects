package zad5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) {
        int n = 4;
        Object lock = new Object();
        ExecutorService executor = Executors.newFixedThreadPool(n);
        Future<Integer> future1 = executor.submit(new MyThread(lock, 1));
        Future<Integer> future2 = executor.submit(new MyThread(lock, 2));
        Future<Integer> future3 = executor.submit(new MyThread(lock, 3));
        Future<Integer> future4 = executor.submit(new MyThread(lock, 4));
        try {
          System.out.println((future1.get().intValue()+ future2.get().intValue()+ future3.get().intValue()+ future4.get().intValue()));
        } catch (Exception e) {
        } finally {
            System.out.println("koniec");
            executor.shutdown();
        }

    }
}
