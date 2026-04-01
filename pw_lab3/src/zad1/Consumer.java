package zad1;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.random.RandomGenerator;
public class Consumer {
    public static Semaphore full;
    public static Semaphore empty;
    private int id;
    public Consumer(Semaphore full1, Semaphore empty1,int id) {
        full = full1;
        empty = empty1;
        this.id = id;
    }

    private void privateJob() {
        Random random = new Random();

    }
}
