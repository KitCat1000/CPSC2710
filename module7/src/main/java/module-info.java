module com.recipe {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.recipe to javafx.fxml;
    exports com.recipe;
}