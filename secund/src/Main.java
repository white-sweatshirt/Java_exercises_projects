import  java.util.Scanner;
public class Main
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        int randomValue;
        for(int i=0;i<10;i++)
            // pseudo random number generator just like rand in c with execption that it generates numbers
            // form subset of real numbers of [0,1)
            System.out.println("random pseudo value "+i+": " + (int)(Math.random()*100%100));
        System.out.println(Math.round(3.32));
        System.out.println(Long.MIN_VALUE);

    }
}
