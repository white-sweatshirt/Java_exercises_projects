package zad6;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Barrier {

    ReentrantLock lock = new ReentrantLock();
    Condition allHited = lock.newCondition();
    boolean wasRunned = false;
    int nrWatki = 5;

    public Barrier(Runnable action, int nrWatki) {
        this.nrWatki = nrWatki;
    }

    public void barrier() {
        lock.lock();
        try {
            while (nrWatki > 0) {
                allHited.await();
            }
        } catch (InterruptedException e) {
           return;
        } finally {
            lock.unlock();
        }
    }

    public void countDown() {
        lock.lock();
        try {
            while (--nrWatki > 0) {
                allHited.await();
            }
            allHited.signalAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        finally {
            lock.unlock();
        }

    }

}