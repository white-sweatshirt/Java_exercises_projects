package semaphoreFun;

import java.util.concurrent.*;
import java.lang.Math;

public class SemTest {
    public static Account account = new Account();
    private static Semaphore sharedSem = new Semaphore(1);

    public static void main() {
        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++)
            service.execute(new AccountIncAsych());
        service.shutdown();
        while (!service.isTerminated())
            ;
        System.out.println("stan konta: " + account.getBalance());
    }

    private static class AccountIncAsych implements Runnable {
        @Override
        public  void run() {
            account.deposit(1);
        }
    }

    private static class Account {

        static int balance = 0;

        public int getBalance() {
            return balance;
        }

        public void deposit(int amount) {
            try {
                sharedSem.acquire();
                int newBalance = balance + Math.max(amount, 0);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
                balance = newBalance;

            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            } finally {
                sharedSem.release();
            }
        }
    }
}
