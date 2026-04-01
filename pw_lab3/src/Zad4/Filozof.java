package Zad4;

import java.util.Random;

public class Filozof extends Thread {

    private final int id;
    private final WidelecMonitor monitor;
    private final Random rand = new Random();
    private long rep = 0;

    public Filozof(int id, WidelecMonitor monitor) {
        this.id = id;
        this.monitor = monitor;
        rep = 0;
    }

    public void run() {
        try {
            while (!interrupted()) {

                // myślenie
                Thread.sleep(rand.nextInt(15+5));
                monitor.wez(id,rep,id);
                // jedzenie
                Thread.sleep(rand.nextInt(5+1));

                monitor.odloz(id,rep,id);

            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}