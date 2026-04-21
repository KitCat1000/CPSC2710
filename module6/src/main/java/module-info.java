module org.example.module6 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.module6 to javafx.fxml;
    exports org.example.module6;
}