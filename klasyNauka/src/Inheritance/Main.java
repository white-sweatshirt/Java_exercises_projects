package Inheritance;

public class Main
{
    // carpe diem
    public static void main()
    {
        GeometricObject a = new Squere(10.0);
        GeometricObject b = new Circle(10.0);
        System.out.println("Area of a: " + a.giveArea());
        System.out.println("Area of b: " + Math.round(b.giveArea()));
        System.out.println(a.toText());
    }
}
