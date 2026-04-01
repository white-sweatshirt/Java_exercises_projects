package zad1;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.Math;

class Producer extends Thread {
    private final SharedBuffer buffer;
    private final int idProducent;

    public Producer(int id, SharedBuffer buffer) {
        this.idProducent = id;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            for (int nrRepetition = 1; nrRepetition <= 10; nrRepetition++) {
                // --- Production happens here (between line 10 and 11) ---
                // element=[id_producent, nr repetition, position in buffer, whatever]
                String elem = "[" + idProducent + ", " + nrRepetition + ", " + buffer.wej + ", extra_data]";
                Thread.sleep((int) (Math.random() * 10 + 2)); // Slow down for visibility

                buffer.full.acquire();
                try {
                    buffer.pula[buffer.wej] = elem;               // Line 14
                    buffer.wej = (buffer.wej + 1) % buffer.N;     // Line 15
                    buffer.licz++;                                // Line 16
                } finally {
                    buffer.empty.release();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}