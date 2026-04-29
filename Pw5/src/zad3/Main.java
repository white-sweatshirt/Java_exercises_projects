package zad3;

import java.lang.foreign.SymbolLookup;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) {
        int n = 4;
        Object lock = new Object();
        ExecutorService executor = Executors.newFixedThreadPool(n);
        Future future1 = executor.submit(new MyThread(lock, 1));
        Future future2 = executor.submit(new MyThread(lock, 2));
        Future future3 = executor.submit(new MyThread(lock, 3));
        Future future4 = executor.submit(new MyThread(lock, 4));
        try {
            System.out.println(future1.get());
            System.out.println(future2.get());
            System.out.println(future3.get());
            System.out.println(future4.get());
        } catch (Exception e) {
        } finally {
            System.out.println("koniec");
            executor.shutdown();
        }

    }
}
