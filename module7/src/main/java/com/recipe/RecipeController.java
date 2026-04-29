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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

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

    // Keep references to filters so we can add/remove them reliably
    private final javafx.event.EventHandler<KeyEvent> consumeKeyEvents = evt -> evt.consume();
    private final javafx.event.EventHandler<MouseEvent> consumeMouseEvents = evt -> {
        // Allow selection/copy via context menu by only consuming primary-button presses that would place caret
        if (evt.isPrimaryButtonDown()) {
            evt.consume();
        }
    };

    @FXML
    public void initialize() {
        recipes = FXCollections.observableArrayList();
        recipeListView.setItems(recipes);

        // Load recipes from storage
        loadRecipes();

        // Make details read-only by default and prevent editing via keyboard/mouse
        recipeDetailsArea.setEditable(false);
        recipeDetailsArea.setFocusTraversable(false);
        recipeDetailsArea.addEventFilter(KeyEvent.ANY, consumeKeyEvents);
        recipeDetailsArea.addEventFilter(MouseEvent.ANY, consumeMouseEvents);

        // Listen for recipe selection — only display details
        recipeListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                displayRecipeDetails(newVal);
            } else {
                recipeTitleLabel.setText("No Recipe Selected");
                recipeDetailsArea.clear();
                recipeImageView.setImage(null);
            }
        });

        // Double-click shows details (not edit) and Enter shows details.
        recipeListView.setOnMouseClicked(evt -> {
            if (evt.getClickCount() == 2) {
                Recipe sel = recipeListView.getSelectionModel().getSelectedItem();
                if (sel != null) {
                    displayRecipeDetails(sel);
                }
            }
        });

        recipeListView.setOnKeyPressed(evt -> {
            if (evt.getCode() == KeyCode.ENTER) {
                Recipe sel = recipeListView.getSelectionModel().getSelectedItem();
                if (sel != null) {
                    displayRecipeDetails(sel);
                    evt.consume();
                }
            }
        });

        // Setup cuisine filter
        setupCuisineFilter();

        // Setup search
        searchField.setOnKeyReleased(e -> filterRecipes());

        // Note: global scene key handlers are installed in RecipeApp when the Scene is created.
    }

    private void setupCuisineFilter() {
        ObservableList<String> cuisines = FXCollections.observableArrayList(
                "All Cuisines",
                "Italian",
                "Japanese",
                "American",
                "Desserts",
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
        } else {
            recipeImageView.setImage(null);
        }
    }

    @FXML
    public void openAddRecipeWindow() {
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
    public void openEditRecipeWindow() {
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

            // Optional: allow inline details editing while edit window is open
            setDetailsEditableInline(false); // keep read-only; editing occurs in the edit window

            Stage stage = new Stage();
            stage.setTitle("Edit Recipe");
            stage.setScene(new Scene(root, 600, 700));
            stage.setResizable(false);
            stage.showAndWait();

            // After edit window closes, refresh list and re-display selected recipe (if still present)
            recipeListView.refresh();
            Recipe maybeUpdated = recipes.stream().filter(r -> r.getId().equals(selected.getId())).findFirst().orElse(null);
            if (maybeUpdated != null) {
                recipeListView.getSelectionModel().select(maybeUpdated);
                displayRecipeDetails(maybeUpdated);
            } else {
                recipeTitleLabel.setText("No Recipe Selected");
                recipeDetailsArea.clear();
                recipeImageView.setImage(null);
            }

        } catch (IOException e) {
            showAlert("Error", "Could not open Edit Recipe window: " + e.getMessage());
        }
    }

    @FXML
    public void deleteRecipe() {
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
    public void exitApplication() {
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

                // Use ||| as delimiter to separate fields
                StringBuilder content = new StringBuilder();
                content.append(recipe.getName()).append("|||");
                content.append(recipe.getCuisine()).append("|||");
                content.append(recipe.getPrepTime()).append("|||");
                content.append(recipe.getCookTime()).append("|||");
                content.append(recipe.getServings()).append("|||");
                content.append(recipe.getIngredients()).append("|||");
                content.append(recipe.getInstructions()).append("|||");
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
            String content = new String(Files.readAllBytes(file.toPath()));

            // Split by ||| delimiter instead of newlines
            String[] parts = content.split("\\|\\|\\|");

            if (parts.length >= 8) {
                Recipe recipe = new Recipe(
                        file.getName().replace(".txt", ""),
                        parts[0],                    // name
                        parts[1],                    // cuisine
                        Integer.parseInt(parts[2]), // prepTime
                        Integer.parseInt(parts[3]), // cookTime
                        Integer.parseInt(parts[4]), // servings
                        parts[5],                    // ingredients (with newlines!)
                        parts[6],                    // instructions (with newlines!)
                        parts.length > 7 && !parts[7].isEmpty() ? parts[7] : null  // imagePath
                );
                recipes.add(recipe);
            }
        } catch (Exception e) {
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
                String.join("\n",
                        "400 g spaghetti",
                        "2 large eggs",
                        "100 g guanciale (or pancetta), diced",
                        "100 g freshly grated Parmesan (or Pecorino Romano)",
                        "Freshly ground black pepper",
                        "Salt (for pasta water)"
                ),
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

    // Public helper to toggle inline editability (keeps default behavior: editing only via Edit window)
    public void setDetailsEditableInline(boolean editable) {
        recipeDetailsArea.setEditable(editable);
        recipeDetailsArea.setFocusTraversable(editable);
        if (editable) {
            recipeDetailsArea.removeEventFilter(KeyEvent.ANY, consumeKeyEvents);
            recipeDetailsArea.removeEventFilter(MouseEvent.ANY, consumeMouseEvents);
        } else {
            recipeDetailsArea.addEventFilter(KeyEvent.ANY, consumeKeyEvents);
            recipeDetailsArea.addEventFilter(MouseEvent.ANY, consumeMouseEvents);
        }
    }
}
