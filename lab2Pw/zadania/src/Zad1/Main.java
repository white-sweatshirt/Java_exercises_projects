//Carpe Diem

package  Zad1;
import java.util.Scanner;

public class Main
        {

public static void main()
{
    Scanner input = new Scanner(System.in);
    DeckerClass first = new DeckerClass(0, '+', 100);
    DeckerClass secund = new DeckerClass(1, '-', 100);
    System.out.println("czy chcesz synchronizacje \n 1- tak co innego -nie: ");
    byte a = input.nextByte();
    if(a == 1)
        first.setSynchronise(true);
    else
        first.setSynchronise(false);
    first.start();
    secund.start();

    try
    {
        first.join();
        secund.join();
    } catch (InterruptedException e)
    {
        System.out.println(e.getMessage());
    }
}
                }
