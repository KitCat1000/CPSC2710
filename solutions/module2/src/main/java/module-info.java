module org.example.module2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.module2 to javafx.fxml;
    exports org.example.module2;
}