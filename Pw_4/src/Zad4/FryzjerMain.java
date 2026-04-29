package Zad4;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;



public class FryzjerMain {
    public static void main(String[] args) {
        int m=10, n=5, k=4, l=6, a=8, b=12, c=2, d=6, powt=1000;
        FryzjerMonitor monitor = new FryzjerMonitor(k, l);

        for (int i = 1; i <= n; i++) new Fryzjer(i, monitor, powt).start();
        for (int i = 1; i <= m; i++) new Klient(i, monitor, a, b, c, d, powt).start();

        // Przerwanie po 6 sekundach
        new Thread(() -> {
            try {
                Thread.sleep(6000);
                System.out.println("Koniec czasu (6s). Zamykanie...");
                System.exit(0);
            } catch (InterruptedException e) { }
        }).start();
    }
}