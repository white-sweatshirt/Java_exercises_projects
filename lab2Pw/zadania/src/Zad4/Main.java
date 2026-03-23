//Carpe Diem
package Zad4;

import java.util.Scanner;

public class Main implements Constans {


    public static void main() {
        Scanner input = new Scanner(System.in);
        Sem[] threads = new Sem[N_THREADS];
        System.out.println("czy chcesz synchronizacje \n 1- tak co innego -nie: ");
        byte a = input.nextByte();
        for (int i = 0; i < N_THREADS; i++)
            threads[i] = new Sem(i, '*', 100);
        if (a == 1)
            for (int i = 0; i < N_THREADS; i++)
                threads[i].setSynchronise(true);
        else
            for (int i = 0; i < N_THREADS; i++)
                threads[i].setSynchronise(false);
        for (int i = 0; i < N_THREADS; i++)
            threads[i].start();
        try {
            for (int i = 0; i < N_THREADS; i++)
                threads[i].join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}