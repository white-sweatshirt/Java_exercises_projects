package projekt.Consumer;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import projekt.pool.Pool;
import projekt.utility.PoolCleaner;
import projekt.utility.SetUp;

public class ClientWithChild extends Client {

    private Rectangle childRepresentation;
    private final double childSize = 10.0;
    private HBox familyGroup;
    private final int childAge;

    public ClientWithChild(int timeToSpendMs) {
        super();
        this.timeItWantsToSpendms = timeToSpendMs;
        this.childAge = 1 + (int) (Math.random() * 12);
    }

    public int getChildAge() {
        return this.childAge;
    }

    @Override
    public void run() {
        Platform.runLater(() -> {
            buildLayoutWrapper(Color.DARKGREEN);

            this.childRepresentation = new Rectangle(childSize, childSize, Color.LIGHTGREEN);

            VBox childBox = new VBox(2);
            childBox.setAlignment(Pos.CENTER);

            Text childText = new Text("Ch: " + this.childAge);
            childText.setFill(Color.BLACK);
            childText.setStyle("-fx-font-size: 8px; -fx-font-weight: bold;");

            childBox.getChildren().addAll(childRepresentation, childText);

            familyGroup = new HBox(4);
            familyGroup.setAlignment(Pos.BOTTOM_CENTER);
            familyGroup.getChildren().addAll(componentLayoutWrapper, childBox);

            // Family layout unit enters the shared normal vertical FlowPane line
            SetUp.normalQueueBox.getChildren().add(familyGroup);
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

        Platform.runLater(() -> {
            SetUp.normalQueueBox.getChildren().remove(familyGroup);
            targetPool.assginedPanel.getChildren().add(familyGroup);

            ScaleTransition shrink = new ScaleTransition(Duration.millis(timeItWantsToSpendms), familyGroup);
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

            Platform.runLater(() -> {
                if (familyGroup != null) {
                    targetPool.assginedPanel.getChildren().remove(familyGroup);
                }
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