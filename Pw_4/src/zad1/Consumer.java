package zad1;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.random.RandomGenerator;

import java.util.concurrent.Semaphore;

// Separate class for the Consumer
class Consumer extends Thread {
    private final SharedBuffer buffer;
    private int id;
    private int amountOfRepetions;

    public Consumer(SharedBuffer buffer, int id, int rep) {
        this.buffer = buffer;
        this.id = id;
        amountOfRepetions = rep;
    }

    @Override
    public void run() {
        try {
            buffer.lock.lock();
            for (int i = 0; i < amountOfRepetions; i++) {
                // Wait for an item to be available (signaled by producer's 'empty')
                Thread.sleep((int) (Math.random() * 10 + 1)); // Slow down for visibility
                buffer.lock.lock();
                String item;
                try {
                    while (buffer.licz <= 0)
                        buffer.full.await();
                    item = buffer.pula[buffer.wyj];
                    buffer.wyj = (buffer.wyj + 1) % buffer.N;
                    buffer.licz--;
                    System.out.println("[K-" + id + ", " + i + "]" + "=>" + item);

                } finally {
                    buffer.empty.signal();
                    buffer.lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            buffer.lock.unlock();
        }
    }
}
