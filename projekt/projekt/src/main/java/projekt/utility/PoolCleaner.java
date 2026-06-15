package projekt.utility;

import java.util.Random;

import projekt.Consumer.Client;
import projekt.pool.Pool;

public class PoolCleaner implements Runnable {

    private final long TIME_BETWEEN_CLEANINGS = 20000;
    private final Random random = new Random();
    private static volatile boolean isCleaningPhase = false;

    public static boolean isCleaningInProgress() {
        return isCleaningPhase;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(TIME_BETWEEN_CLEANINGS);

                // Acquire the exposed public lock from Client interface definition
                Client.queLock.lock();
                try {
                    isCleaningPhase = true;

                    while (!areAllPoolsEmpty()) {
                        Thread.sleep(200);
                    }

                    long cleaningDuration = 3000 + random.nextInt(4000);
                    Thread.sleep(cleaningDuration);

                } finally {
                    isCleaningPhase = false;

                    // Signal variables exposed inside the Client abstraction layer
                    Client.normalPersonCanPass.signalAll();
                    Client.vipCanPass.signalAll();

                    Client.queLock.unlock();
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private boolean areAllPoolsEmpty() {
        if (Client.getAllPools() == null) return true;
        for (Pool pool : Client.getAllPools()) {
            if (pool.getCurrentPeopleCount() > 0) {
                return false;
            }
        }
        return true;
    }
}