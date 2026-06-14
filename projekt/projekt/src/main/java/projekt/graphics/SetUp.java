package projekt.graphics;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public final class SetUp
{
    final static double lineStartY = 0;

    final static double lineEndY = 100;

    private static void addCashier()
    {
        // this proves person at work  is not ...
        Circle cashier = new Circle();
        cashier.setFill(Color.CORAL);
        cashier.setStroke(Color.CORAL);

    }

    private static void addLines(Pane basicPane)
    {
        Line a = new Line(100, lineStartY, 100, lineEndY);
        a.startXProperty().bind(basicPane.widthProperty().multiply(0.1));
        a.endXProperty().bind(basicPane.widthProperty().multiply(0.1));
        a.endYProperty().bind(basicPane.heightProperty().multiply(0.5));
        a.setStroke(Color.BLACK);
        Line b = new Line(200, lineStartY, 200, lineEndY);
        b.startXProperty().bind(basicPane.widthProperty().multiply(0.2));
        b.endXProperty().bind(basicPane.widthProperty().multiply(0.2));
        b.endYProperty().bind(basicPane.heightProperty().multiply(0.5));
        b.setStroke(Color.BLACK);
        basicPane.getChildren().addAll(a, b);
    }

    public static void addDesk(Pane basicPane)
    {
        Rectangle desk = new Rectangle();
        desk.xProperty().bind(basicPane.widthProperty().multiply(0.09));
        desk.yProperty().bind(basicPane.heightProperty().multiply(0.5).add(100));
        desk.widthProperty().bind(basicPane.widthProperty().multiply(0.12));
        desk.heightProperty().bind(basicPane.heightProperty().multiply(0.1));
        desk.setFill(Color.BROWN);
        desk.setStroke(Color.BLACK);
        basicPane.getChildren().add(desk);
    }

    public static void addPoolsRepresentations(Pane root)
    {
        Pane regularPoolPane = new Pane();
        Pane olympicPoolPane = new Pane();
        Pane childPoolPane = new Pane();

        Rectangle regularWater = new Rectangle();
        Rectangle olympicWater = new Rectangle();
        Circle childWater = new Circle();

        regularWater.setFill(Color.LIGHTBLUE);
        olympicWater.setFill(Color.LIGHTBLUE);
        childWater.setFill(Color.LIGHTBLUE);

        regularWater.setStroke(Color.BLACK);
        olympicWater.setStroke(Color.BLACK);
        childWater.setStroke(Color.BLACK);


        regularPoolPane.layoutXProperty().bind(root.widthProperty().multiply(0.40));
        regularPoolPane.layoutYProperty().bind(root.heightProperty().multiply(0.08));
        regularPoolPane.prefWidthProperty().bind(root.widthProperty().multiply(0.50));
        regularPoolPane.prefHeightProperty().bind(root.heightProperty().multiply(0.28));

        regularWater.widthProperty().bind(regularPoolPane.prefWidthProperty());
        regularWater.heightProperty().bind(regularPoolPane.prefHeightProperty());

        olympicPoolPane.layoutXProperty().bind(root.widthProperty().multiply(0.8));
        olympicPoolPane.layoutYProperty().bind(root.heightProperty().multiply(0.45));
        olympicPoolPane.prefWidthProperty().bind(root.widthProperty().multiply(0.40));
        olympicPoolPane.prefHeightProperty().bind(root.heightProperty().multiply(0.18));

        olympicWater.widthProperty().bind(olympicPoolPane.prefWidthProperty());
        olympicWater.heightProperty().bind(olympicPoolPane.prefHeightProperty());

        childPoolPane.layoutXProperty().bind(root.widthProperty().multiply(0.65));
        childPoolPane.layoutYProperty().bind(root.heightProperty().multiply(0.72));
        childPoolPane.prefWidthProperty().bind(root.widthProperty().multiply(0.20));
        childPoolPane.prefHeightProperty().bind(root.widthProperty().multiply(0.20));

        childWater.centerXProperty().bind(childPoolPane.prefWidthProperty().divide(2));
        childWater.centerYProperty().bind(childPoolPane.prefHeightProperty().divide(2));
        childWater.radiusProperty().bind(childPoolPane.prefWidthProperty().divide(2));

        // Add water shapes as background layer
        regularPoolPane.getChildren().add(regularWater);
        olympicPoolPane.getChildren().add(olympicWater);
        childPoolPane.getChildren().add(childWater);

        root.getChildren().addAll(regularPoolPane, olympicPoolPane, childPoolPane);
    }

    public static void produceBackground(Pane basicPane)
    {
        addLines(basicPane);
        addDesk(basicPane);
        addPoolsRepresentations(basicPane);
    }

}
