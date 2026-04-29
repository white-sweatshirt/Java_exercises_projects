package zad1;

import java.util.Objects;
import java.util.concurrent.*;

public class Main {


    public static void main(String[] args) {
        int n = 2;
        Object lock = new Object();
        ExecutorService exec = Executors.newFixedThreadPool(2);
        long start = System.currentTimeMillis();
        exec.execute(new MyThread(start, lock));
        exec.execute(() -> {
            long startTime = start;
            synchronized (lock) {
                System.out.println(System.currentTimeMillis() - startTime + " lambda wej :" + Thread.currentThread().getName());
            }
            try {
                TimeUnit.SECONDS.sleep((int) (Math.random() * 5 + 1));
                synchronized (lock) {
                    System.out.println(System.currentTimeMillis() - startTime + " lambda wy :" + Thread.currentThread().getName());
                }
                Thread.currentThread().interrupt();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        });
        try {
            exec.shutdown();
            exec.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Koniec");
        try {
            exec.execute(() -> {
                System.out.println("Task after shutdown");
            });
        } catch (RejectedExecutionException exc) {
            exc.printStackTrace();
        }
        try {
            exec.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException exc) {
            exc.printStackTrace();
        }
        System.out.println("Terminated: " + exec.isTerminated());
    }
}
