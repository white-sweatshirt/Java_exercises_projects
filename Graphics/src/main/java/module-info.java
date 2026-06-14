module com.example.graphics {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.graphics to javafx.fxml;
    exports com.example.graphics;
}