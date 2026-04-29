package Zad4;

import java.util.Random;

// Skrócone klasy wątków dla czytelności
public class Klient extends Thread {
    private final int id, a, b, c, d, powt;
    private final FryzjerMonitor monitor;
    private final Random rand = new Random();

    public Klient(int id, FryzjerMonitor m, int a, int b, int c, int d, int p) {
        this.id = id; this.monitor = m; this.a = a; this.b = b; this.c = c; this.d = d; this.powt = p;
    }

    public void run() {
        try {
            for (int i = 1; i <= powt; i++) {
                Thread.sleep(rand.nextInt(b - a + 1) + a);
                if (monitor.wejdzKlient(id, i)) {
                    Thread.sleep(rand.nextInt(d - c + 1) + c); // Czas usługi
                    monitor.zwolnijFotel(id, i);
                }
            }
        } catch (InterruptedException e) { }
    }
}

