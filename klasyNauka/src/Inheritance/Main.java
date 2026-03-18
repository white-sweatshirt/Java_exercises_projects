package Inheritance;

import java.util.ArrayList;

public class Main
{
    // carpe diem
    public static void main()
    {
        //
        GeometricObject a = new Squere(10.0);
        GeometricObject b = new Circle(10.0);
        System.out.println("Area of a: " + a.giveArea());
        System.out.println("Area of b: " + Math.round(b.giveArea()));
        System.out.println(a.toText());
        if (a instanceof Squere)
            System.out.println("mamy kwadrat udajacy objeckt");
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (int i = 0; i < 14; i++)
            arrayList.add(i);
        // dynamic sizing of array.
        for (Integer w : arrayList)
            System.out.println(w.intValue());
    }
}
