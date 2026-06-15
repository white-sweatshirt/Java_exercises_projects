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

    // Extra graphic element specifically for the child
    private Rectangle childRepresentation;
    private final double childSize = 10.0;

    public ClientWithChild(int timeToSpendMs) {
        this.timeItWantsToSpendms = timeToSpendMs;
    }

    @Override
    public void run() {
        // 1. Spawn both parent (Circle) and child (Rectangle) in the queue visually
        Platform.runLater(() -> {
            // Parent setup (Green to differentiate from regular customers, or any color you prefer)
            this.circleRepresentation = new Circle(constRadius, Color.DARKGREEN);

            // Queue boundary logic mirroring regularCustomer
            double minX = mainPane.getWidth() * 0.1;
            double maxX = mainPane.getWidth() * 0.2;
            double maxY = mainPane.getHeight() * 0.5;

            double safeMinX = minX + constRadius;
            double safeMaxX = maxX - constRadius;

            double spawnX = safeMinX + Math.random() * (safeMaxX - safeMinX);
            double spawnY = constRadius + Math.random() * (maxY - constRadius * 2);

            circleRepresentation.setCenterX(spawnX);
            circleRepresentation.setCenterY(spawnY);

            // Child setup (Smaller square, positioned right next to the parent circle)
            this.childRepresentation = new Rectangle(childSize, childSize, Color.LIGHTGREEN);
            // Offset slightly so they don't render exactly on top of each other
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

        // 3. Move from the queue to the pool visually and shrink both shapes
        Platform.runLater(() -> {
            // Remove both from the main queue pane
            mainPane.getChildren().removeAll(circleRepresentation, childRepresentation);

            // Move parent into the pool pane randomly
            goToChosenPool(targetPool.assginedPanel);

            // Position child relative to the parent's new pool position
            childRepresentation.setX(circleRepresentation.getCenterX() + constRadius);
            childRepresentation.setY(circleRepresentation.getCenterY() - (childSize / 2));
            targetPool.assginedPanel.getChildren().add(childRepresentation);

            // Animation for Parent
            ScaleTransition shrinkParent = new ScaleTransition(Duration.millis(timeItWantsToSpendms), circleRepresentation);
            shrinkParent.setToX(0.0);
            shrinkParent.setToY(0.0);

            // Animation for Child
            ScaleTransition shrinkChild = new ScaleTransition(Duration.millis(timeItWantsToSpendms), childRepresentation);
            shrinkChild.setToX(0.0);
            shrinkChild.setToY(0.0);

            // Play both animations at the exact same time
            ParallelTransition parallelTransition = new ParallelTransition(shrinkParent, shrinkChild);
            parallelTransition.play();
        });

        // 4. Sleep for the duration of the pool visit on the backend thread
        try {
            Thread.sleep(timeItWantsToSpendms);
        } catch (InterruptedException e) {
            interrupt();
        } finally {
            // Guarantee that the spot is freed and both graphics are cleaned up
            targetPool.leave();

            Platform.runLater(() -> {
                getOut(targetPool.assginedPanel); // Removes parent
                targetPool.assginedPanel.getChildren().remove(childRepresentation); // Removes child
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