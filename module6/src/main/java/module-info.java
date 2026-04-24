module org.example.module6 {
    requires javafx.controls;
    requires javafx.fxml;

    opens edu.au.cpsc.part1 to javafx.fxml;
    exports edu.au.cpsc.part1;
    exports com.airline;
    opens com.airline to javafx.fxml;
}