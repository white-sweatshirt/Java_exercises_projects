package projekt;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
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

    // --- Capacity Limits ---
    private final int MAX_NON_VIPS = 44;
    private final int MAX_VIPS = 22;

    // --- Collections to track active client threads ---
    private final List<Client> activeVIPs = new ArrayList<>();
    private final List<Client> activeNonVIPs = new ArrayList<>();

    // --- Variables to track previous states to trigger interactive pop effects ---
    private int lastNonVipCount = 0;
    private int lastVipCount = 0;

    @Override
    public void start(Stage stage) {

        Pane pane = new Pane();
        Scene firstScene = new Scene(pane, 1000, 800);
        stage.setScene(firstScene);
        firstScene.setFill(Color.WHITE);
        stage.setTitle("Projekt Franciszek Wawer - Isolated Metrics Engine");

        Pool[] pools = SetUp.producePoolsRepresentations(pane);
        SetUp.produceBackground(pane);
        Client.setPools(pools);
        Client.setMainPane(pane);
        Cashier.setPools(pools);
        Cashier cashier = SetUp.createQue(pane);
        stage.show();

        // =========================================================================
        // THREAD 1: DEDICATED VISUAL METRICS MONITOR
        // =========================================================================
        Thread uiMetricsMonitorThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    // Pull stats at a consistent high-frequency 50Hz clock rate
                    Thread.sleep(20);

                    int currentNonVip = activeNonVIPs.size();
                    int currentVip = activeVIPs.size();

                    // Evaluate data state variations on this background thread first
                    if (currentNonVip != lastNonVipCount || currentVip != lastVipCount) {
                        final boolean animateNonVip = (currentNonVip != lastNonVipCount);
                        final boolean animateVip = (currentVip != lastVipCount);

                        lastNonVipCount = currentNonVip;
                        lastVipCount = currentVip;

                        // Safely dispatch execution block to JavaFX Application thread
                        Platform.runLater(() -> {
                            if (animateNonVip) {
                                SetUp.normalCountLabel.setText("Queue: " + currentNonVip + " / " + MAX_NON_VIPS);
                                animatePopEffect(SetUp.normalCountLabel);
                            }
                            if (animateVip) {
                                SetUp.vipCountLabel.setText("Queue: " + currentVip + " / " + MAX_VIPS);
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
                    if (randomValue < 0.07) {
                        if (activeVIPs.size() < MAX_VIPS) {
                            customer = new VIPClient(timeInPoolsMs);
                            activeVIPs.add(customer);
                        }
                    } else if (randomValue < 0.18) {
                        if (activeNonVIPs.size() < MAX_NON_VIPS) {
                            customer = new ClientWithChild(timeInPoolsMs);
                            activeNonVIPs.add(customer);
                        }
                    } else if (randomValue < 0.35) {
                        if (activeNonVIPs.size() < MAX_NON_VIPS) {
                            customer = new regularCustomer(timeInPoolsMs);
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