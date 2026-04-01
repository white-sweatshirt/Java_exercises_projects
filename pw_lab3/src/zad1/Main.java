package zad1;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) {
        // N = 5 (buffer size)
        SharedBuffer sharedData = new SharedBuffer(5);

        Producer[] producers = new Producer[4];
        Consumer[] consumers = new Consumer[5];
        for (int i = 0; i < producers.length; i++) {
            producers[i] = new Producer(i, sharedData);
        }
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer(sharedData);
        }
        for (int i = 0; i < producers.length; i++) {
            producers[i].start();
        }
        for (int i = 0; i < consumers.length; i++) {
            consumers[i].start();
        }
        for (int i = 0; i < producers.length; i++) {
            try {
                producers[i].join();
            } catch (InterruptedException e) {

            }

        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
        for (int i = 0; i < consumers.length; i++) {
            consumers[i].interrupt();
        }
    }
}
