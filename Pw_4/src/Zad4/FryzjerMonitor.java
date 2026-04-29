package Zad4;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FryzjerMonitor {
    private int licz_czek = 0;      // klienci w poczekalni
    private int licz_wol_fot;       // wolne fotele
    private final int cap_poczekalni;

    private final Lock lock = new ReentrantLock();
    private final Condition klientDostepny = lock.newCondition();
    private final Condition fotelWolny = lock.newCondition();
    private final Condition uslugaZakonczona = lock.newCondition();

    public FryzjerMonitor(int fotele, int poczekalnia) {
        this.licz_wol_fot = fotele;
        this.cap_poczekalni = poczekalnia;
    }

    private void drukuj(String prefix, String id, int powt) {
        System.out.printf("%s  [%s, %d] :: %d, %d%n",
                prefix, id, powt, licz_czek, licz_wol_fot);
    }

    public boolean wejdzKlient(int id, int powt) throws InterruptedException {
        lock.lock();
        try {
            drukuj(">>>", "K-" + id, powt);
            if (licz_czek >= cap_poczekalni) {
                System.out.println("Brak miejsca w poczekalni dla K-" + id);
                return false;
            }

            licz_czek++;
            // Czekaj na wolny fotel
            while (licz_wol_fot == 0) {
                fotelWolny.await();
            }

            licz_czek--;
            licz_wol_fot--;
            klientDostepny.signal(); // Obudź fryzjera

            drukuj(">>>", "K-" + id, powt);
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void zwolnijFotel(int id, int powt) {
        lock.lock();
        try {
            drukuj("<<<", "K-" + id, powt);
            licz_wol_fot++;
            fotelWolny.signal();
            drukuj("<<<", "K-" + id, powt);
        } finally {
            lock.unlock();
        }
    }

    public void rozpocznijUsluge(int id, int powt) throws InterruptedException {
        lock.lock();
        try {
            // Fryzjer śpi, jeśli nie ma klientów czekających na fotel
            while (licz_wol_fot > 0 && licz_czek == 0 && !maKlientaPrzyFotelu()) {
                klientDostepny.await();
            }
            // (Uproszczona logika dla wielu fryzjerów)
        } finally {
            lock.unlock();
        }
    }

    private boolean maKlientaPrzyFotelu() {
        return false; /* pomocnicze */
    }
}

