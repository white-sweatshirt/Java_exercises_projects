package projekt.Consumer;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import projekt.pool.Pool;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public abstract class Client extends Thread {
    // Add this to your static variables in Client.java
    protected static Pane mainPane;

    // Add this setter method
    public static void setMainPane(Pane pane) {
        mainPane = pane;
    }

    public static int giveAmountOfVipes() {
        return vipsInQue;
    }

    protected static ReentrantLock queLock = new ReentrantLock(true);
    protected static Condition normalPersonCanPass = queLock.newCondition();
    protected static Condition vipCanPass = queLock.newCondition();

    protected static int vipsInQue = 0;
    protected static Pool[] allPools; // Reference to all available pools

    protected int timeItWantsToSpendms;
    final protected int constRadius = 10;
    protected Circle circleRepresentation;
    static protected Semaphore screenLocker = new Semaphore(1);

    public static void setPools(Pool[] pools) {
        allPools = pools;
    }

    @Override
    public abstract void run();

    // Replaces getFreePool()
    protected Pool claimFreePool() {
        for (Pool pool : allPools) {
            if (pool.tryEnter()) {
                return pool; // Spot is successfully claimed
            }
        }
        return null;
    }

    public void goToChosenPool(Pane poolPane) {
        // Position clients randomly or sequentially in the UI here if needed
        circleRepresentation.setCenterX(Math.random() * poolPane.getPrefWidth());
        circleRepresentation.setCenterY(Math.random() * poolPane.getPrefHeight());
        poolPane.getChildren().add(circleRepresentation);
    }

    public void getOut(Pane pane) {
        pane.getChildren().remove(circleRepresentation);
    }
}