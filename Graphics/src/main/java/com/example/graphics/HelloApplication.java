package com.example.graphics;

import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Group root = new Group();
        Scene scene = new Scene(root, 800, 600);
        Circle circle = (new Circle(100, 100, 100));
        root.getChildren().add(circle);

        circle.setCenterX(0 + circle.getRadius());
        circle.setCenterY(0 + circle.getRadius());
        circle.setFill(Color.GREEN);
        Button button = new Button("Hello World");
        root.getChildren().add(button);
        stage.setScene(scene);
        new Thread(() -> {
            Rectangle rectangle1 = new Rectangle(100, 100, 100, 100);
            rectangle1.setFill(Color.YELLOW);
            TranslateTransition translate = new TranslateTransition(Duration.millis(5000), rectangle1);
            translate.setByY(rectangle1.getHeight());
            translate.setByX(rectangle1.getWidth());
            translate.setFromY(0);
            translate.setFromX(0);
            translate.setCycleCount(Transition.INDEFINITE);
            root.getChildren().add(rectangle1);
            translate.play();
        });
        stage.show();
    }
}
