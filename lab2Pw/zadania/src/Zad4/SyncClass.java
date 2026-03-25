package Zad4;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class SyncClass extends Thread implements Constans {
    // chce to odpowiednik K1 oraz K2 czyli rezerwacji sekcji krytcznej
    private char znak;
    private static Semaphore sem = new Semaphore(1);
    private int nr = 0;
    static byte mode;
    private int nRepetions;
    private Random a = new Random();
    private static char[] znaki = new char[N_THREADS];
    private static Object object = new Object();

    static {
        znaki[0] = '+';
        znaki[1] = '-';
        znaki[2] = '*';
        znaki[3] = '~';
        znaki[4] = '^';
    }

    public void setMode(byte newMode) {
        mode = newMode;
    }

    public SyncClass(int nr, int nRepetions) {
        this.nr = nr;
        this.nRepetions = nRepetions;
    }

    private void privateJob() {
        try {
            sleep((int) (Math.random() * 9 + 1));
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

    }

    static final int TIMES_TO_WRITE = 100;

    private void wirteSeparator() {
        for (int i = 0; i < TIMES_TO_WRITE; i++)
            System.out.print(znaki[nr]);
        System.out.print("\n");
    }

    private void criticalSection(int numberOfRepetion) {
        System.out.println("Sekcja krytyczna wątku: SemThread-" + (nr + 1) + ",nr powt.=" + (numberOfRepetion + 1));
        wirteSeparator();
    }

    private static final ReentrantLock lock = new ReentrantLock();

    public void dzialanieLock() {
        for (int i = 0; i < nRepetions; i++) {
            try {

                privateJob();
                lock.lock();
                criticalSection(i);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                lock.unlock();
            }
        }
    }

    public void dzialanieMetSynchr() {
        for (int i = 0; i < nRepetions; i++) {
            privateJob();
            synchronized (object) {
                criticalSection(i);
            }
        }
    }

    public void dzialanieSem() {
        for (int i = 0; i < nRepetions; i++) {
            privateJob();
            try {
                sem.acquire();
                criticalSection(i);

            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            } finally {
                sem.release();
            }
        }
    }

    @Override
    public void run() {
        if (mode == 0)
            dzialanieSem();
        else if (mode == 1)
            dzialanieLock();
        else
            dzialanieMetSynchr();
    }

    public int giveMax(int[] table) {
        int max = Integer.MIN_VALUE;
        for (int w : table)
            if (w > max)
                max = w;
        return max;
    }
}
