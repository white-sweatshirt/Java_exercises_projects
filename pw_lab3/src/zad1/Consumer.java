package zad1;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.random.RandomGenerator;

import java.util.concurrent.Semaphore;

// Separate class for the Consumer
class Consumer extends Thread {
    private final SharedBuffer buffer;

    public Consumer(SharedBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                // Wait for an item to be available (signaled by producer's 'empty')
                Thread.sleep((int) (Math.random() * 10 + 1)); // Slow down for visibility
                buffer.empty.acquire();
                String item;
                try {
                    item = buffer.pula[buffer.wyj];
                    buffer.wyj = (buffer.wyj + 1) % buffer.N;
                    buffer.licz--;
                    System.out.println("Dana- " + item);
                } finally {
                    buffer.full.release();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
