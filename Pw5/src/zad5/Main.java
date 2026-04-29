package zad5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int n = 5;
        Runnable barrierAction = () -> {
            System.out.println(">>> Akcja bariery (Runnable) wykonana!");
        };
        Barrier barrier = new Barrier(barrierAction);
        ExecutorService executor = Executors.newFixedThreadPool(n);
        for (int i = 0; i < n; i++) {
            executor.execute(new MyThread(barrier, i));
        }
        try {
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }
        System.out.println("Koniec");
    }
}