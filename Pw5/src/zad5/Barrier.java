package zad5;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Barrier {

    ReentrantLock lock = new ReentrantLock();
    Condition allHited = lock.newCondition();
    Runnable action;
    boolean wasRunned = false;
    int nrWatki = 5;

    public Barrier(Runnable action) {
        this.action = action;
    }

    public void barrier() {
        lock.lock();

        try {
            while (--nrWatki > 0) {
                allHited.await();
            }
            if (!wasRunned) {
                action.run();
                wasRunned = true;
            }
            allHited.signalAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }


    }
}