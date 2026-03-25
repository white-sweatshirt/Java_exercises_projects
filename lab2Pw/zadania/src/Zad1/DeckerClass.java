package Zad1;

public class DeckerClass extends Thread
{
    // Carpe Diem

    // chce to odpowiednik K1 oraz K2 czyli rezerwacji sekcji krytcznej
    static volatile boolean[] chce = new boolean[2];
    static volatile int czyjaKolej = 0;
    private static char []znaki =new char[2];
    static
    {
        znaki[0]='+';
        znaki[1]='-';
    }



    private int nr = 0;
    static boolean synchronise;
    private int nRepetions;

    public void setSynchronise(boolean toSynchronise)
    {
        synchronise = toSynchronise;
    }

    public DeckerClass(int nr,  int nRepetions)
    {

        this.nr = nr;
        this.nRepetions = nRepetions;
        chce[nr] = true;
    }

    private void privateJob()
    {
        try
        {
            sleep((int) (Math.random() * 9 + 1));
        } catch (InterruptedException e)
        {
            System.out.println("DeckClass privateJob interrupted");
            System.out.println(e.getMessage());
        }

    }

    static final int TIMES_TO_WRITE = 100;

    private void wirteSeparator()
    {
        for (int i = 0; i < TIMES_TO_WRITE; i++)
            System.out.print(znaki[nr]);
        System.out.print("\n");
    }

    private void criticalSection(int numberOfRepetion)
    {
        System.out.println("Sekcja krytyczna wątku: Dekker-" + (nr + 1) + ",nr powt.=" + numberOfRepetion);
        wirteSeparator();
    }

    public void unsynchronizedAct()
    {
        for (int i = 0; i < nRepetions; i++)
        {
            privateJob();
            criticalSection(i);
        }
    }

    public void synchronizedAct()
    {
        int oppositeNumber = (nr == 0 ? 1 : 0);

        for (int i = 0; i < nRepetions; i++)
        {
            privateJob();
            chce[nr] = true;
            while (chce[oppositeNumber])
            {
                if(czyjaKolej == oppositeNumber)
                {
                    chce[nr] = false;
                    while (czyjaKolej == oppositeNumber)
                        ;
                    chce[nr] = true;
                }
            }
            criticalSection(i);
            czyjaKolej = oppositeNumber;
            chce[nr] = false;
        }
    }


    @Override
    public void run()
    {
        if(synchronise)
            synchronizedAct();
        else
            unsynchronizedAct();
    }

}
