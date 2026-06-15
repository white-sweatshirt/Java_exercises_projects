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
import projekt.Consumer.ClientWithChild; // Import the new class

public class AppFX extends Application {
    final  int timeInPoolsMs = 5000;
    @Override
    public void start(Stage stage) {

        Pane pane = new Pane();
        Scene firstScene = new Scene(pane, 1000, 800);
        stage.setScene(firstScene);
        firstScene.setFill(Color.WHITE);
        stage.setTitle("Projekt Franciszek Wawer");

        Pool[] pools = SetUp.producePoolsRepresentations(pane);
        SetUp.produceBackground(pane);
        Client.setPools(pools);
        Client.setMainPane(pane);
        Cashier.setPools(pools);
        Cashier cashier = SetUp.createQue(pane);
        stage.show();
        Thread backendThread = new Thread(() -> {
            for (Pool p : pools) {
                p.start();
            }
            cashier.start();
            try {
                for (int i = 0; i < 100; i++) {
                    Thread.sleep(100);
                    Client customer;
                    double randomValue = Math.random();
                    if (randomValue < 0.20) {
                        customer = new VIPClient(timeInPoolsMs);
                    } else if (randomValue < 0.40) {
                        customer = new ClientWithChild(timeInPoolsMs);
                    } else {
                        customer = new regularCustomer(timeInPoolsMs);
                    }
                    customer.start();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        backendThread.start();
    }
}