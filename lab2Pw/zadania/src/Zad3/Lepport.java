package Zad3;

public class Lepport extends Thread implements Constans {
    // Carpe Diem
    // chce to odpowiednik K1 oraz K2 czyli rezerwacji sekcji krytcznej
    private static char[] znaki = new char[N_THREADS];

    static {
        znaki[0] = '+';
        znaki[1] = '-';
        znaki[2] = '*';
        znaki[3] = '~';
        znaki[4] = '^';
    }

    static volatile boolean[] wybieranie = new boolean[N_THREADS];
    static volatile int[] numerek = new int[N_THREADS];
    private int nr = 0;
    static boolean synchronise;
    private int nRepetions;

    public void setSynchronise(boolean toSynchronise) {
        synchronise = toSynchronise;
    }

    public Lepport(int nr, int nRepetions) {
        this.nr = nr;
        this.nRepetions = nRepetions;
    }

    private void privateJob() {
        try {
            sleep((int) (Math.random() * 9 + 1));
        } catch (InterruptedException e) {
            System.out.println("DeckClass privateJob interrupted");
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
        System.out.println("Sekcja krytyczna wątku: Lamport-" + (nr + 1) + ",nr powt.=" + (numberOfRepetion + 1));
        wirteSeparator();
    }

    public void dzialanieNiesynchr() {
        for (int i = 0; i < nRepetions; i++) {
            privateJob();
            criticalSection(i);
        }
    }

    public void dzialanieSynchr() {
        for (int i = 0; i < nRepetions; i++) {
            privateJob();
            wybieranie[nr] = true;// wymusznie jednoznacznosici numerkow
            numerek[nr] = giveMax(numerek) + 1;
            wybieranie[nr] = false;
            for (int j = 0; j < N_THREADS; j++) {
                while (wybieranie[j])
                    ;
                while (numerek[j] != 0 && (numerek[j] < numerek[nr] || (numerek[j] == numerek[nr] && j < nr)))
                    ;
            }
            criticalSection(i);
            numerek[nr] = 0;
        }
    }

    @Override
    public void run() {
        if (synchronise)
            dzialanieSynchr();
        else
            dzialanieNiesynchr();
    }

    public int giveMax(int[] table) {
        int max = Integer.MIN_VALUE;
        for (int w : table)
            if (w > max)
                max = w;
        return max;
    }
}
