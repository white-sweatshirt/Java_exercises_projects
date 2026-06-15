package projekt;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import projekt.Consumer.VIPClient;
import projekt.utility.Cashier;
import projekt.utility.PoolCleaner;
import projekt.utility.SetUp;
import projekt.pool.Pool;
import projekt.Consumer.Client;
import projekt.Consumer.regularCustomer;
import projekt.Consumer.ClientWithChild;

import java.util.ArrayList;
import java.util.List;

public class AppFX extends Application {
    final int timeInPoolsMs = 3000;

    // --- Dynamic Slider Values (Volatile variables to ensure thread safety across engines) ---
    private volatile int maxNonVipsLimit = 44;
    private volatile int maxVipsLimit = 22;
    private volatile double regularCustomerProportion = 0.75; // Out of remaining non-VIP percentage distribution

    // --- Collections to track active client threads ---
    private final List<Client> activeVIPs = new ArrayList<>();
    private final List<Client> activeNonVIPs = new ArrayList<>();

    // --- Tracking variables for the animation pop triggers ---
    private int lastNonVipCount = 0;
    private int lastVipCount = 0;

    @Override
    public void start(Stage stage) {

        Pane pane = new Pane();
        Scene firstScene = new Scene(pane, 1150, 800); // Expanded stage slightly to comfortably hold sliders box
        stage.setScene(firstScene);
        firstScene.setFill(Color.WHITE);
        stage.setTitle("Projekt Franciszek Wawer - Interactive Sliders Configurator");

        Pool[] pools = SetUp.producePoolsRepresentations(pane);
        SetUp.produceBackground(pane);
        Client.setPools(pools);
        Client.setMainPane(pane);
        Cashier.setPools(pools);
        Cashier cashier = SetUp.createQue(pane);

        // =========================================================================
        // STEP 1: CONSTRUCT THE USER CONFIGURATION DASHBOARD GRAPHICS
        // =========================================================================
        VBox controlPanel = new VBox(14);
        controlPanel.setPadding(new Insets(15));
        controlPanel.setStyle("-fx-background-color: #f4f4f6; -fx-border-color: #d1d1d6; -fx-border-width: 0 1px 0 0;");
        controlPanel.prefHeightProperty().bind(pane.heightProperty());
        controlPanel.layoutXProperty().setValue(0);
        controlPanel.layoutYProperty().setValue(0);
        controlPanel.prefWidthProperty().bind(pane.widthProperty().multiply(0.12));

        Label controlTitle = new Label("SIM SETTINGS");
        controlTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #3a3a3c;");

        // Slider A: Max Non-VIPs Capacity (0 to 44)
        Label lblNonVipCap = new Label("Max Non-VIP: 44");
        Slider sliderNonVip = new Slider(0, 44, 44);
        sliderNonVip.setBlockIncrement(1);
        sliderNonVip.valueProperty().addListener((obs, oldVal, newVal) -> {
            maxNonVipsLimit = newVal.intValue();
            lblNonVipCap.setText("Max Non-VIP: " + maxNonVipsLimit);
        });

        // Slider B: Max VIPs Capacity (0 to 22)
        Label lblVipCap = new Label("Max VIP: 22");
        Slider sliderVip = new Slider(0, 22, 22);
        sliderVip.setBlockIncrement(1);
        sliderVip.valueProperty().addListener((obs, oldVal, newVal) -> {
            maxVipsLimit = newVal.intValue();
            lblVipCap.setText("Max VIP: " + maxVipsLimit);
        });

        // Slider C: Regular Customer Proportion Selection (0% to 100%)
        Label lblRegularProp = new Label("Regular Cust: 75%");
        Label lblFamilyProp = new Label("Family Unit: 25%");
        Slider sliderProportion = new Slider(0.0, 1.0, 0.75);
        sliderProportion.valueProperty().addListener((obs, oldVal, newVal) -> {
            regularCustomerProportion = newVal.doubleValue();
            int regPercent = (int) (regularCustomerProportion * 100);
            int famPercent = 100 - regPercent;
            lblRegularProp.setText("Regular Cust: " + regPercent + "%");
            lblFamilyProp.setText("Family Unit: " + famPercent + "%");
        });

        controlPanel.getChildren().addAll(
                controlTitle,
                lblNonVipCap, sliderNonVip,
                lblVipCap, sliderVip,
                lblRegularProp, sliderProportion,
                lblFamilyProp
        );
        pane.getChildren().add(controlPanel);
        stage.show();

        // =========================================================================
        // THREAD 1: SYSTEM WORKER / ACCELERATED GENERATOR LOOP
        // =========================================================================
        Thread uiMetricsMonitorThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(20);

                    int currentNonVip = activeNonVIPs.size();
                    int currentVip = activeVIPs.size();

                    if (currentNonVip != lastNonVipCount || currentVip != lastVipCount) {
                        final boolean animateNonVip = (currentNonVip != lastNonVipCount);
                        final boolean animateVip = (currentVip != lastVipCount);

                        lastNonVipCount = currentNonVip;
                        lastVipCount = currentVip;

                        Platform.runLater(() -> {
                            // Max dynamic caps updated on display label scoreboards in real-time
                            if (animateNonVip) {
                                SetUp.normalCountLabel.setText("Queue: " + currentNonVip + " / " + maxNonVipsLimit);
                                animatePopEffect(SetUp.normalCountLabel);
                            }
                            if (animateVip) {
                                SetUp.vipCountLabel.setText("Queue: " + currentVip + " / " + maxVipsLimit);
                                animatePopEffect(SetUp.vipCountLabel);
                            }
                        });
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        uiMetricsMonitorThread.setDaemon(true);

        // =========================================================================
        // THREAD 2: DEDICATED VISUAL METRICS REFRESH ENGINE
        // =========================================================================
        Thread backendThread = new Thread(() -> {
            for (Pool p : pools) {
                p.start();
            }
            cashier.start();
            uiMetricsMonitorThread.start();

            try {
                PoolCleaner cleanerTask = new PoolCleaner(pane);
                Thread cleanerThread = new Thread(cleanerTask);
                cleanerThread.start();

                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(20);

                    activeVIPs.removeIf(client -> !client.isAlive());
                    activeNonVIPs.removeIf(client -> !client.isAlive());

                    double randomValue = Math.random();
                    Client customer = null;

                    if (randomValue < 0.20) {
                        // VIP Spawn using live values from the Slider
                        if (activeVIPs.size() < maxVipsLimit) {
                            customer = new VIPClient(timeInPoolsMs);
                            activeVIPs.add(customer);
                        }
                    } else {
                        // Non-VIP distribution split based on user-controlled proportions
                        if (activeNonVIPs.size() < maxNonVipsLimit) {
                            double strategyRoll = Math.random();
                            if (strategyRoll < regularCustomerProportion) {
                                customer = new regularCustomer(timeInPoolsMs);
                            } else {
                                customer = new ClientWithChild(timeInPoolsMs);
                            }
                            activeNonVIPs.add(customer);
                        }
                    }

                    if (customer != null) {
                        customer.start();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        backendThread.start();
    }

    private void animatePopEffect(Text textNode) {
        ScaleTransition pop = new ScaleTransition(Duration.millis(70), textNode);
        pop.setFromX(1.0);
        pop.setFromY(1.0);
        pop.setToX(1.20);
        pop.setToY(1.20);
        pop.setAutoReverse(true);
        pop.setCycleCount(2);
        pop.play();
    }
}