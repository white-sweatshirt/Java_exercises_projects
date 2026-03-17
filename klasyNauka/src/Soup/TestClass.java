package Soup;

public class TestClass
{
    protected double force;

    public static void useHiddenMethodFromMain()
    {
        System.out.println("DUm dum");
        HiddenClass.hiddenMethodForKlasaTest();
    }

    public TestClass()
    {
        force = 0.0;
    }

    public double giveForce()
    {
        return force;
    }

    public TestClass giveRefence()
    {
        // this gives refence to instance of class
        return this;
    }

    public void setForce(double newForce)
    {
        this.force = newForce;
    }
}

class HiddenClass
{
    public static void hiddenMethodForKlasaTest()
    {
        System.out.println("BIm bop");
    }

}

interface supperInterface
{
}
