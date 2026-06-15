package projekt.utility;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import projekt.PoolsEnumeration;
import projekt.pool.Pool;


public final class SetUp {
    final static double lineStartY = 0;

    final static double lineEndY = 100;

    public static Cashier addCashier(DoubleProperty Xstart, DoubleBinding Ystart, DoubleProperty radius, Pane pane) {
        Circle cashier = new Circle();
        cashier.setFill(Color.CORAL);
        cashier.setStroke(Color.CORAL);

        cashier.radiusProperty().bind(radius);
        cashier.centerXProperty().bind(Xstart);
        cashier.centerYProperty().bind(Ystart);

        pane.getChildren().add(cashier);
        return new Cashier();
    }

    private static void addLines(Pane basicPane) {
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

    public static Cashier createQue(Pane basicPane) {

        Rectangle desk = new Rectangle();
        desk.xProperty().bind(basicPane.widthProperty().multiply(0.09));
        desk.yProperty().bind(basicPane.heightProperty().multiply(0.5).add(100));
        desk.widthProperty().bind(basicPane.widthProperty().multiply(0.12));
        desk.heightProperty().bind(basicPane.heightProperty().multiply(0.1));

        desk.setFill(Color.BROWN);
        desk.setStroke(Color.BLACK);
        basicPane.getChildren().add(desk);
        DoubleProperty xCenterProp = new javafx.beans.property.SimpleDoubleProperty();
        xCenterProp.bind(desk.xProperty().add(desk.widthProperty().divide(2)));
        DoubleBinding yCenterBinding = desk.yProperty().add(desk.heightProperty()).add(desk.heightProperty());

        return addCashier(xCenterProp, yCenterBinding, desk.heightProperty(), basicPane);
    }

    public static Pool[] producePoolsRepresentations(Pane root) {
        Pool olympicPool = new Pool(root, root.widthProperty().multiply(0.45), root.heightProperty().multiply(0.45),
                root.widthProperty().multiply(0.40), root.heightProperty().multiply(0.18));

        Pool regularPool = new Pool(root, root.widthProperty().multiply(0.40), root.heightProperty().multiply(0.08),
                root.widthProperty().multiply(0.50), root.heightProperty().multiply(0.28));

        Pool childPool = new Pool(root, root.widthProperty().multiply(0.60), root.heightProperty().multiply(0.72),
                root.widthProperty().multiply(0.25), root.heightProperty().multiply(0.15));

        Pool[] tab = new Pool[3];
        tab[PoolsEnumeration.regular.ordinal()] = regularPool;
        tab[PoolsEnumeration.olympic.ordinal()] = olympicPool;
        tab[PoolsEnumeration.children.ordinal()] = childPool;
        return tab;
    }

    public static void produceBackground(Pane basicPane) {
        addLines(basicPane);
    }
}