package Zad3;
public class Main {
    public static void main(String[] args) {
        WidelecMonitor monitor = new WidelecMonitor();
        for (int i = 0; i < 5; i++) {
            new Filozof(i, monitor, 8, 12, 2, 6, 100).start();
        }
    }
}