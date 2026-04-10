module org.example.module4 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.airline to javafx.fxml;
    exports com.airline;
}