package projekt.pool;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import projekt.PoolsEnumeration;
import projekt.Consumer.Client; // Assuming Client is in this package path

public class Pool extends Thread {
    // This remains public and acts as the FlowPane layout layer for clients
    public FlowPane assginedPanel;
    Rectangle graphicalRepresentation;

    // Hardcoded max capacity as requested
    int maxPeopleInPool = 10;
    int currentPeopleInPool = 0;

    // --- Age and Enum Type Tracking ---
    int totalAgeInPool = 0;
    private PoolsEnumeration poolType;

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

    // Setter to track which type of pool this instance represents
    public void setPoolType(PoolsEnumeration poolType) {
        this.poolType = poolType;
    }

    public PoolsEnumeration getPoolType() {
        return this.poolType;
    }

    // Updated to accept the client context for validation checks
    public boolean tryEnter(Client client) {
        synchronized (lockForChecking) {
            // Check hard limit capacity first
            if (currentPeopleInPool >= maxPeopleInPool) {
                return false;
            }

            // Enforce age constraint if this is the regular pool
            if (this.poolType == PoolsEnumeration.regular) {
                int nextPeopleCount = currentPeopleInPool + 1;
                int nextTotalAge = totalAgeInPool + client.getAge();
                double projectedAverageAge = (double) nextTotalAge / nextPeopleCount;

                if (projectedAverageAge > 40.0) {
                    return false; // Deny entry to maintain the average age policy
                }
            }

            // Admission valid: Update metrics safely inside synchronization lock
            currentPeopleInPool++;
            totalAgeInPool += client.getAge();
            return true;
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

    // Updated to subtract the leaving client's age metrics safely
    public void leave(Client client) {
        synchronized (lockForChecking) {
            currentPeopleInPool--;
            totalAgeInPool -= client.getAge();
            if (totalAgeInPool < 0) {
                totalAgeInPool = 0; // Boundary safety check
            }
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