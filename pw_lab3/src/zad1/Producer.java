package zad1;

import java.util.concurrent.Semaphore;

public class Producer {
    static Semaphore full;
    static Semaphore empty;
    private int id;
    public Producer(Semaphore full1, Semaphore empty1,int id)
    {
        full = full1;
        empty = empty1;
        this.id = id;
    }
}
