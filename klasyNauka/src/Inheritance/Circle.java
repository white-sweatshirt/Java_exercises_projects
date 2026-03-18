package Inheritance;

import java.lang.Math;

public class Circle extends GeometricObject
{
    double radius;
    double a;

    Circle(double radius)
    {
        super();
        this.radius = radius;
    }

    @Override
    public double giveArea()
    {
        super.testShow();
        return radius * radius * Math.PI;

    }

    @Override
    public String toText()
    {
        return "" + radius + this.giveArea();
    }
}
