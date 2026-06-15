package projekt.pool;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Pool extends Thread {
    public Pane assginedPanel;
    Rectangle graphicalRepresentation;

    // Hardcoded max capacity as requested
    int maxPeopleInPool = 10;
    int currentPeopleInPool = 0;
    final Object lockForChecking = new Object();

    public Pool(Pane basicPane, DoubleBinding XFromStart,
                DoubleBinding YFromStart, DoubleBinding widthFromStart, DoubleBinding heightFromStart) {

        this.assginedPanel = new Pane();
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

    // Replaces isFree() and enter()
    public boolean tryEnter() {
        synchronized (lockForChecking) {
            if (currentPeopleInPool < maxPeopleInPool) {
                currentPeopleInPool++; // Claim it immediately while locked
                return true;
            }
            return false;
        }
    }

    public void leave() {
        synchronized (lockForChecking) {
            currentPeopleInPool--;
        }
    }

    @Override
    public void run() {
        // As an independent thread, it can monitor its state or run maintenance.
        // For now, it stays alive until the program terminates.
        while (!isInterrupted()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }
}