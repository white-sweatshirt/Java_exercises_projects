package projekt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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

        Pool[] pools = SetUp.producePoolsRepresentations(pane);
        SetUp.produceBackground(pane);

        // Pass the pools to the clients so they can search for free spots
        Client.setPools(pools);

        stage.show();

        // -------------------------------------------------------------
        // BACKEND THREAD: Keeps JavaFX unblocked and manages logic
        // -------------------------------------------------------------
        Thread backendThread = new Thread(() -> {
            // Start the independent Pool threads
            for (Pool p : pools) {
                p.start();
            }

            // Start Cashier thread here (assuming you instantiate it)
            // Cashier cashier = new Cashier();
            // cashier.start();

            // Example generation of clients
            try {
                for (int i = 0; i < 35; i++) {
                    Thread.sleep(500); // Stagger customer arrivals
                    regularCustomer customer = new regularCustomer(5000); // 5 seconds in pool
                    customer.start();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Setting to daemon ensures it closes when you exit the JavaFX window
        backendThread.setDaemon(true);
        backendThread.start();
    }
}