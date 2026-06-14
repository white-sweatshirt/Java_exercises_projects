package projekt.Consumer;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Semaphore;


public abstract class Client implements Runnable
{

    protected static ReentrantLock queLock = new ReentrantLock(true);
    protected static Condition normalPersonCanPass = queLock.newCondition();
    protected static int vipsInQue =0;
    protected int timeItWantsToSpendms;
    protected int whereItWantsToGo;
    final protected int constRadius = 10;
    protected Circle circleRepresentation;
    static protected Semaphore screenLocker = new Semaphore(1);

    @Override
    public abstract void run();

    public void Client()
    {
    }

    public void Client(Pane mainPane, Semaphore addingToScreen)
    {}
    public void goToChosenPool(Pane pool)
    {
        // It needs to add cordinates
        pool.getChildren().add(circleRepresentation);
    }
    public  void getOut(Pane pane)
    {
        pane.getChildren().remove(circleRepresentation);
    }
}
