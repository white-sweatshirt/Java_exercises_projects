package projekt.utility;

import projekt.pool.Pool;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Cashier extends Thread {
    private static Pool[] pools;
    ReentrantLock lock = new ReentrantLock();
    Condition vipsEmpty = lock.newCondition();

    @Override
    public void run() {
    }

    public void administerPools() {
    }

}
