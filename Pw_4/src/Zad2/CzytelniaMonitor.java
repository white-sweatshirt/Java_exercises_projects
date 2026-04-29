package Zad2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CzytelniaMonitor {
    private int licz_czyt = 0;
    private int licz_czyt_pocz = 0;
    private int licz_pis = 0;
    private int licz_pis_pocz = 0;

    private final Lock lock = new ReentrantLock();
    private final Condition canRead = lock.newCondition();
    private final Condition canWrite = lock.newCondition();

    private void drukuj(String prefix, String id, int powt) {
        System.out.printf("%s [%s, %d] :: [licz_czyt=%d, licz_czyt_pocz=%d, licz_pis=%d, licz_pis_pocz=%d]%n",
                prefix, id, powt, licz_czyt, licz_czyt_pocz, licz_pis, licz_pis_pocz);
    }

    public void wejdzCzytelnik(int id, int powt) throws InterruptedException {
        lock.lock();
        try {
            licz_czyt_pocz++;
            drukuj(">>>",  "C-" + id, powt);

            // Czytelnik czeka, gdy w środku jest pisarz lub gdy są oczekujący pisarze (priorytet pisarzy)
            while (licz_pis > 0 || licz_pis_pocz > 0) {
                canRead.await();
            }

            licz_czyt_pocz--;
            licz_czyt++;
            drukuj(">>>", "C-" + id, powt);
        } finally {
            lock.unlock();
        }
    }

    public void wyjdzCzytelnik(int id, int powt) {
        lock.lock();
        try {
            drukuj("<<<", "C-" + id, powt);
            licz_czyt--;
            if (licz_czyt == 0) {
                canWrite.signal(); // Jeśli nie ma czytelników, obudź pisarza
            }
            drukuj("<<<", "C-" + id, powt);
        } finally {
            lock.unlock();
        }
    }

    public void wejdzPisarz(int id, int powt) throws InterruptedException {
        lock.lock();
        try {
            licz_pis_pocz++;
            drukuj("==>", "P-" + id, powt);

            // Pisarz czeka, gdy ktokolwiek jest w środku
            while (licz_czyt > 0 || licz_pis > 0) {
                canWrite.await();
            }

            licz_pis_pocz--;
            licz_pis++;
            drukuj("==>",  "P-" + id, powt);
        } finally {
            lock.unlock();
        }
    }

    public void wyjdzPisarz(int id, int powt) {
        lock.lock();
        try {
            drukuj("<==",  "P-" + id, powt);
            licz_pis--;
            // Pisarz zwalnia miejsce - najpierw budzimy wszystkich czytelników (priorytet)
            // lub innego pisarza, jeśli brak czytelników.
            if (licz_czyt_pocz > 0) {
                canRead.signalAll();
            } else {
                canWrite.signal();
            }
            drukuj("<==",  "P-" + id, powt);
        } finally {
            lock.unlock();
        }
    }
}