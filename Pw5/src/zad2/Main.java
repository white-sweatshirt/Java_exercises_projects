package zad2;

import java.util.concurrent.*;

public class Main {


    public static void main(String[] args) {
        int n = 2;
        Object lock = new Object();
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(2);
        long start = System.currentTimeMillis();
        exec.schedule(new MyThread(start, lock), 2, TimeUnit.SECONDS);
        exec.scheduleAtFixedRate(() -> {
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

        }, 0, 1, TimeUnit.SECONDS);
        try {
            Thread.sleep(8000);
            exec.shutdown();
            exec.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Koniec");
    }
}
