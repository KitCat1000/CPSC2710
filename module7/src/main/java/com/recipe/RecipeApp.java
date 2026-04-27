/*
 * Author: Nicole Tressler
 * Date: 26 - 04 - 26
 * Auburn University
 * CPSC 2710
 * Recipe Vault App - Final Project
 */

package com.recipe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class RecipeApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
        BorderPane root = loader.load();
        
        RecipeController controller = loader.getController();
        controller.setMainStage(primaryStage);
        
        primaryStage.setTitle("🏺 Recipe Vault");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
