package projekt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import projekt.graphics.SetUp;

public class AppFX extends Application
{

    @Override
    public void start(Stage stage)
    {

        Pane pane = new Pane();
        Scene firstScene = new Scene(pane, 1000, 800);
        stage.setScene(firstScene);
        firstScene.setFill(Color.WHITE);
        stage.setTitle("Projekt Franciszek Wawer");
        //SetUp.produceBackground(pane);
        stage.show();
        Pane supe2 = new Pane();
        supe2.layoutXProperty().bind(pane.widthProperty().divide(2));
        supe2.layoutYProperty().bind(pane.heightProperty().divide(2));
        supe2.setPrefWidth(100);
        supe2.setPrefHeight(400);
        SetUp.produceBackground(pane);
        /*EventClass appExpiriance = new EventClass(pane);
        appExpiriance.start();
        try
        {
            appExpiriance.join();
        } catch (InterruptedException e)
        {
            appExpiriance.interrupt();
        }*/
    }
}
