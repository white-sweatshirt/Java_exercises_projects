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

    private Text titleLabel;
    private Text statsLabel;

    int maxPeopleInPool = 10;
    int currentPeopleInPool = 0;

    int totalAgeInPool = 0;
    private PoolsEnumeration poolType;

    final Object lockForChecking = new Object();

    public Pool(Pane basicPane, DoubleBinding XFromStart, DoubleBinding YFromStart, DoubleBinding widthFromStart, DoubleBinding heightFromStart) {

        Pane baseLayerPane = new Pane();

        // Bind the base container coordinates to the incoming window values
        baseLayerPane.layoutXProperty().bind(XFromStart);
        baseLayerPane.layoutYProperty().bind(YFromStart);
        baseLayerPane.prefWidthProperty().bind(widthFromStart);
        baseLayerPane.prefHeightProperty().bind(heightFromStart);

        this.graphicalRepresentation = new Rectangle();
        graphicalRepresentation.setFill(Color.LIGHTBLUE);
        graphicalRepresentation.setStroke(Color.BLACK);

        graphicalRepresentation.widthProperty().bind(baseLayerPane.prefWidthProperty());
        graphicalRepresentation.heightProperty().bind(baseLayerPane.prefHeightProperty());

        this.assginedPanel = new FlowPane();
        this.assginedPanel.setHgap(10);
        this.assginedPanel.setVgap(10);
        this.assginedPanel.setAlignment(Pos.CENTER_LEFT);
        this.assginedPanel.setPadding(new Insets(10, 10, 35, 10)); // Added extra bottom padding for text overlay clear-space

        this.assginedPanel.prefWidthProperty().bind(baseLayerPane.prefWidthProperty());
        this.assginedPanel.prefHeightProperty().bind(baseLayerPane.prefHeightProperty());

        VBox labelContainer = new VBox(2);
        labelContainer.setPadding(new Insets(5));
        labelContainer.setAlignment(Pos.BOTTOM_LEFT);

        labelContainer.layoutYProperty().bind(baseLayerPane.prefHeightProperty().subtract(35));
        labelContainer.layoutXProperty().setValue(10);

        this.titleLabel = new Text("Pool Initialization...");
        this.titleLabel.setFill(Color.BLACK);
        this.titleLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

        this.statsLabel = new Text("Avg Age: 0.0 | Count: 0/10");
        this.statsLabel.setFill(Color.DARKSLATEGRAY);
        this.statsLabel.setStyle("-fx-font-size: 10px;");

        labelContainer.getChildren().addAll(this.titleLabel, this.statsLabel);

        baseLayerPane.getChildren().add(graphicalRepresentation);
        baseLayerPane.getChildren().add(labelContainer);
        baseLayerPane.getChildren().add(this.assginedPanel);

        basicPane.getChildren().add(baseLayerPane);
    }

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

    private void updateVisualLabels() {
        double avg = currentPeopleInPool == 0 ? 0.0 : (double) totalAgeInPool / currentPeopleInPool;
        String statsText = String.format("Avg Age: %.1f | Count: %d/%d", avg, currentPeopleInPool, maxPeopleInPool);
        this.statsLabel.setText(statsText);
    }

    public boolean tryEnter(Client client) {
        synchronized (lockForChecking) {
            if (currentPeopleInPool >= maxPeopleInPool) {
                return false;
            }
            // metoda sprawdzajaca czy dany objekt jest instatcja dzieczacej klasy rodzina
            boolean isFamily = (client instanceof ClientWithChild);

            if (this.poolType == PoolsEnumeration.children) {
                if (!isFamily) {
                    return false; // Forbid regular customers and VIPs from entering the children's pool
                }
            }

            //  Zakaz osob nie pelnoletnich do basenu olympjskiego
            if (this.poolType == PoolsEnumeration.olympic) {
                if (isFamily || client.getAge() < 18) {
                    return false; // Forbid families completely, as well as anyone under 18
                }
            }

            if (this.poolType == PoolsEnumeration.regular) {
                int nextPeopleCount = currentPeopleInPool + 1;
                int nextTotalAge = totalAgeInPool + client.getAge();
                double projectedAverageAge = (double) nextTotalAge / nextPeopleCount;

                if (projectedAverageAge > 40.0) {
                    return false;
                }
            }
            currentPeopleInPool++;
            totalAgeInPool += client.getAge();

            Platform.runLater(this::updateVisualLabels);
            return true;
        }
    }

    public int getCurrentPeopleCount() {
        synchronized (lockForChecking) {
            return currentPeopleInPool;
        }
    }

    public int getMaxCapacity() {
        return maxPeopleInPool;
    }

    public void leave(Client client) {
        synchronized (lockForChecking) {
            currentPeopleInPool--;
            totalAgeInPool -= client.getAge();
            if (totalAgeInPool < 0) {
                totalAgeInPool = 0;
            }

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