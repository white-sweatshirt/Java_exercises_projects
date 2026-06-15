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
        // queLock for having unambigous numbers defing rihght of entry
        queLock.lock();
        try {
            vipsInQue++; // forcing non vip consumers to wait for their turn
            // VIPs can only wait  if and only if all pools are completely full
            while ((chosenPool = claimFreePool()) == null) {
                vipCanPass.await();
            }
            vipsInQue--;
        } catch (InterruptedException e) {
            interrupt();
            return;
        } finally {
            queLock.unlock();
        }

        final Pool targetPool = chosenPool;

        Platform.runLater(() -> {
            mainPane.getChildren().remove(circleRepresentation);
            goToChosenPool(targetPool.assginedPanel);

            ScaleTransition shrink = new ScaleTransition(Duration.millis(timeItWantsToSpendms), circleRepresentation);
            shrink.setToX(0.0);
            shrink.setToY(0.0);
            shrink.play();
        });

        try {
            Thread.sleep(timeItWantsToSpendms);
        } catch (InterruptedException e) {
            interrupt();
        } finally {
            targetPool.leave(this);
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