package projekt.Consumer;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import projekt.pool.Pool;

public class VIPClient extends Client {

    public VIPClient(int timeToSpendMs) {
        this.timeItWantsToSpendms = timeToSpendMs;
    }

    @Override
    public void run() {
        // 1. Spawn in the queue visually as GOLD
        Platform.runLater(() -> {
            this.circleRepresentation = new Circle(constRadius, Color.GOLD);
            circleRepresentation.setStroke(Color.BLACK);

            double minX = mainPane.getWidth() * 0.1;
            double maxX = mainPane.getWidth() * 0.2;
            double maxY = mainPane.getHeight() * 0.5;

            circleRepresentation.setCenterX((minX + constRadius) + Math.random() * ((maxX - constRadius) - (minX + constRadius)));
            circleRepresentation.setCenterY(constRadius + Math.random() * (maxY - constRadius * 2));

            mainPane.getChildren().add(circleRepresentation);
        });

        Pool chosenPool = null;

        // 2. Lock and register as a waiting VIP
        queLock.lock();
        try {
            vipsInQue++; // Alert regular customers that a VIP is waiting

            // VIPs only wait if all pools are completely full
            while ((chosenPool = claimFreePool()) == null) {
                vipCanPass.await();
            }

            vipsInQue--; // Successfully leaving the queue to enter a pool
        } catch (InterruptedException e) {
            interrupt();
            return;
        } finally {
            queLock.unlock();
        }

        final Pool targetPool = chosenPool;

        // 3. Move from queue to pool visually
        Platform.runLater(() -> {
            mainPane.getChildren().remove(circleRepresentation);
            goToChosenPool(targetPool.assginedPanel);

            ScaleTransition shrink = new ScaleTransition(Duration.millis(timeItWantsToSpendms), circleRepresentation);
            shrink.setToX(0.0);
            shrink.setToY(0.0);
            shrink.play();
        });

        // 4. Enjoy the pool
        try {
            Thread.sleep(timeItWantsToSpendms);
        } catch (InterruptedException e) {
            interrupt();
        } finally {
            // Safe exit tracking
            targetPool.leave();
            Platform.runLater(() -> getOut(targetPool.assginedPanel));

            queLock.lock();
            try {
                vipCanPass.signalAll();
                normalPersonCanPass.signalAll();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                queLock.unlock();
            }
        }
    }
}