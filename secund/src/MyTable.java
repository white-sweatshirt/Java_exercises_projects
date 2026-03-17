import java.util.Arrays;

public class MyTable
{
    public static void tableTest1()
    {
        int x = 10;
        double[] tab1 = new double[x];
        for (int i = 0; i < x; i++)
            tab1[i] = i;
        for (double a : tab1)
        {
            // a is just a copy of value on index in tab1
            System.out.println(a);
            a += 0.10;
        }
    }

    public static void varibleNumbersOfArguments(double... numTab)
    {
        // multiple arguments in java behaves like table of arguments
        // Arrays is fun class having nice methods for tables
        Arrays.sort(numTab);
        for (double w : numTab)
            System.out.println("pum pum different values: " + w);
    }

    public static void swapTest(int[] a, int[] b)
    {
        int temp = a[0];
        a[0] = b[0];
        b[0] = temp;
    }

    public static void table2()
    {
        int[][] carpeDiem = new int[10][10];

        for (int i = 0; i < carpeDiem.length; i++)
        {
            System.out.println("**************");
            for (int j = 0; j < carpeDiem[i].length; j++)
            {
                carpeDiem[i][j] = i + j;
                System.out.print(carpeDiem[i][j] + (carpeDiem[i][j] <= 9 ? "  " : " "));
            }
        }
    }
}
