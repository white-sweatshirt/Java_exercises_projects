import Soup.*;
import polimorphy.Troll;

public class KlasaTest
{
    public static void main()
    {
        TestClass.useHiddenMethodFromMain();
        // impossible to refence hidden class type  HiddenClass
        TestClass firstUser = new TestClass();
        TestClass secundUser = firstUser.giveRefence();
        secundUser.setForce(10.32);
        System.out.println("ustawiona sila to: " + firstUser.giveForce());
        Troll troll1 = new Troll();
        System.out.println("troll hp: " + troll1.returnHp());
        Troll.unboxingTest();
    }

}
