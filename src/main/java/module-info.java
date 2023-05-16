module com.example.javafxprac {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.relatedrate to javafx.fxml;
    exports com.relatedrate;
}