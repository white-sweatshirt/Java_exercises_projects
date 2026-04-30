package zad6;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int n1 = 2, n2 = 4;
        Runnable barrierAction = () -> {
            System.out.println(">>> Akcja bariery (Runnable) wykonana!");
        };
        Barrier barrier = new Barrier(barrierAction, n2);
        ExecutorService executor1 = Executors.newFixedThreadPool(n1);
        ExecutorService executor2 = Executors.newFixedThreadPool(n2);

        for (int i = 0; i < n1; i++) {
            executor1.execute(new MyThread(barrier, i));
        }
        for (int i = 0; i < n2; i++) {
            executor2.execute(new MyThread2(barrier, i));
        }
        try {
            executor1.shutdown();
            executor2.shutdown();
            executor1.awaitTermination(15, TimeUnit.SECONDS);
            executor2.awaitTermination(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }
        System.out.println("KONIEC");
    }
}