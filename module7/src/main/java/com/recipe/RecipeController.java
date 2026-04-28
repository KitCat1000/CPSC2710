/*
 * Author: Nicole Tressler
 * Date: 26 - 04 - 26
 * Auburn University
 * CPSC 2710
 * Recipe Vault App - Final Project
 */

package com.recipe;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RecipeController {

    @FXML
    private ListView<Recipe> recipeListView;

    @FXML
    private TextArea recipeDetailsArea;

    @FXML
    private Label recipeTitleLabel;

    @FXML
    private ImageView recipeImageView;

    @FXML
    private Button addRecipeBtn;

    @FXML
    private Button editRecipeBtn;

    @FXML
    private Button deleteRecipeBtn;

    @FXML
    private ComboBox<String> cuisineFilterComboBox;

    @FXML
    private TextField searchField;

    @FXML
    private Label statusLabel;

    private ObservableList<Recipe> recipes;
    private Stage mainStage;
    private final String RECIPES_DIR = "recipes_data";

    @FXML
    public void initialize() {
        recipes = FXCollections.observableArrayList();
        recipeListView.setItems(recipes);

        // Load recipes from storage
        loadRecipes();

        // Listen for recipe selection
        recipeListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                displayRecipeDetails(newVal);
            }
        });

        // Setup cuisine filter
        setupCuisineFilter();

        // Setup search
        searchField.setOnKeyReleased(e -> filterRecipes());
    }

    private void setupCuisineFilter() {
        ObservableList<String> cuisines = FXCollections.observableArrayList(
                "All Cuisines",
                "Italian",
                "Japanese",
                "American",
                "Desserts",
                "Mexican",
                "Indian",
                "German",
                "Chinese",
                "French",
                "Mediterranean",
                "Other"
        );
        cuisineFilterComboBox.setItems(cuisines);
        cuisineFilterComboBox.setValue("All Cuisines");
        cuisineFilterComboBox.setOnAction(e -> filterRecipes());
    }

    private void filterRecipes() {
        String searchText = searchField.getText().toLowerCase();
        String cuisineFilter = cuisineFilterComboBox.getValue();

        recipeListView.setItems(recipes.filtered(recipe -> {
            boolean matchesCuisine = cuisineFilter.equals("All Cuisines") ||
                    recipe.getCuisine().equals(cuisineFilter);
            boolean matchesSearch = recipe.getName().toLowerCase().contains(searchText) ||
                    recipe.getIngredients().toLowerCase().contains(searchText);
            return matchesCuisine && matchesSearch;
        }));
    }

    private void displayRecipeDetails(Recipe recipe) {
        recipeTitleLabel.setText(recipe.getName());

        StringBuilder details = new StringBuilder();
        details.append("Cuisine: ").append(recipe.getCuisine()).append("\n");
        details.append("Prep Time: ").append(recipe.getPrepTime()).append(" minutes\n");
        details.append("Cook Time: ").append(recipe.getCookTime()).append(" minutes\n");
        details.append("Servings: ").append(recipe.getServings()).append("\n\n");
        details.append("INGREDIENTS:\n");
        details.append(recipe.getIngredients()).append("\n\n");
        details.append("INSTRUCTIONS:\n");
        details.append(recipe.getInstructions());

        recipeDetailsArea.setText(details.toString());

        // Load and display image if it exists
        if (recipe.getImagePath() != null && !recipe.getImagePath().isEmpty()) {
            try {
                recipeImageView.setImage(new javafx.scene.image.Image("file:" + recipe.getImagePath()));
            } catch (Exception e) {
                System.out.println("Could not load image: " + e.getMessage());
            }
        }
    }

    @FXML
    private void openAddRecipeWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddRecipeWindow.fxml"));
            VBox root = loader.load();

            AddRecipeController controller = loader.getController();
            controller.setMainController(this);

            Stage stage = new Stage();
            stage.setTitle("Add New Recipe");
            stage.setScene(new Scene(root, 600, 700));
            stage.setResizable(false);
            stage.showAndWait();

        } catch (IOException e) {
            showAlert("Error", "Could not open Add Recipe window: " + e.getMessage());
        }
    }

    @FXML
    private void openEditRecipeWindow() {
        Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Info", "Please select a recipe to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddRecipeWindow.fxml"));
            VBox root = loader.load();

            AddRecipeController controller = loader.getController();
            controller.setMainController(this);
            controller.loadRecipe(selected);

            Stage stage = new Stage();
            stage.setTitle("Edit Recipe");
            stage.setScene(new Scene(root, 600, 700));
            stage.setResizable(false);
            stage.showAndWait();

        } catch (IOException e) {
            showAlert("Error", "Could not open Edit Recipe window: " + e.getMessage());
        }
    }

    @FXML
    private void deleteRecipe() {
        Recipe selected = recipeListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Info", "Please select a recipe to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Recipe");
        alert.setHeaderText("Delete '" + selected.getName() + "'?");
        alert.setContentText("This action cannot be undone.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            recipes.remove(selected);
            saveRecipes();
            statusLabel.setText("Recipe deleted successfully.");
            recipeTitleLabel.setText("No Recipe Selected");
            recipeDetailsArea.clear();
            recipeImageView.setImage(null);
        }
    }

    @FXML
    private void exitApplication() {
        Stage stage = (Stage) recipeListView.getScene().getWindow();
        stage.close();
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
        saveRecipes();
        statusLabel.setText("Recipe '" + recipe.getName() + "' added successfully!");
    }

    public void updateRecipe(Recipe oldRecipe, Recipe newRecipe) {
        int index = recipes.indexOf(oldRecipe);
        if (index >= 0) {
            recipes.set(index, newRecipe);
            saveRecipes();
            statusLabel.setText("Recipe updated successfully!");
            recipeListView.refresh();
        }
    }

    private void saveRecipes() {
        try {
            Files.createDirectories(Paths.get(RECIPES_DIR));

            for (Recipe recipe : recipes) {
                String filename = RECIPES_DIR + "/" + recipe.getId() + ".txt";
                StringBuilder content = new StringBuilder();
                content.append(recipe.getName()).append("\n");
                content.append(recipe.getCuisine()).append("\n");
                content.append(recipe.getPrepTime()).append("\n");
                content.append(recipe.getCookTime()).append("\n");
                content.append(recipe.getServings()).append("\n");
                content.append(recipe.getIngredients()).append("\n");
                content.append(recipe.getInstructions()).append("\n");
                content.append(recipe.getImagePath() != null ? recipe.getImagePath() : "");

                Files.write(Paths.get(filename), content.toString().getBytes());
            }
        } catch (IOException e) {
            System.out.println("Error saving recipes: " + e.getMessage());
        }
    }

    private void loadRecipes() {
        try {
            Files.createDirectories(Paths.get(RECIPES_DIR));
            File dir = new File(RECIPES_DIR);

            if (dir.listFiles() != null) {
                for (File file : dir.listFiles((d, name) -> name.endsWith(".txt"))) {
                    loadRecipeFromFile(file);
                }
            }

            // Add sample recipe if no recipes exist
            if (recipes.isEmpty()) {
                addSampleRecipe();
            }
        } catch (IOException e) {
            System.out.println("Error loading recipes: " + e.getMessage());
        }
    }

    private void loadRecipeFromFile(File file) {
        try {
            String[] lines = new String(Files.readAllBytes(file.toPath())).split("\n");
            if (lines.length >= 8) {
                Recipe recipe = new Recipe(
                        file.getName().replace(".txt", ""),
                        lines[0],
                        lines[1],
                        Integer.parseInt(lines[2]),
                        Integer.parseInt(lines[3]),
                        Integer.parseInt(lines[4]),
                        lines[5],
                        lines[6],
                        lines.length > 7 && !lines[7].isEmpty() ? lines[7] : null
                );
                recipes.add(recipe);
            }
        } catch (IOException e) {
            System.out.println("Error loading recipe from file: " + e.getMessage());
        }
    }

    private void addSampleRecipe() {
        Recipe sample = new Recipe(
                "recipe_1",
                "Spaghetti Carbonara",
                "Italian",
                10,
                20,
                4,
// Ingredients: each ingredient on its own line (or JSON/CSV if you prefer)
                String.join("\n",
                        "400 g spaghetti",
                        "2 large eggs",
                        "100 g guanciale (or pancetta), diced",
                        "100 g freshly grated Parmesan (or Pecorino Romano)",
                        "Freshly ground black pepper",
                        "Salt (for pasta water)"
                ),
// Instructions: numbered steps, blank line between sections for clarity
                String.join("\n",
                        "1) Bring a large pot of salted water to a boil. Cook the spaghetti until al dente according to package directions.",
                        "",
                        "2) While pasta cooks, heat a skillet over medium heat and cook the diced guanciale until crispy and golden. Remove from heat and reserve the fat in the pan.",
                        "",
                        "3) In a bowl, whisk the eggs with most of the grated cheese (reserve a little for serving) and a generous amount of freshly ground black pepper.",
                        "",
                        "4) When the pasta is done, reserve ~1 cup of pasta cooking water, then drain the pasta.",
                        "",
                        "5) Add the hot pasta to the skillet with the guanciale fat (off the heat). Quickly pour the egg-and-cheese mixture over the pasta and toss vigorously to create a creamy sauce — add small amounts of reserved pasta water if needed to loosen the sauce. The residual heat will cook the eggs without scrambling them.",
                        "",
                        "6) Stir in the crispy guanciale, taste and adjust seasoning with salt and more pepper if needed.",
                        "",
                        "7) Serve immediately topped with the remaining grated cheese."
                ),
// imagePath (null for sample)
                null

        );
        recipes.add(sample);
        saveRecipes();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }
}
