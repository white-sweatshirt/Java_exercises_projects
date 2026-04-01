package Zad4;
public class Main {

    public static void main(String[] args) throws InterruptedException {

        WidelecMonitor monitor = new WidelecMonitor();

        Filozof[] filozofowie = new Filozof[5];

        for (int i = 0; i < 5; i++) {
            filozofowie[i] = new Filozof(i, monitor);
            filozofowie[i].start();
        }

        Thread.sleep(5000);

        for (Filozof f : filozofowie) f.interrupt();
        for (Filozof f : filozofowie) f.join();

        System.out.println("Koniec.");
    }
}