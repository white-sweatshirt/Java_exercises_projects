package projekt.Consumer;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import projekt.pool.Pool;
import projekt.utility.PoolCleaner;
import projekt.utility.SetUp;

public class regularCustomer extends Client {

    public regularCustomer(int timeToSpendMs) {
        super();
        this.timeItWantsToSpendms = timeToSpendMs;
    }

    @Override
    public void run() {
        Platform.runLater(() -> {
            buildLayoutWrapper(Color.BLUE);
            SetUp.normalQueueBox.getChildren().add(componentLayoutWrapper);
        });

        Pool chosenPool = null;

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
        // runLater pozwala na opzninie wykonia animacji chodzi o oszczednosc watku application JavyFX
        Platform.runLater(() -> {
            // deleting from graphics
            SetUp.normalQueueBox.getChildren().remove(componentLayoutWrapper);
            goToChosenPool(targetPool.assginedPanel);
            ScaleTransition shrink = new ScaleTransition(Duration.millis(timeItWantsToSpendms),
                    componentLayoutWrapper);
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