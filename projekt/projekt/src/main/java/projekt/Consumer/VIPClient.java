package projekt.Consumer;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import projekt.pool.Pool;
import projekt.utility.PoolCleaner;
import projekt.utility.SetUp;

public class VIPClient extends Client {

    public VIPClient(int timeToSpendMs) {
        super();
        this.timeItWantsToSpendms = timeToSpendMs;
    }

    @Override
    public void run() {
        Platform.runLater(() -> {
            buildLayoutWrapper(Color.GOLD);
            circleRepresentation.setStroke(Color.BLACK);
            SetUp.vipQueueBox.getChildren().add(componentLayoutWrapper);
        });

        Pool chosenPool = null;

        queLock.lock();
        try {
            vipsInQue++;

            while (PoolCleaner.isCleaningInProgress() || (chosenPool = claimFreePool()) == null) {
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
            SetUp.vipQueueBox.getChildren().remove(componentLayoutWrapper);
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