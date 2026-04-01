package Zad4;

import java.util.Random;

public class Filozof extends Thread {

    private final int id;
    private final WidelecMonitor monitor;
    private final Random rand = new Random();

    public Filozof(int id, WidelecMonitor monitor) {
        this.id = id;
        this.monitor = monitor;
    }

    public void run() {
        try {
            while (!interrupted()) {

                // myślenie
                Thread.sleep(rand.nextInt(1000));

                monitor.wez(id);
                System.out.println("Filozof " + id + " je");

                // jedzenie
                Thread.sleep(rand.nextInt(500));

                monitor.odloz(id);
                System.out.println("Filozof " + id + " odkłada widelce");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}