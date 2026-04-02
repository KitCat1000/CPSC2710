module org.example.module3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.module3 to javafx.fxml;
    exports org.example.module3;
}