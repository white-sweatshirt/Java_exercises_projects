package projekt.utility; // Adjust package as needed

import projekt.Consumer.Client;
import projekt.pool.Pool;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Cashier extends Thread {
    private static Pool[] pools;
    ReentrantLock lock = new ReentrantLock();
    Condition vipsEmpty = lock.newCondition();

    // Setter so AppFX can pass the initialized pools to the Cashier
    public static void setPools(Pool[] initializedPools) {
        pools = initializedPools;
    }

    @Override
    public void run() {
        // Run continuously while the program is active
        while (!isInterrupted()) {
            try {
                administerPools();
                // The cashier checks the pools every 2 seconds
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("Cashier thread interrupted. Closing register.");
                interrupt();
            }
        }
    }

    public void administerPools() {
        if (pools == null) return;

        // Print a diagnostic report of the pools to the console
        System.out.println("=== Cashier Pool Report ===");
        for (int i = 0; i < pools.length; i++) {
            Pool p = pools[i];
            if (p != null) {
                // We use synchronized getters (defined below) to read the pool state safely
                System.out.println("Pool " + i + ": " + p.getCurrentPeopleCount() + "/" + p.getMaxCapacity() + " people.");
            }
        }
        System.out.println("VIPs currently in queue: " + Client.giveAmountOfVipes());
        System.out.println("===========================\n");
    }
}