package Inheritance;

public class Squere extends GeometricObject
{
    double side;

    Squere(double side)
    {
        super();
        this.side = side;
    }

    @Override
    public double giveArea()
    {
        return side * side;
    }

    @Override
    public String toText()
    {
        return "" + side + " " + this.giveArea();
    }

}
