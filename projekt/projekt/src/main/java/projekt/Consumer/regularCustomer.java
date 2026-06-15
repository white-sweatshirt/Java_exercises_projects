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
        // 1. Spawn in the LEFT queue visually (Left Column: 0% to 10%)
        Platform.runLater(() -> {
            this.circleRepresentation = new Circle(constRadius, Color.BLUE);

            // Shifted bounds to the left lane
            double minX = mainPane.getWidth() * 0.0;
            double maxX = mainPane.getWidth() * 0.1;
            double maxY = mainPane.getHeight() * 0.5;

            double safeMinX = minX + constRadius;
            double safeMaxX = maxX - constRadius;

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
            mainPane.getChildren().remove(circleRepresentation);
            goToChosenPool(targetPool.assginedPanel);

            ScaleTransition shrink = new ScaleTransition(Duration.millis(timeItWantsToSpendms), circleRepresentation);
            shrink.setToX(0.0);
            shrink.setToY(0.0);
            shrink.play();
        });

        // 4. Sleep for the duration of the animation
        try {
            Thread.sleep(timeItWantsToSpendms);
        } catch (InterruptedException e) {
            interrupt();
        } finally {
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