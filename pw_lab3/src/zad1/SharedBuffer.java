package zad1;

import java.util.concurrent.Semaphore;

class SharedBuffer {
    final String[] pula;
    final int N;
    int wej = 0; // index for producer
    int wyj = 0; // index for consumer
    int licz = 0;

    // Semaphores
    final Semaphore full;  // Controls available slots (wait when N)
    final Semaphore empty; // Controls available items (signal when added)

    public SharedBuffer(int size) {
        this.N = size;
        this.pula = new String[N];
        this.full = new Semaphore(N);
        this.empty = new Semaphore(0);
    }
}