package Zad4;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.*;

public class WidelecMonitor {

    private final int N = 5;
    private final Semaphore[] widelec = new Semaphore[N];
    private final Semaphore lokaj = new Semaphore(4, true); // max 4 przy stole
    private final Semaphore chron = new Semaphore(1, true);

    public WidelecMonitor() {
        for (int i = 0; i < N; i++) {
            widelec[i] = new Semaphore(1, true); // każdy widelec dostępny
        }
    }

    public void wez(int i, long rep, int id) throws InterruptedException {
        int right = (i + 1) % N;

        lokaj.acquire();
        try {
            widelec[i].acquire();
            widelec[right].acquire();
            log(id, i, rep, ">>");
        } catch (InterruptedException e) {
            lokaj.release();
            throw e;
        }
    }

    public void odloz(int i, long rep, int id) throws InterruptedException {
        int right = (i + 1) % N;

        widelec[i].release();
        widelec[right].release();
        lokaj.release();

        log(id, i, rep, "<<");
    }

    public void log(int id, int i, long rep, String prefix) throws InterruptedException {
        chron.acquire();
        try {
            System.out.print(prefix + "[" + id + "," + rep + "] :: licz_fil_przy_stole=");

            System.out.print(" ? ");

            System.out.print(" [");
            for (int j = 0; j < N; j++) {
                System.out.print("w" + j + "=" + (widelec[j].availablePermits() > 0 ? 1 : 0));
                if (j < N - 1) System.out.print(", ");
            }
            System.out.println("]");
        } finally {
            chron.release();
        }
    }
}