import java.util.Arrays;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        int randomValue;
        for (int i = 0; i < 10; i++)
            // pseudo random number generator just like rand in c with execution that it generates numbers
            // form subset of real numbers of [0,1)
            System.out.println("random pseudo value " + i + ": " + (int) (Math.random() * 100 % 100));
        System.out.println(Math.round(3.32));
        System.out.println(Long.MIN_VALUE);
        char a = 'ą';// char is 16 bits as opposed to c . It has Unicode coding for those 16 bits
        System.out.println(a);
        //   stringTesting();
        // stringTesting2();
        String strrr = "st";
        // methodStringTest(strrr);
        MyTable.varibleNumbersOfArguments(10.3, 312.13, 0.31, 3132.4, 3232.322);
        //MyTable.tableTest1();
        MyTable.table2();
        int a2 = 10, b = 31;
        Data ala = new Data();
        ala.a = 31;
        System.gc();
        changesToData(ala);
        System.out.println("ala.a = " + ala.a);

    }

    public static void stringTesting()
    {
        Scanner in = new Scanner(System.in);
        String a = "carpe DiĘm  ";
        System.out.println(a.toLowerCase());
        System.out.println(a);
        System.out.println(a.length());
        System.out.println(a + "2313");
        System.out.println(a.trim() + " 2313");
        System.out.println(a.charAt(3));
        a += "zupa";
        System.out.println(a);
        System.out.println("podaj string do wczytania: ");
        // method  takes next set of characters ended with white character
        // that is it ends with space.
        System.out.println("wczytano: " + a);
        a = in.nextLine();
        System.out.println("wczytano: " + a);
    }

    public static void stringTesting2()
    {
        String firstLine, secundLine;
        Scanner in = new Scanner(System.in);
        System.out.println("podja pierwsza linie: ");
        firstLine = in.nextLine();
        System.out.println("podaj druga linie do poruwnania: ");
        secundLine = in.nextLine();
        System.out.println(firstLine.compareTo(secundLine) > 0 ? "pierwsza wieksza" : "druga wieksza");
        String stringNumber = "32fa1323";
        int a = Integer.parseInt(stringNumber);

    }

    public static void methodStringTest(String a)
    {
        {
            int x = 10;
            System.out.println("x= " + x);
        }

    }

    public static void changesToData(Data data)
    {
        // kalsy sa przekazywane przez refencje
        data.a = 313;

    }

}
class  Circle
{
    double radious;
    static int numberOfCircles =0;
    public Circle()
    {

    }

}
