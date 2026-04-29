package Zad4;
public class Fryzjer extends Thread {
    private final int id, powt;
    private final FryzjerMonitor monitor;

    public Fryzjer(int id, FryzjerMonitor m, int p) {
        this.id = id; this.monitor = m; this.powt = p;
    }

    public void run() {
        try {
            for (int i = 1; i <= powt; i++) {
                monitor.rozpocznijUsluge(id, i);
                // Fryzjer pracuje (czas kontrolowany przez sleep klienta w tej symulacji)
            }
        } catch (InterruptedException e) { }
    }
}