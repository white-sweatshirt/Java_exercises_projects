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
        this.circleRepresentation = new Circle(constRadius, Color.BLUE);
    }
    // Replaces getFreePool()
    protected Pool claimFreePool() {
        for (Pool pool : allPools) {
            if (pool.tryEnter()) {
                return pool; // Spot is successfully claimed
            }
        }
        return null;
    }
    @Override
    public void run() {
        Pool chosenPool = null;

        queLock.lock();
        try {
            // Claiming the pool is now atomic and part of the condition check
            while (vipsInQue > 0 || (chosenPool = claimFreePool()) == null) {
                normalPersonCanPass.await();
            }
            // NO NEED for chosenPool.enter() here anymore!
        } catch (InterruptedException e) {
            interrupt();
            return;
        } finally {
            queLock.unlock();
        }

        // 1. Enter the pool graphically (Must be on JavaFX Application Thread)
        final Pool targetPool = chosenPool;
        //
        Platform.runLater(() -> {
            // Instantiate the UI element ON the JavaFX thread
            this.circleRepresentation = new Circle(constRadius, Color.BLUE);

            goToChosenPool(targetPool.assginedPanel);

            ScaleTransition shrink = new ScaleTransition(Duration.millis(timeItWantsToSpendms), circleRepresentation);
            shrink.setToX(0.0);
            shrink.setToY(0.0);
            shrink.play();
        });

        // 3. Sleep for the duration of the animation on the backend thread
        try {
            Thread.sleep(timeItWantsToSpendms);
        } catch (InterruptedException e) {
            interrupt();
        }

        // 4. Leave the pool logically
        targetPool.leave();

        // 5. Leave the pool graphically
        Platform.runLater(() -> getOut(targetPool.assginedPanel));

        // 6. Signal the queue that a spot has opened up
        queLock.lock();
        try {
            // Signal VIPs first. If no VIPs, signal regular customers.
            vipCanPass.signalAll();
            normalPersonCanPass.signalAll();
        } finally {
            queLock.unlock();
        }
    }
}