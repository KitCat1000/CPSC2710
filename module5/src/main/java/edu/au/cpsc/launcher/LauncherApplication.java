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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class LauncherApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                LauncherApplication.class.getResource("launcher-app.fxml")
        );

        if (loader.getLocation() == null) {
            System.err.println("ERROR: launcher-app.fxml not found!");
            System.err.println("Make sure the file is at: resources/edu/au/cpsc/launcher/launcher-app.fxml");
            throw new IOException("launcher-app.fxml not found");
        }

        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 500);

        // Load the CSS file
        String css = LauncherApplication.class.getResource("style/main.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("Flight Designator Launcher");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}