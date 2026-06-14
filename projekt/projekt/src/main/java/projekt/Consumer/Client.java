package projekt.Consumer;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.concurrent.Semaphore;

public abstract class Client implements Runnable
{
    int timeItWantsToSpend;
    Circle circleRepresentation = new Circle();
    Semaphore screenLocker;
    @Override
    public void run()
    {

    }

    public void Client(Pane mainPane, Semaphore addingToScreen)
    {
        circleRepresentation.setCenterX(100);
        circleRepresentation.setRadius(100);
        circleRepresentation.setRadius(10);
        circleRepresentation.setStroke(Color.BLACK);
        circleRepresentation.setFill(Color.WHITE);
    }

    public void getOut()
    {

    }
}
