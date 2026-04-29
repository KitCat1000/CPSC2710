package com.recipe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class RecipeApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
        BorderPane root = loader.load();

        RecipeController controller = loader.getController();
        controller.setMainStage(primaryStage);

        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

        // Event filter (optional backup)
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            // Logging for debug
            System.out.println("Key pressed: " + event.getCode());
            try {
                boolean shortcutDown = event.isControlDown() || event.isMetaDown();
                if (shortcutDown && event.getCode() == KeyCode.N) {
                    controller.openAddRecipeWindow(); event.consume();
                } else if (shortcutDown && event.getCode() == KeyCode.E) {
                    controller.openEditRecipeWindow(); event.consume();
                } else if (event.getCode() == KeyCode.DELETE) {
                    controller.deleteRecipe(); event.consume();
                } else if (shortcutDown && event.getCode() == KeyCode.Q) {
                    controller.exitApplication(); event.consume();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Create a MenuBar with accelerators but DO NOT add it to the scene graph.
        // This registers accelerators with the scene without altering the UI layout.
        MenuBar accelBar = new MenuBar();
        accelBar.setVisible(false); // not necessary since it won't be attached, but keep for clarity

        Menu accelMenu = new Menu("accels");

        MenuItem addAccel = new MenuItem("Add");
        addAccel.setAccelerator(KeyCombination.keyCombination("SHORTCUT+N"));
        addAccel.setOnAction(e -> {
            try { controller.openAddRecipeWindow(); } catch (Exception ex) { ex.printStackTrace(); }
        });

        MenuItem editAccel = new MenuItem("Edit");
        editAccel.setAccelerator(KeyCombination.keyCombination("SHORTCUT+E"));
        editAccel.setOnAction(e -> {
            try { controller.openEditRecipeWindow(); } catch (Exception ex) { ex.printStackTrace(); }
        });

        MenuItem deleteAccel = new MenuItem("Delete");
        deleteAccel.setAccelerator(KeyCombination.keyCombination("DELETE"));
        deleteAccel.setOnAction(e -> {
            try { controller.deleteRecipe(); } catch (Exception ex) { ex.printStackTrace(); }
        });

        MenuItem quitAccel = new MenuItem("Quit");
        quitAccel.setAccelerator(KeyCombination.keyCombination("SHORTCUT+Q"));
        quitAccel.setOnAction(e -> {
            try { controller.exitApplication(); } catch (Exception ex) { ex.printStackTrace(); }
        });

        accelMenu.getItems().addAll(addAccel, editAccel, deleteAccel, quitAccel);
        accelBar.getMenus().add(accelMenu);

        // DO NOT attach accelBar to root; accelerators remain registered with the scene.

        primaryStage.setTitle("Recipe Vault");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Ensure focus goes to root so key events start flowing
        root.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
