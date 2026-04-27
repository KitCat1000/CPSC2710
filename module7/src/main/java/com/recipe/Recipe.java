package com.recipe;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Recipe {
    private String id;
    private SimpleStringProperty name;
    private SimpleStringProperty cuisine;
    private SimpleIntegerProperty prepTime;
    private SimpleIntegerProperty cookTime;
    private SimpleIntegerProperty servings;
    private SimpleStringProperty ingredients;
    private SimpleStringProperty instructions;
    private SimpleStringProperty imagePath;

    public Recipe(String id, String name, String cuisine, int prepTime, 
                 int cookTime, int servings, String ingredients, 
                 String instructions, String imagePath) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.cuisine = new SimpleStringProperty(cuisine);
        this.prepTime = new SimpleIntegerProperty(prepTime);
        this.cookTime = new SimpleIntegerProperty(cookTime);
        this.servings = new SimpleIntegerProperty(servings);
        this.ingredients = new SimpleStringProperty(ingredients);
        this.instructions = new SimpleStringProperty(instructions);
        this.imagePath = new SimpleStringProperty(imagePath);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name.get(); }
    public void setName(String value) { name.set(value); }
    public SimpleStringProperty nameProperty() { return name; }

    public String getCuisine() { return cuisine.get(); }
    public void setCuisine(String value) { cuisine.set(value); }
    public SimpleStringProperty cuisineProperty() { return cuisine; }

    public int getPrepTime() { return prepTime.get(); }
    public void setPrepTime(int value) { prepTime.set(value); }
    public SimpleIntegerProperty prepTimeProperty() { return prepTime; }

    public int getCookTime() { return cookTime.get(); }
    public void setCookTime(int value) { cookTime.set(value); }
    public SimpleIntegerProperty cookTimeProperty() { return cookTime; }

    public int getServings() { return servings.get(); }
    public void setServings(int value) { servings.set(value); }
    public SimpleIntegerProperty servingsProperty() { return servings; }

    public String getIngredients() { return ingredients.get(); }
    public void setIngredients(String value) { ingredients.set(value); }
    public SimpleStringProperty ingredientsProperty() { return ingredients; }

    public String getInstructions() { return instructions.get(); }
    public void setInstructions(String value) { instructions.set(value); }
    public SimpleStringProperty instructionsProperty() { return instructions; }

    public String getImagePath() { return imagePath.get(); }
    public void setImagePath(String value) { imagePath.set(value); }
    public SimpleStringProperty imagePathProperty() { return imagePath; }

    @Override
    public String toString() {
        return name.get();
    }
}
