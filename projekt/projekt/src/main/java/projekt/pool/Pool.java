package projekt.pool;

import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import projekt.Consumer.ClientWithChild;
import projekt.PoolsEnumeration;
import projekt.Consumer.Client;

public class Pool extends Thread {
    // This remains public and acts as the FlowPane layout layer for clients
    public FlowPane assginedPanel;
    Rectangle graphicalRepresentation;

    // --- Dynamic Text Objects ---
    private Text titleLabel;
    private Text statsLabel;

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
        this.assginedPanel.setPadding(new Insets(10, 10, 35, 10)); // Added extra bottom padding for text overlay clear-space

        // Bind the client flow layout container to match the size of the base container
        this.assginedPanel.prefWidthProperty().bind(baseLayerPane.prefWidthProperty());
        this.assginedPanel.prefHeightProperty().bind(baseLayerPane.prefHeightProperty());

        // 4. Create the Pool Name and Information Label HUD Layout Box
        VBox labelContainer = new VBox(2);
        labelContainer.setPadding(new Insets(5));
        labelContainer.setAlignment(Pos.BOTTOM_LEFT);

        // Pin the information text neatly to the bottom edge inside the regular container Pane
        labelContainer.layoutYProperty().bind(baseLayerPane.prefHeightProperty().subtract(35));
        labelContainer.layoutXProperty().setValue(10);

        this.titleLabel = new Text("Pool Initialization...");
        this.titleLabel.setFill(Color.BLACK);
        this.titleLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

        this.statsLabel = new Text("Avg Age: 0.0 | Count: 0/10");
        this.statsLabel.setFill(Color.DARKSLATEGRAY);
        this.statsLabel.setStyle("-fx-font-size: 10px;");

        labelContainer.getChildren().addAll(this.titleLabel, this.statsLabel);

        // 5. Correct Z-Ordering Insertion
        baseLayerPane.getChildren().add(graphicalRepresentation); // Added 1st (Bottom Layer)
        baseLayerPane.getChildren().add(labelContainer);          // Added 2nd (HUD Label Info Layer)
        baseLayerPane.getChildren().add(this.assginedPanel);      // Added 3rd (Top Layer for interactive clients)

        // 6. Add the complete layered composite component into the main visual root
        basicPane.getChildren().add(baseLayerPane);
    }

    // Setter to track which type of pool this instance represents and update titles accordingly
    public void setPoolType(PoolsEnumeration poolType) {
        this.poolType = poolType;
        Platform.runLater(() -> {
            if (this.poolType != null) {
                // Capitalize first letter cleanly for GUI aesthetics
                String name = this.poolType.name();
                this.titleLabel.setText(name.substring(0, 1).toUpperCase() + name.substring(1) + " Pool");
                updateVisualLabels();
            }
        });
    }

    public PoolsEnumeration getPoolType() {
        return this.poolType;
    }

    // Helper method to refresh display metrics cleanly on JavaFX Thread
    private void updateVisualLabels() {
        double avg = currentPeopleInPool == 0 ? 0.0 : (double) totalAgeInPool / currentPeopleInPool;
        String statsText = String.format("Avg Age: %.1f | Count: %d/%d", avg, currentPeopleInPool, maxPeopleInPool);
        this.statsLabel.setText(statsText);
    }

    // Updated to accept the client context for validation checks
    // Updated verification logic to enforce explicit class-type restrictions
    public boolean tryEnter(Client client) {
        synchronized (lockForChecking) {
            // Rule 1: Check hard limit capacity first
            if (currentPeopleInPool >= maxPeopleInPool) {
                return false;
            }

            // Is this client a family unit?
            boolean isFamily = (client instanceof ClientWithChild);

            // Rule 2: CHILDREN'S POOL RESTRICTION
            if (this.poolType == PoolsEnumeration.children) {
                if (!isFamily) {
                    return false; // Forbid regular customers and VIPs from entering the children's pool
                }
            }

            // Rule 3: OLYMPIC POOL RESTRICTION
            if (this.poolType == PoolsEnumeration.olympic) {
                if (isFamily || client.getAge() < 18) {
                    return false; // Forbid families completely, as well as anyone under 18
                }
            }

            // Rule 4: REGULAR POOL AGE POLICY
            if (this.poolType == PoolsEnumeration.regular) {
                int nextPeopleCount = currentPeopleInPool + 1;
                int nextTotalAge = totalAgeInPool + client.getAge();
                double projectedAverageAge = (double) nextTotalAge / nextPeopleCount;

                if (projectedAverageAge > 40.0) {
                    return false; // Deny entry if adding this customer breaks the 40-year average threshold
                }
            }

            // If all checks clear, admit the client safely
            currentPeopleInPool++;
            totalAgeInPool += client.getAge();

            // Dispatch visual update immediately to show live changes
            Platform.runLater(this::updateVisualLabels);
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

            // Dispatch visual updates immediately
            Platform.runLater(this::updateVisualLabels);
        }
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }
}