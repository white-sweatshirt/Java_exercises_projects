package Zad2;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {

    // ===== MAIN =====
    public static void main(String[] args) throws InterruptedException {

        int m = 4, n = 2;
        int a = 5, b = 15, c = 1, d = 5;

        CzytelniaMonitor monitor = new CzytelniaMonitor();

        Thread[] czytelnicy = new Thread[m];
        Thread[] pisarze = new Thread[n];

        for (int i = 0; i < m; i++) {
            czytelnicy[i] = new Czytelnik(i, monitor, a, b, c, d);
            czytelnicy[i].start();
        }

        for (int i = 0; i < n; i++) {
            pisarze[i] = new Pisarz(i, monitor, a, b, c, d);
            pisarze[i].start();
        }

        // 10 sekund działania
        Thread.sleep(10000);

        // przerwanie
        for (Thread t : czytelnicy) t.interrupt();
        for (Thread t : pisarze) t.interrupt();

        // oczekiwanie
        for (Thread t : czytelnicy) t.join();
        for (Thread t : pisarze) t.join();

        System.out.println("Koniec programu.");
    }
}