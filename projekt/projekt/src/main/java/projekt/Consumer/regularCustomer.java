package projekt.Consumer;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import projekt.pool.Pool;

public class regularCustomer extends Client {

    public regularCustomer(int timeToSpendMs) {
        this.timeItWantsToSpendms = timeToSpendMs;
    }

    @Override
    public void run() {
        // 1. Spawn in the queue visually BEFORE waiting for a pool
        Platform.runLater(() -> {
            this.circleRepresentation = new Circle(constRadius, Color.BLUE);

            // Calculate bounds based on your SetUp.addLines logic
            // X is between 10% and 20% of the pane width
            // Y is between 0 and 50% of the pane height
            double minX = mainPane.getWidth() * 0.1;
            double maxX = mainPane.getWidth() * 0.2;
            double maxY = mainPane.getHeight() * 0.5;

            // Keep the circle safely inside the lines by accounting for its radius
            double safeMinX = minX + constRadius;
            double safeMaxX = maxX - constRadius;

            // Randomize position within the queue bounds
            circleRepresentation.setCenterX(safeMinX + Math.random() * (safeMaxX - safeMinX));
            circleRepresentation.setCenterY(constRadius + Math.random() * (maxY - constRadius * 2));

            mainPane.getChildren().add(circleRepresentation);
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

        // 3. Move from the queue to the pool visually
        Platform.runLater(() -> {
            // Remove from the main queue pane
            mainPane.getChildren().remove(circleRepresentation);

            // Add to the specific pool pane
            goToChosenPool(targetPool.assginedPanel);

            // Play shrinking animation
            ScaleTransition shrink = new ScaleTransition(Duration.millis(timeItWantsToSpendms), circleRepresentation);
            shrink.setToX(0.0);
            shrink.setToY(0.0);
            shrink.play();
        });

        // 4. Sleep for the duration of the animation on the backend thread
        try {
            Thread.sleep(timeItWantsToSpendms);
        } catch (InterruptedException e) {
            interrupt();
        } finally {
            // Guarantee that the spot is freed and the client is removed visually
            targetPool.leave();
            Platform.runLater(() -> getOut(targetPool.assginedPanel));

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