package interfaces;

import java.lang.String;

public interface Edible extends Forgatable2, Forgetable1
{
    String howToEat();
}

interface Forgetable1
{
    void writeLala();

}

interface Forgatable2
{
    public final int NUMBERS_OF_THREADS = 100;
}