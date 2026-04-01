package Zad4;
import java.util.concurrent.Semaphore;

public class WidelecMonitor {

    private final int N = 5;

    private boolean[] zajety = new boolean[N];
    private int jest = 0;

    private final Semaphore lokaj = new Semaphore(4); // max 4 filozofów
    private final Semaphore[] widelec = new Semaphore[N];
    private final Semaphore mutex = new Semaphore(1);

    public WidelecMonitor() {
        for (int i = 0; i < N; i++) {
            zajety[i] = false;
            widelec[i] = new Semaphore(1);
        }
    }

    // ===== WEŹ =====
    public void wez(int i) throws InterruptedException {

        lokaj.acquire(); // if jest == 4 → blokada

        mutex.acquire();
        jest++;

        // lewy widelec
        if (zajety[i]) {
            mutex.release();
            widelec[i].acquire();
            mutex.acquire();
        }
        zajety[i] = true;

        // prawy widelec
        int right = (i + 1) % N;
        if (zajety[right]) {
            mutex.release();
            widelec[right].acquire();
            mutex.acquire();
        }
        zajety[right] = true;

        mutex.release();
    }

    // ===== ODŁÓŻ =====
    public void odloz(int i) throws InterruptedException {

        mutex.acquire();

        int right = (i + 1) % N;

        zajety[i] = false;
        widelec[i].release();

        zajety[right] = false;
        widelec[right].release();

        jest--;

        mutex.release();

        lokaj.release();
    }
}