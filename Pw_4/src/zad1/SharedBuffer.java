package zad1;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class SharedBuffer {
    final String[] pula;
    final int N;
    int wej = 0; // index for producer
    int wyj = 0; // index for consumer
    int licz = 0;

    // locks
    final public ReentrantLock lock = new ReentrantLock();
    final public Condition full = lock.newCondition();
    final public Condition empty = lock.newCondition();

    public SharedBuffer(int size) {
        this.N = size;
        this.pula = new String[N];
    }

    public void pisz(int idProducent, int nrRepetition) {
        int randomValue = (int) (Math.random() * 10 + 0);
        String elem = "(" + randomValue + ", " + wej + ")";
        System.out.println("[P-" + idProducent + ", " + nrRepetition + "]" + "=>" + elem);
        lock.lock();
        try {
            while (licz >= N)
                empty.await();

            pula[wej] = elem;
            wej = (wej + 1) % N;
            licz++;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            full.signal();
            lock.unlock();
        }
    }
    public
}
