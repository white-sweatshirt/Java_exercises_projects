package projekt.utility;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import projekt.Consumer.Client;
import projekt.pool.Pool;

public class PoolCleaner implements Runnable {

    private final long TIME_BETWEEN_CLEANINGS = 5000;
    private static volatile boolean isCleaningPhase = false;
    // vbox jest po to aby pod kolem byl tekst
    private VBox componentLayoutWrapper;
    private Circle cleanerCircle;
    private FadeTransition blinkAnimation;

    public PoolCleaner(Pane rootPane) {
        Platform.runLater(() -> {
            this.componentLayoutWrapper = new VBox(4);
            this.componentLayoutWrapper.setAlignment(Pos.CENTER);
            this.componentLayoutWrapper.setVisible(false);

            this.cleanerCircle = new Circle(15, Color.RED);
            this.cleanerCircle.setStroke(Color.BLACK);
            this.cleanerCircle.setStrokeWidth(2);

            Text etiquetteLabel = new Text("Cleaner");
            etiquetteLabel.setFill(Color.BLACK);
            etiquetteLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");

            this.componentLayoutWrapper.getChildren().addAll(cleanerCircle, etiquetteLabel);

            this.componentLayoutWrapper.layoutXProperty().bind(rootPane.widthProperty().multiply(0.65));
            this.componentLayoutWrapper.layoutYProperty().bind(rootPane.heightProperty().multiply(0.40));

            this.blinkAnimation = new FadeTransition(Duration.millis(500), componentLayoutWrapper);
            this.blinkAnimation.setFromValue(1.0);
            this.blinkAnimation.setToValue(0.3);
            this.blinkAnimation.setCycleCount(FadeTransition.INDEFINITE);
            this.blinkAnimation.setAutoReverse(true);

            rootPane.getChildren().add(componentLayoutWrapper);
        });
    }

    public static boolean isCleaningInProgress() {
        return isCleaningPhase;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(TIME_BETWEEN_CLEANINGS);
                Client.queLock.lock();
                try {
                    isCleaningPhase = true; // zmienna zakazujaca wstepu nowym watkom
                    Platform.runLater(() -> {
                        if (componentLayoutWrapper != null) {
                            componentLayoutWrapper.setVisible(true);
                            blinkAnimation.play();
                        }
                    });
                } finally {
                    Client.queLock.unlock();
                }

                // PHASE 2: Passive wait loop (No locks held, letting pools naturally drain)
                while (!areAllPoolsEmpty()) {
                    Thread.sleep(200);
                }

                Client.queLock.lock();
                try {
                    Platform.runLater(() -> cleanerCircle.setFill(Color.DARKRED));
                    long cleaningDuration = 4000;
                    Thread.sleep(cleaningDuration);
                } finally {
                    isCleaningPhase = false;
                    Platform.runLater(() -> {
                        if (componentLayoutWrapper != null) {
                            blinkAnimation.stop();
                            componentLayoutWrapper.setOpacity(1.0);
                            cleanerCircle.setFill(Color.RED);
                            componentLayoutWrapper.setVisible(false);
                        }
                    });

                    Client.normalPersonCanPass.signalAll();
                    Client.vipCanPass.signalAll();
                    Client.queLock.unlock();
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private boolean areAllPoolsEmpty() {
        if (Client.getAllPools() == null) return true;
        for (Pool pool : Client.getAllPools()) {
            if (pool.getCurrentPeopleCount() > 0) return false;
        }
        return true;
    }
}