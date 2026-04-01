package Zad3;

import java.util.Random;

public class Czytelnik extends Thread {

    private final int id;
    private final CzytelniaMonitor monitor;
    private final Random rand;
    private final int a, b, c, d;

    public Czytelnik(int id, CzytelniaMonitor monitor, int a, int b, int c, int d) {
        this.id = id;
        this.monitor = monitor;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.rand = new Random();
    }

    public void run() {
        int powt = 0;

        try {
            while (!interrupted()) {
                powt++;
                // sprawy własne
                Thread.sleep(rand.nextInt(b - a + 1) + a);
                monitor.wejdzCzytelnik(id, powt);
                monitor.wyjdzCzytelnik(id, powt);
            }
        } catch (InterruptedException e) {
            // zakończenie
        }
    }
}