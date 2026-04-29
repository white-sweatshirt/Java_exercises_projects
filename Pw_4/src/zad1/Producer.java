package zad1;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.Math;

class Producer extends Thread {
    private final SharedBuffer buffer;
    private final int idProducent;
    int nRepetions;

    public Producer(int id, SharedBuffer buffer, int rep) {
        this.idProducent = id;
        this.buffer = buffer;
        nRepetions = rep;
    }

    @Override
    public void run() {
        try {
            for (int nrRepetition = 1; nrRepetition <= nRepetions; nrRepetition++) {
                // --- Production happens here (between line 10 and 11) ---
                // element=[id_producent, nr repetition, position in buffer, whatever]

                try {
                    Thread.sleep((int) (Math.random() * 10 + 2)); // Slow down for visibility
                    buffer.pisz(idProducent,nrRepetition);
                }
                catch (InterruptedException e) {
                Thread.currentThread().interrupt();}
            }

    }
}