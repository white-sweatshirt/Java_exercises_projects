import java.util.Scanner;
import java.util.*;

public class Main
{


    public static void main(String[] args)
    {

        System.out.println("huj w dupie: ");
        System.out.print("happens to be: ");
        System.out.println((10.32 + 32 * 10) / 2);
        double a = 889;
        System.out.print("ddad=" + a);
        Random d = new Random();
        for (int i = 32; i < 141; i++)
            ;
        Scanner scannerInstance = new Scanner(System.in);
        int dummt = scannerInstance.nextInt();
        System.out.println("\npumpit: " + dummt);
        scannerInstance = null;

        System.out.print("garbage collected Uwu\n");
        int x = 2;
        System.out.println(x = 2);
        x = x % 2 == 1 ? 1 : 0;
        byte ala = 'A';// byte is char from C/C++ it is kind of better called
        System.out.println(x);
        // imposibale for java to use text[]="dddupa";
        byte textTableLikeC[] = {'1', '3', 'c', 'a', '\0'};
        System.out.println(ala);
        scanerTestFunction();
        System.gc();
    }

    public static void scanerTestFunction()
    {
        // deklaracja rerfencji na klase skaner udostepniajaca metody pobierania liczb
        Scanner input = new Scanner(System.in);
        double a = input.nextDouble();
        System.out.print("wczytano w metodzie a: " + a);
    }

}
