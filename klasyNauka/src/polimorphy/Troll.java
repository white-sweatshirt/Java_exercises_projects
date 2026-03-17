package polimorphy;

public class Troll extends MagicalCreature
{
    public double returnHp()
    {
        return hp + 10.1;
    }

    public static void unboxingTest()
    {
        int x = Integer.valueOf(2) + Integer.valueOf(3);
        Integer z = 1 + 3;
        System.out.println("x= " + x);
        System.out.println("z= " + z.intValue());
    }
}

abstract class MagicalCreature
{
    protected int hp = 199;

    public double returnHp()
    {
        return hp;
    }

}
