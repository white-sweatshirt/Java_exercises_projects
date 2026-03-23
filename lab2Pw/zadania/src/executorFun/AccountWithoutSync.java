package executorFun;

import java.util.concurrent.*;

public class AccountWithoutSync {
    static Account account = new Account();


    public static void main() {
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++)
            executor.execute(new AddPennyTask());
        executor.shutdown();
        while (!executor.isTerminated())
            ;
        System.out.println("Stan konta: " + account.getBalance());

    }

    static private class AddPennyTask implements Runnable {
        @Override
        public void run() {
            account.deposit(1);
        }
    }

    private static class Account {
        private int balance;

        public int getBalance() {
            return balance;
        }

        public void deposit(int amount) {
            int newBalance = balance + Math.max(amount, 0);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            balance = newBalance;
        }
    }
}
