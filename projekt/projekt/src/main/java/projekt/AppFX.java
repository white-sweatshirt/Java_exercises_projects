package projekt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import projekt.Consumer.VIPClient;
import projekt.utility.Cashier;
import projekt.utility.SetUp;
import projekt.pool.Pool;
import projekt.Consumer.Client;
import projekt.Consumer.regularCustomer;

public class AppFX extends Application {

    @Override
    public void start(Stage stage) {

        Pane pane = new Pane();
        Scene firstScene = new Scene(pane, 1000, 800);
        stage.setScene(firstScene);
        firstScene.setFill(Color.WHITE);
        stage.setTitle("Projekt Franciszek Wawer");

        // Inside start() method of AppFX.java

        Pool[] pools = SetUp.producePoolsRepresentations(pane);
        SetUp.produceBackground(pane);

        // Pass references to the abstract Client class
        // Inside start() method of AppFX.java
        Client.setPools(pools);
        Client.setMainPane(pane);
        Cashier.setPools(pools);

        stage.show();

        // HIGH-LOAD BACKEND GENERATOR
        Thread backendThread = new Thread(() -> {
            // Start pool threads
            for (Pool p : pools) {
                p.start();
            }

            // Start cashier monitoring
            Cashier cashier = new Cashier();
            cashier.setDaemon(true);
            cashier.start();

            // Rapidly generate 100 customers
            try {
                for (int i = 0; i < 100; i++) {
                    // Spawn a new thread every 100ms (10 clients per second)
                    Thread.sleep(100);

                    Client customer;
                    // 20% chance to spawn a VIP, 80% regular customer
                    if (Math.random() < 0.20) {
                        customer = new VIPClient(4000); // VIP stays for 4 seconds
                    } else {
                        customer = new regularCustomer(6000); // Regular stays for 6 seconds
                    }

                    customer.setDaemon(true);
                    customer.start(); // Launch independent thread
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        backendThread.setDaemon(true);
        backendThread.start();
    }
}