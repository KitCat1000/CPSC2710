module Part1 {
    requires javafx.controls;
    requires javafx.fxml;

    exports edu.au.cpsc.launcher;
    opens edu.au.cpsc.launcher to javafx.fxml;

    exports edu.au.cpsc.miscstyle;
    opens edu.au.cpsc.miscstyle to javafx.fxml;
}