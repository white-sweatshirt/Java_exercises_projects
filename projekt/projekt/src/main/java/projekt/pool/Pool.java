package projekt.pool;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Pool extends Thread {
    // This remains public and acts as the FlowPane layout layer for clients
    public FlowPane assginedPanel;
    Rectangle graphicalRepresentation;

    // Hardcoded max capacity as requested
    int maxPeopleInPool = 10;
    int currentPeopleInPool = 0;
    final Object lockForChecking = new Object();

    public Pool(Pane basicPane, DoubleBinding XFromStart, DoubleBinding YFromStart, DoubleBinding widthFromStart, DoubleBinding heightFromStart) {

        // 1. Create a regular base container Pane to hold the layered background and grid layout
        Pane baseLayerPane = new Pane();

        // Bind the base container coordinates to the incoming window values
        baseLayerPane.layoutXProperty().bind(XFromStart);
        baseLayerPane.layoutYProperty().bind(YFromStart);
        baseLayerPane.prefWidthProperty().bind(widthFromStart);
        baseLayerPane.prefHeightProperty().bind(heightFromStart);

        // 2. Setup the structural pool background shape
        this.graphicalRepresentation = new Rectangle();
        graphicalRepresentation.setFill(Color.LIGHTBLUE);
        graphicalRepresentation.setStroke(Color.BLACK);

        // Bind the background rectangle to fill the base container exactly
        graphicalRepresentation.widthProperty().bind(baseLayerPane.prefWidthProperty());
        graphicalRepresentation.heightProperty().bind(baseLayerPane.prefHeightProperty());

        // 3. Create the transparent client layout layer
        this.assginedPanel = new FlowPane();
        this.assginedPanel.setHgap(10);
        this.assginedPanel.setVgap(10);
        this.assginedPanel.setAlignment(Pos.CENTER_LEFT);
        this.assginedPanel.setPadding(new Insets(10)); // Safe buffer from edges

        // Bind the client flow layout container to match the size of the base container
        this.assginedPanel.prefWidthProperty().bind(baseLayerPane.prefWidthProperty());
        this.assginedPanel.prefHeightProperty().bind(baseLayerPane.prefHeightProperty());

        // 4. Correct Z-Ordering Insertion
        baseLayerPane.getChildren().add(graphicalRepresentation); // Added 1st (Bottom Layer)
        baseLayerPane.getChildren().add(this.assginedPanel);      // Added 2nd (Top Layer)

        // 5. Add the complete layered composite component into the main visual root
        basicPane.getChildren().add(baseLayerPane);
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

    // Add to Pool.java
    public int getCurrentPeopleCount() {
        synchronized (lockForChecking) {
            return currentPeopleInPool;
        }
    }

    public int getMaxCapacity() {
        return maxPeopleInPool;
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