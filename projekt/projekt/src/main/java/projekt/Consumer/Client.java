package projekt.Consumer;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import projekt.pool.Pool;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Client extends Thread {
    protected static Pane mainPane;
    protected static Pool[] allPools;

    // Publicly accessible synchronization tools for Subclasses and the Cleaner
    public static final ReentrantLock queLock = new ReentrantLock(true);
    public static final Condition normalPersonCanPass = queLock.newCondition();
    public static final Condition vipCanPass = queLock.newCondition();
    public static int vipsInQue = 0;

    protected int timeItWantsToSpendms;
    protected final int constRadius = 10;
    protected int age;

    protected Circle circleRepresentation;
    protected VBox componentLayoutWrapper;
    protected static Semaphore screenLocker = new Semaphore(1);

    public Client() {
        this.age = 12 + (int) (Math.random() * 53);
    }

    public static void setMainPane(Pane pane) {
        mainPane = pane;
    }

    public static void setPools(Pool[] pools) {
        allPools = pools;
    }

    public static int giveAmountOfVipes() {
        return vipsInQue;
    }

    public static Pool[] getAllPools() {
        return allPools;
    }

    public int getAge() {
        return this.age;
    }

    @Override
    public abstract void run();

    // General interface function for subclasses to safely try claiming a spot
    protected Pool claimFreePool() {
        for (Pool pool : allPools) {
            if (pool.tryEnter(this)) {
                return pool;
            }
        }
        return null;
    }

    protected void buildLayoutWrapper(Color circleColor) {
        this.componentLayoutWrapper = new VBox(2);
        this.componentLayoutWrapper.setAlignment(Pos.CENTER);

        this.circleRepresentation = new Circle(constRadius, circleColor);
        Text ageText = new Text(String.valueOf(this.age));
        ageText.setFill(Color.BLACK);
        ageText.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");

        this.componentLayoutWrapper.getChildren().addAll(circleRepresentation, ageText);
    }

    public void goToChosenPool(Pane poolPane) {
        if (componentLayoutWrapper != null) poolPane.getChildren().add(componentLayoutWrapper);
    }

    public void getOut(Pane pane) {
        if (componentLayoutWrapper != null) pane.getChildren().remove(componentLayoutWrapper);
    }
}