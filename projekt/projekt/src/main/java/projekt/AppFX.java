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
import projekt.utility.ConfigReader;
import projekt.utility.PoolCleaner;
import projekt.utility.SetUp;
import projekt.pool.Pool;
import projekt.Consumer.Client;
import projekt.Consumer.regularCustomer;
import projekt.Consumer.ClientWithChild;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AppFX extends Application {
    // Pace of client sessions inside the pools
    final int timeInPoolsMs = 3000;

    // paramtrs of simulation set by user volatile is for safe updates
    private volatile int maxNonVipsLimit;
    private volatile int maxVipsLimit;
    private volatile double regularCustomerProportion = 0.75;

    // Lissts for managing threads
    private final List<Client> activeVIPs = new CopyOnWriteArrayList<>();
    private final List<Client> activeNonVIPs = new CopyOnWriteArrayList<>();

    private int lastNonVipCount = 0;
    private int lastVipCount = 0;

    @Override
    public void start(Stage stage) {
        ConfigReader config = new ConfigReader();
        this.maxNonVipsLimit = config.getMaxNonVips();
        this.maxVipsLimit = config.getMaxVips();

        Pane pane = new Pane();
        Scene firstScene = new Scene(pane, 1150, 800);
        stage.setScene(firstScene);
        firstScene.setFill(Color.WHITE);
        stage.setTitle("Projekt Franciszek Wawer - Complete Concurrent Pool System");

        Pool[] pools = SetUp.producePoolsRepresentations(pane);
        SetUp.produceBackground(pane);
        Client.setPools(pools);
        Client.setMainPane(pane);
        Cashier.setPools(pools);
        Cashier cashier = SetUp.createQue(pane);
         // interface is down here mabe i should put it into separate class...
        VBox controlPanel = new VBox(14);
        controlPanel.setPadding(new Insets(15));
        controlPanel.setStyle("-fx-background-color: #f4f4f6; -fx-border-color: #d1d1d6; -fx-border-width: 0 1px 0 0;");
        controlPanel.prefHeightProperty().bind(pane.heightProperty());
        controlPanel.layoutXProperty().setValue(0);
        controlPanel.layoutYProperty().setValue(0);
        controlPanel.prefWidthProperty().bind(pane.widthProperty().multiply(0.12));

        Label controlTitle = new Label("LIMIT SETTINGS");
        controlTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #3a3a3c;");

        Label lblNonVipCap = new Label("Max Limit Non-VIP: " + maxNonVipsLimit);
        // slider skonstruowany przez klase
        Slider sliderNonVip = new Slider(0, 44, maxNonVipsLimit);
        sliderNonVip.setBlockIncrement(1);
        // add Listener powiadamia ze wartosc na pasku sie zmienila
        sliderNonVip.valueProperty().addListener((obs, oldVal, newVal) -> {
            maxNonVipsLimit = newVal.intValue();
            lblNonVipCap.setText("Max Limit Non-VIP: " + maxNonVipsLimit);
        });

        Label lblVipCap = new Label("Max Limit VIP: " + maxVipsLimit);
        Slider sliderVip = new Slider(0, 22, maxVipsLimit);
        sliderVip.setBlockIncrement(1);
        sliderVip.valueProperty().addListener((obs, oldVal, newVal) -> {
            maxVipsLimit = newVal.intValue();
            lblVipCap.setText("Max Limit VIP: " + maxVipsLimit);
        });

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

        SetUp.normalCountLabel.setText("Queue: 0 / " + maxNonVipsLimit);
        SetUp.vipCountLabel.setText("Queue: 0 / " + maxVipsLimit);

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
                        if (activeVIPs.size() < maxVipsLimit) {
                            customer = new VIPClient(timeInPoolsMs);
                            activeVIPs.add(customer);
                        }
                    } else {
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