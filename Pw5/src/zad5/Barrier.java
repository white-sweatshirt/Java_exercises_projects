package zad5;

public class Barrier {
    private final int parties;       // liczba wątków
    private int count = 0;           // ile już doszło
    private final Runnable action;   // kod do wykonania po synchronizacji

    public Barrier(int parties, Runnable action) {
        this.parties = parties;
        this.action = action;
    }

    public synchronized void await() throws InterruptedException {
        count++;

        if (count < parties) {
            wait();
        } else {
            System.out.println(">>> Wszystkie wątki osiągnęły barierę!");
            action.run();
            count = 0;
            notifyAll();
        }
    }
}