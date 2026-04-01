package zad1;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static AtomicBoolean running = new AtomicBoolean(false);
    public static void main(String[] args) {
        Semaphore full = new Semaphore(1);
        Semaphore empty = new Semaphore(1);
    }
}
