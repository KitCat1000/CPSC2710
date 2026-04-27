/*
 * Author: Nicole Tressler
 * Date: 26 - 04 - 26
 * Auburn University
 * CPSC 2710
 * Recipe Vault App - Final Project
 */

package com.recipe;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class AddRecipeController {

    @FXML
    private TextField recipeNameField;

    @FXML
    private ComboBox<String> cuisineComboBox;

    @FXML
    private Spinner<Integer> prepTimeSpinner;

    @FXML
    private Spinner<Integer> cookTimeSpinner;

    @FXML
    private Spinner<Integer> servingsSpinner;

    @FXML
    private TextArea ingredientsArea;

    @FXML
    private TextArea instructionsArea;

    @FXML
    private Button uploadImageBtn;

    @FXML
    private Label imagePathLabel;

    @FXML
    private Button saveRecipeBtn;

    @FXML
    private Button cancelBtn;

    private RecipeController mainController;
    private Recipe currentRecipe;
    private String selectedImagePath;

    @FXML
    public void initialize() {
        // Setup cuisine dropdown
        cuisineComboBox.getItems().addAll(
                "Italian", "Asian", "American", "Mexican", "Indian", "French", "Mediterranean", "Other"
        );
        cuisineComboBox.setValue("Italian");

        // Setup spinners
        prepTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 480, 15));
        cookTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 480, 30));
        servingsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 4));
    }

    public void setMainController(RecipeController controller) {
        this.mainController = controller;
    }

    public void loadRecipe(Recipe recipe) {
        this.currentRecipe = recipe;
        recipeNameField.setText(recipe.getName());
        cuisineComboBox.setValue(recipe.getCuisine());
        prepTimeSpinner.getValueFactory().setValue(recipe.getPrepTime());
        cookTimeSpinner.getValueFactory().setValue(recipe.getCookTime());
        servingsSpinner.getValueFactory().setValue(recipe.getServings());
        ingredientsArea.setText(recipe.getIngredients());
        instructionsArea.setText(recipe.getInstructions());
        selectedImagePath = recipe.getImagePath();
        if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
            imagePathLabel.setText("Image: " + new File(selectedImagePath).getName());
        }
    }

    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Recipe Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Stage stage = (Stage) uploadImageBtn.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                String destDir = "recipe_images";
                java.nio.file.Files.createDirectories(java.nio.file.Paths.get(destDir));

                String filename = UUID.randomUUID().toString() + "_" + selectedFile.getName();
                String destPath = destDir + "/" + filename;

                Files.copy(selectedFile.toPath(), java.nio.file.Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
                selectedImagePath = destPath;
                imagePathLabel.setText("Image: " + selectedFile.getName());
            } catch (Exception e) {
                showAlert("Error", "Could not upload image: " + e.getMessage());
            }
        }
    }

    @FXML
    private void saveRecipe() {
        if (recipeNameField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter a recipe name.");
            return;
        }

        if (ingredientsArea.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter ingredients.");
            return;
        }

        if (instructionsArea.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter instructions.");
            return;
        }

        Recipe recipe;

        if (currentRecipe != null) {
            // Update existing recipe
            currentRecipe.setName(recipeNameField.getText());
            currentRecipe.setCuisine(cuisineComboBox.getValue());
            currentRecipe.setPrepTime(prepTimeSpinner.getValue());
            currentRecipe.setCookTime(cookTimeSpinner.getValue());
            currentRecipe.setServings(servingsSpinner.getValue());
            currentRecipe.setIngredients(ingredientsArea.getText());
            currentRecipe.setInstructions(instructionsArea.getText());
            currentRecipe.setImagePath(selectedImagePath);
            mainController.updateRecipe(currentRecipe, currentRecipe);
        } else {
            // Create new recipe
            String id = "recipe_" + System.currentTimeMillis();
            recipe = new Recipe(
                    id,
                    recipeNameField.getText(),
                    cuisineComboBox.getValue(),
                    prepTimeSpinner.getValue(),
                    cookTimeSpinner.getValue(),
                    servingsSpinner.getValue(),
                    ingredientsArea.getText(),
                    instructionsArea.getText(),
                    selectedImagePath
            );
            mainController.addRecipe(recipe);
        }

        closeWindow();
    }

    @FXML
    private void cancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
