package Zad6;

public class Licznik
{
    private  long count=0;
    public long get()
    {
        return count;
    }
    public void  incNiesynch()
    {
        count++;
    }
}
