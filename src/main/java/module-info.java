module com.example.rubank_ui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.rubank_ui to javafx.fxml;
    exports com.example.rubank_ui;
}