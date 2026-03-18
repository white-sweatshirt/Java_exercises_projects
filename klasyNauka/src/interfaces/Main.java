package interfaces;
public class Main
{
    public static void main()
    {
        Apple apple = new Apple();
        apple.writeLala();
        System.out.println(apple.howToEat());
    }
}

class Apple implements Edible
{
    public final int APPLES_IN_BASCETS=100;
    public String howToEat()
    {
        return "poprostu gryz";
    }

    public void writeLala()
    {
        System.out.println("la la la bum dum");
    }
}
