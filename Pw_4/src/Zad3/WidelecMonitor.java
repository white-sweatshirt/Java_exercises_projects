package Zad3;
import java.util.concurrent.locks.*;
import java.util.Arrays;
class WidelecMonitor {
    private final int N = 5;
    private final int[] widelce = new int[N]; // 0 - wolny, 1 - zajęty
    private int licz_fil_przy_stole = 0;
    private final Lock lock = new ReentrantLock();
    private final Condition[] mozeJesc = new Condition[N];

    public WidelecMonitor() {
        for (int i = 0; i < N; i++) mozeJesc[i] = lock.newCondition();
    }

    private void drukuj(String prefix, String symbol, int id, int powt) {
        System.out.printf("%s (%s) [F-%d, %d] :: %s – %d%n",
                prefix, symbol, id, powt, Arrays.toString(widelce), licz_fil_przy_stole);
    }

    public void wezWidelce(int id, int powt) throws InterruptedException {
        lock.lock();
        try {
            drukuj(">>>", "*", id, powt);
            // Filozof czeka, aż oba widelce (lewy i prawy) będą wolne
            while (widelce[id] == 1 || widelce[(id + 1) % N] == 1) {
                mozeJesc[id].await();
            }
            widelce[id] = 1;
            widelce[(id + 1) % N] = 1;
            licz_fil_przy_stole++;
            drukuj(">>>", "**", id, powt);
        } finally {
            lock.unlock();
        }
    }

    public void odlozWidelce(int id, int powt) {
        lock.lock();
        try {
            drukuj("<<<", "*", id, powt);
            widelce[id] = 0;
            widelce[(id + 1) % N] = 0;
            licz_fil_przy_stole--;
            // Powiadom sąsiadów, że widelce są wolne
            mozeJesc[(id + N - 1) % N].signal();
            mozeJesc[(id + 1) % N].signal();
            drukuj("<<<", "**", id, powt);
        } finally {
            lock.unlock();
        }
    }
}