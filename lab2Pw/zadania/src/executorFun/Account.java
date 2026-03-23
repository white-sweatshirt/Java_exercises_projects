package executorFun;

import java.lang.Math;

public class Account {
    private int balance;

    public int getBalance() {
        return balance;
    }

    public void deposit(int amount) {
        int newBalance = balance + Math.max(amount, 0);
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        balance = newBalance;
    }
}
