package projekt.Consumer;

import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import projekt.pool.Pool;

public class ClientWithChild extends Client {

    private Rectangle childRepresentation;
    private final double childSize = 10.0;
    private HBox familyGroup; // Container to hold parent and child together in the FlowPane

    public ClientWithChild(int timeToSpendMs) {
        this.timeItWantsToSpendms = timeToSpendMs;
    }

    @Override
    public void run() {
        Platform.runLater(() -> {
            this.circleRepresentation = new Circle(constRadius, Color.DARKGREEN);
            double minX = mainPane.getWidth() * 0.0;
            double maxX = mainPane.getWidth() * 0.1;
            double maxY = mainPane.getHeight() * 0.5;
            double safeMinX = minX + constRadius;
            double safeMaxX = maxX - constRadius;
            double spawnX = safeMinX + Math.random() * (safeMaxX - safeMinX);
            double spawnY = constRadius + Math.random() * (maxY - constRadius * 2);

            circleRepresentation.setCenterX(spawnX);
            circleRepresentation.setCenterY(spawnY);

            this.childRepresentation = new Rectangle(childSize, childSize, Color.LIGHTGREEN);
            childRepresentation.setX(spawnX + constRadius);
            childRepresentation.setY(spawnY - (childSize / 2));

            mainPane.getChildren().addAll(circleRepresentation, childRepresentation);
        });

        Pool chosenPool = null;

        // 2. Wait logically for a spot
        queLock.lock();
        try {
            while (vipsInQue > 0 || (chosenPool = claimFreePool()) == null) {
                normalPersonCanPass.await();
            }
        } catch (InterruptedException e) {
            interrupt();
            return;
        } finally {
            queLock.unlock();
        }

        final Pool targetPool = chosenPool;

        // 3. Move to FlowPane layout safely wrapped in an HBox
        Platform.runLater(() -> {
            mainPane.getChildren().removeAll(circleRepresentation, childRepresentation);

            // Create a small combined container for the parent and child
            familyGroup = new HBox(4); // 4px spacing between them
            familyGroup.setAlignment(Pos.CENTER_LEFT);

            // Put shapes in the family container
            familyGroup.getChildren().addAll(circleRepresentation, childRepresentation);

            // Append the group to the pool's FlowPane
            targetPool.assginedPanel.getChildren().add(familyGroup);

            // Scale transitions work perfectly on the inner elements
            ScaleTransition shrinkParent = new ScaleTransition(Duration.millis(timeItWantsToSpendms), circleRepresentation);
            shrinkParent.setToX(0.0);
            shrinkParent.setToY(0.0);

            ScaleTransition shrinkChild = new ScaleTransition(Duration.millis(timeItWantsToSpendms), childRepresentation);
            shrinkChild.setToX(0.0);
            shrinkChild.setToY(0.0);

            ParallelTransition parallelTransition = new ParallelTransition(shrinkParent, shrinkChild);
            parallelTransition.play();
        });

        // 4. Stay duration
        try {
            Thread.sleep(timeItWantsToSpendms);
        } catch (InterruptedException e) {
            interrupt();
        } finally {
            targetPool.leave(this);

            Platform.runLater(() -> {
                // Simply remove the single container from the pool panel
                if (familyGroup != null) {
                    targetPool.assginedPanel.getChildren().remove(familyGroup);
                }
            });

            queLock.lock();
            try {
                vipCanPass.signalAll();
                normalPersonCanPass.signalAll();
            } finally {
                queLock.unlock();
            }
        }
    }
}