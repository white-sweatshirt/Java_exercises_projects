//Carpe Diem
package Zad4;

import java.util.Scanner;

public class Main implements Constans {


    public static void main() {
        Scanner input = new Scanner(System.in);
        Sem[] threads = new Sem[N_THREADS];
        System.out.println("0. synchronizacja semafor , \n 1. synchronizacja lock \n inne- synchronizacja metoda synchronizowana: ");
        byte a = input.nextByte();
        for (int i = 0; i < N_THREADS; i++)
            threads[i] = new Sem(i, '*', 100);
        for (int i = 0; i < N_THREADS; i++)
            threads[i].setMode(a);

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