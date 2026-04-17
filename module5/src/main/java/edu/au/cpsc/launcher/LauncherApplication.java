/*
 * Assignment: Part 2 - Launcher Application
 * Author: Nicole Tressler
 * Auburn Email: nit0005@auburn.edu
 * Date: 16 - 04 - 2026
 * Description: A launcher application with a 2x2 grid of buttons to access different modules
 */

        package edu.au.cpsc.launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class LauncherApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LauncherApplication.class.getResource("launcher-app.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500); // Adjust size as needed

        // Load the CSS file
        scene.getStylesheets().add(LauncherApplication.class.getResource("style/main.css").toExternalForm());

        stage.setTitle("Flight Designator Launcher");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}