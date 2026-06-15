package projekt.utility;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import projekt.PoolsEnumeration;
import projekt.pool.Pool;

public final class SetUp {
    final static double lineStartY = 0;
    final static double lineEndY = 100;

    // --- Vertical FlowPanes to handle top-to-bottom filling with wrapping columns ---
    public static final FlowPane normalQueueBox = new FlowPane(Orientation.VERTICAL);
    public static final FlowPane vipQueueBox = new FlowPane(Orientation.VERTICAL);

    // --- Live Occupancy HUD Display Text Components ---
    public static final Text normalCountLabel = new Text("Queue: 0 / 44");
    public static final Text vipCountLabel = new Text("Queue: 0 / 22");

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
        // Line A: Moves from 0.2 to 0.3 to match shifted layouts
        Line a = new Line(100, lineStartY, 100, lineEndY);
        a.startXProperty().bind(basicPane.widthProperty().multiply(0.3));
        a.endXProperty().bind(basicPane.widthProperty().multiply(0.3));
        a.endYProperty().bind(basicPane.heightProperty().multiply(0.5));
        a.setStroke(Color.BLACK);

        // Line B: Moves from 0.3 to 0.4 to match shifted layouts
        Line b = new Line(200, lineStartY, 200, lineEndY);
        b.startXProperty().bind(basicPane.widthProperty().multiply(0.4));
        b.endXProperty().bind(basicPane.widthProperty().multiply(0.4));
        b.endYProperty().bind(basicPane.heightProperty().multiply(0.5));
        b.setStroke(Color.BLACK);

        basicPane.getChildren().addAll(a, b);
    }

    public static Cashier createQue(Pane basicPane) {
        // Shifted desk rightwards to start at 0.25 to sit squarely under shifted lanes
        Rectangle desk = new Rectangle();
        desk.xProperty().bind(basicPane.widthProperty().multiply(0.25));
        desk.yProperty().bind(basicPane.heightProperty().multiply(0.5).add(100));
        desk.widthProperty().bind(basicPane.widthProperty().multiply(0.12));
        desk.heightProperty().bind(basicPane.heightProperty().multiply(0.1));

        desk.setFill(Color.BROWN);
        desk.setStroke(Color.BLACK);
        basicPane.getChildren().add(desk);

        // normalQueueBox now stretches across 0.1 to 0.3 (Width: 0.2)
        normalQueueBox.setVgap(8);
        normalQueueBox.setHgap(6);
        normalQueueBox.setAlignment(Pos.TOP_CENTER);
        normalQueueBox.layoutXProperty().bind(basicPane.widthProperty().multiply(0.1));
        normalQueueBox.layoutYProperty().setValue(10);
        normalQueueBox.prefWidthProperty().bind(basicPane.widthProperty().multiply(0.2));
        normalQueueBox.prefHeightProperty().bind(desk.yProperty().subtract(40));

        // vipQueueBox now starts at 0.3 and spans to 0.4 (Width: 0.1)
        vipQueueBox.setVgap(8);
        vipQueueBox.setHgap(6);
        vipQueueBox.setAlignment(Pos.TOP_CENTER);
        vipQueueBox.layoutXProperty().bind(basicPane.widthProperty().multiply(0.3));
        vipQueueBox.layoutYProperty().setValue(10);
        vipQueueBox.prefWidthProperty().bind(basicPane.widthProperty().multiply(0.1));
        vipQueueBox.prefHeightProperty().bind(desk.yProperty().subtract(40));

        // Setup the live scoreboard labels under the shifted lanes
        VBox normalLabelContainer = new VBox();
        normalLabelContainer.setAlignment(Pos.CENTER);
        normalLabelContainer.layoutXProperty().bind(basicPane.widthProperty().multiply(0.1));
        normalLabelContainer.layoutYProperty().bind(desk.yProperty().subtract(30));
        normalLabelContainer.prefWidthProperty().bind(basicPane.widthProperty().multiply(0.2));
        normalCountLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");
        normalCountLabel.setFill(Color.DARKBLUE);
        normalLabelContainer.getChildren().add(normalCountLabel);

        VBox vipLabelContainer = new VBox();
        vipLabelContainer.setAlignment(Pos.CENTER);
        vipLabelContainer.layoutXProperty().bind(basicPane.widthProperty().multiply(0.3));
        vipLabelContainer.layoutYProperty().bind(desk.yProperty().subtract(30));
        vipLabelContainer.prefWidthProperty().bind(basicPane.widthProperty().multiply(0.1));
        vipCountLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");
        vipCountLabel.setFill(Color.CHOCOLATE);
        vipLabelContainer.getChildren().add(vipCountLabel);

        basicPane.getChildren().addAll(normalQueueBox, vipQueueBox, normalLabelContainer, vipLabelContainer);

        DoubleProperty xCenterProp = new javafx.beans.property.SimpleDoubleProperty();
        xCenterProp.bind(desk.xProperty().add(desk.widthProperty().divide(2)));
        DoubleBinding yCenterBinding = desk.yProperty().add(desk.heightProperty()).add(desk.heightProperty());

        return addCashier(xCenterProp, yCenterBinding, desk.heightProperty(), basicPane);
    }

    public static Pool[] producePoolsRepresentations(Pane root) {
        // Shifted pool start origins rightwards to prevent background overlaps with our new lines layout
        Pool olympicPool = new Pool(root, root.widthProperty().multiply(0.55), root.heightProperty().multiply(0.45), root.widthProperty().multiply(0.40), root.heightProperty().multiply(0.18));
        olympicPool.setPoolType(PoolsEnumeration.olympic);

        Pool regularPool = new Pool(root, root.widthProperty().multiply(0.50), root.heightProperty().multiply(0.08),
                root.widthProperty().multiply(0.45), root.heightProperty().multiply(0.28));
        regularPool.setPoolType(PoolsEnumeration.regular);

        Pool childPool = new Pool(root, root.widthProperty().multiply(0.65), root.heightProperty().multiply(0.72), root.widthProperty().multiply(0.25), root.heightProperty().multiply(0.15));
        childPool.setPoolType(PoolsEnumeration.children);

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