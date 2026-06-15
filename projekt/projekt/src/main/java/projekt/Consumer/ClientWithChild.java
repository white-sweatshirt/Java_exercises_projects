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

public class ClientWithChild extends Client {

    private Rectangle childRepresentation;
    private final double childSize = 10.0;
    private HBox familyGroup;

    public ClientWithChild(int timeToSpendMs) {
        super();
        this.timeItWantsToSpendms = timeToSpendMs;
    }

    @Override
    public void run() {
        Platform.runLater(() -> {
            buildLayoutWrapper(Color.DARKGREEN);

            double minX = mainPane.getWidth() * 0.0;
            double maxX = mainPane.getWidth() * 0.1;
            double maxY = mainPane.getHeight() * 0.5;

            double spawnX = (minX + constRadius) + Math.random() * ((maxX - constRadius) - (minX + constRadius));
            double spawnY = constRadius + Math.random() * (maxY - constRadius * 2);

            componentLayoutWrapper.setLayoutX(spawnX);
            componentLayoutWrapper.setLayoutY(spawnY);

            this.childRepresentation = new Rectangle(childSize, childSize, Color.LIGHTGREEN);
            VBox childBox = new VBox(2);
            childBox.setAlignment(Pos.CENTER);
            Text childText = new Text("Ch");
            childText.setStyle("-fx-font-size: 8px;");
            childBox.getChildren().addAll(childRepresentation, childText);

            familyGroup = new HBox(4);
            familyGroup.getChildren().addAll(componentLayoutWrapper, childBox);
            familyGroup.setLayoutX(spawnX);
            familyGroup.setLayoutY(spawnY);

            mainPane.getChildren().add(familyGroup);
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
            mainPane.getChildren().remove(familyGroup);

            // Re-wrap cleanly for the target pool FlowPane layer
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