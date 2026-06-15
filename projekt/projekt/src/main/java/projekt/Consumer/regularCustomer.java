package projekt.Consumer;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import projekt.pool.Pool;
import projekt.utility.PoolCleaner;

public class regularCustomer extends Client {

    public regularCustomer(int timeToSpendMs) {
        super();
        this.timeItWantsToSpendms = timeToSpendMs;
    }

    @Override
    public void run() {
        Platform.runLater(() -> {
            buildLayoutWrapper(Color.BLUE);

            double minX = mainPane.getWidth() * 0.0;
            double maxX = mainPane.getWidth() * 0.1;
            double maxY = mainPane.getHeight() * 0.5;

            componentLayoutWrapper.setLayoutX((minX + constRadius) + Math.random() * ((maxX - constRadius) - (minX + constRadius)));
            componentLayoutWrapper.setLayoutY(constRadius + Math.random() * (maxY - constRadius * 2));

            mainPane.getChildren().add(componentLayoutWrapper);
        });

        Pool chosenPool = null;

        // Subclass explicitly evaluates all local condition rules
        queLock.lock();
        try {
            while (PoolCleaner.isCleaningInProgress() || vipsInQue > 0 || (chosenPool = claimFreePool()) == null) {
                normalPersonCanPass.await();
            }
        } catch (InterruptedException e) {
            interrupt();
            return;
        } finally {
            queLock.unlock();
        }

        final Pool targetPool = chosenPool;

        Platform.runLater(() -> {
            mainPane.getChildren().remove(componentLayoutWrapper);
            goToChosenPool(targetPool.assginedPanel);

            ScaleTransition shrink = new ScaleTransition(Duration.millis(timeItWantsToSpendms), componentLayoutWrapper);
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
            } finally {
                queLock.unlock();
            }
        }
    }
}