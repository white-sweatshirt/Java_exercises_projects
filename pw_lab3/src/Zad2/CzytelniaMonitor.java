package Zad2;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class CzytelniaMonitor {

    private AtomicInteger liczCzyt = new AtomicInteger(0);
    private AtomicInteger liczPis = new AtomicInteger(0);
    private AtomicInteger liczCzytPocz = new AtomicInteger(0);
    private AtomicInteger liczPisPocz = new AtomicInteger(0);

    private final Semaphore czytelnik = new Semaphore(1);
    private final Semaphore pisarze = new Semaphore(1);
    private final Semaphore chron = new Semaphore(1);

    // ===== CZYTELNIK =====
    public void wejdzCzytelnik(int id, int powt) {
        try {
            if (liczPis.get() + liczPis.get() > 0) {
                liczCzytPocz.incrementAndGet();
                czytelnik.acquire();
                liczCzytPocz.decrementAndGet();
            }
            liczCzyt.incrementAndGet();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log(">>>", "C-" + id, powt);
    }

    public void wyjdzCzytelnik(int id, int powt) {
        liczCzyt.decrementAndGet();
        log("<<<", "C-" + id, powt);
        if (liczCzyt.get() == 0) pisarze.release();
    }

    // ===== PISARZ =====
    public void wejdzPisarz(int id, int powt) {

        try {
            if (liczCzyt.get() + liczPis.get() > 0) {
                liczPisPocz.incrementAndGet();
                pisarze.acquire();
                liczPisPocz.getAndDecrement();
                log("==>", "P-" + id, powt);
            }
            liczPis.set(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void wyjdzPisarz(int id, int powt) {
        liczPis.set(0);
        log("<==", "P-" + id, powt);
        if (liczCzytPocz.get() > 0) {
            //signal all -not avaiale
            czytelnik.release(4);
        } else pisarze.release(1);
    }

    // ===== LOG =====
    private void log(String prefix, String id, int powt) {
        try {
            chron.acquire();
            System.out.println(prefix + " [" + id + ", " + powt + "] :: " + "[licz_czyt=" + liczCzyt + ", licz_czyt_pocz=" + liczCzytPocz + ", licz_pis=" + liczPis + ", licz_pis_pocz=" + liczPisPocz + "]");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            chron.release();
        }
    }
}