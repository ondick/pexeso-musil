module org.example.pexesotextove {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.pexesotextove to javafx.fxml;
    exports org.example.pexesotextove;
}