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
        stringTesting();
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

    }

}
