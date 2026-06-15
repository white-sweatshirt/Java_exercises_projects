package projekt.pool;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Pool {
    // class meant only to mechanicly represent given pool.
    // it has representation  on graphics it just gives interface for usage of pool.
    // but after intital creation it wont do anything with it it will serve as a kind of thing to manage
    // given pool.
    Pane assginedPanel;
    Rectangle graphicalRepresentation;
    int maxPeopleInPool;// purly helping just serves to minimize calculations.
    int currentPeopleInPool;
    final Object lockForChecking = new Object();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition pullFull = lock.newCondition();

    public boolean sayWheterThereIsPlace() {
        synchronized (lockForChecking) {
            return currentPeopleInPool < maxPeopleInPool;
        }
    }

    public Pool(Pane basicPane, DoubleBinding XFromStart,
                DoubleBinding YFromStart, DoubleBinding widthFromStart, DoubleBinding heightFromStart) {

        this.assginedPanel = new Pane();
        this.maxPeopleInPool = maxPeopleInPool;
        graphicalRepresentation = new Rectangle();

        graphicalRepresentation.setFill(Color.LIGHTBLUE);
        graphicalRepresentation.setStroke(Color.BLACK);

        assginedPanel.layoutXProperty().bind(XFromStart);
        assginedPanel.layoutYProperty().bind(YFromStart);
        assginedPanel.prefWidthProperty().bind(widthFromStart);
        assginedPanel.prefHeightProperty().bind(heightFromStart);

        graphicalRepresentation.widthProperty().bind(assginedPanel.prefWidthProperty());
        graphicalRepresentation.heightProperty().bind(assginedPanel.prefHeightProperty());
        assginedPanel.getChildren().add(graphicalRepresentation);
        basicPane.getChildren().add(assginedPanel);
    }

}
