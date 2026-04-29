package Zad3;
import java.util.concurrent.locks.*;
import java.util.*;
class Filozof extends Thread {
    private final int id;
    private final WidelecMonitor monitor;
    private final int a, b, c, d, powt;
    private final Random rand = new Random();

    public Filozof(int id, WidelecMonitor monitor, int a, int b, int c, int d, int powt) {
        this.id = id; this.monitor = monitor;
        this.a = a; this.b = b; this.c = c; this.d = d; this.powt = powt;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= powt; i++) {
                // Rozmyślanie
                Thread.sleep(rand.nextInt(b - a + 1) + a);
                monitor.wezWidelce(id, i);
                // Posiłek
                Thread.sleep(rand.nextInt(d - c + 1) + c);
                monitor.odlozWidelce(id, i);
            }
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}