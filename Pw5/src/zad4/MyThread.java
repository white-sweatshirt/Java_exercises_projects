import java.util.Random;

public class Worker implements Runnable {
    private final MyBarrier barrier;
    private final int id;
    private final Random rand = new Random();

    public Worker(MyBarrier barrier, int id) {
        this.barrier = barrier;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            System.out.println("Wątek " + id + " start");

            // symulacja pracy (2–5 sekund)
            int sleepTime = 2000 + rand.nextInt(3000);
            Thread.sleep(sleepTime);

            System.out.println("Wątek " + id + " przed barierą");

            barrier.await();

            System.out.println("Wątek " + id + " po barierze - koniec");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}