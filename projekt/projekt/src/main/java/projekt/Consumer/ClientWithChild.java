package projekt.Consumer;

import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import projekt.pool.Pool;

public class ClientWithChild extends Client {

    private Rectangle childRepresentation;
    private final double childSize = 10.0;

    public ClientWithChild(int timeToSpendMs) {
        this.timeItWantsToSpendms = timeToSpendMs;
    }

    @Override
    public void run() {
        // 1. Spawn in the LEFT queue visually (Left Column: 0% to 10%)
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
            // In the queue, they remain static until entry, but let's clear properties clean
            childRepresentation.setX(spawnX + constRadius);
            childRepresentation.setY(spawnY - (childSize / 2));

            mainPane.getChildren().addAll(circleRepresentation, childRepresentation);
        });

        Pool chosenPool = null;

        // 2. Wait logically for a spot in a pool
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

        // 3. Move from queue to pool visually with dynamic property bindings
        Platform.runLater(() -> {
            mainPane.getChildren().removeAll(circleRepresentation, childRepresentation);

            // Binds circleRepresentation.centerXProperty and centerYProperty dynamically to pool bounds
            goToChosenPool(targetPool.assginedPanel);

            // COMPLIANCE FIX: Dynamically bind the child to move with the parent inside the pool pane
            childRepresentation.xProperty().bind(circleRepresentation.centerXProperty().add(constRadius));
            childRepresentation.yProperty().bind(circleRepresentation.centerYProperty().subtract(childSize / 2));

            targetPool.assginedPanel.getChildren().add(childRepresentation);

            // Setup parallel transitions
            ScaleTransition shrinkParent = new ScaleTransition(Duration.millis(timeItWantsToSpendms), circleRepresentation);
            shrinkParent.setToX(0.0);
            shrinkParent.setToY(0.0);

            ScaleTransition shrinkChild = new ScaleTransition(Duration.millis(timeItWantsToSpendms), childRepresentation);
            shrinkChild.setToX(0.0);
            shrinkChild.setToY(0.0);

            ParallelTransition parallelTransition = new ParallelTransition(shrinkParent, shrinkChild);
            parallelTransition.play();
        });

        // 4. Pool session duration
        try {
            Thread.sleep(timeItWantsToSpendms);
        } catch (InterruptedException e) {
            interrupt();
        } finally {
            targetPool.leave();

            Platform.runLater(() -> {
                // Unbind on exit to avoid memory leaks when shapes are disposed
                childRepresentation.xProperty().unbind();
                childRepresentation.yProperty().unbind();

                getOut(targetPool.assginedPanel);
                targetPool.assginedPanel.getChildren().remove(childRepresentation);
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