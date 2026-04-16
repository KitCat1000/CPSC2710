/*
 * Assignment: Part 1 - Flight Designator App
 * Author: Nicole Tressler
 * Auburn Email: nit0005@auburn.edu
 * Date: April 14, 2026
 * Description: A JavaFX application demonstrating CSS styling with various UI controls
 */


package edu.au.cpsc.miscstyle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Part1Application extends Application {

  @Override
  public void start(Stage stage) throws IOException {
      FXMLLoader fxmlLoader = new FXMLLoader(
              Part1Application.class.getResource("/edu/au/cpsc/miscstyle/part1.fxml")
      );
      Scene scene = new Scene(fxmlLoader.load());
    stage.setTitle("Nicole Tressler's Flight Designator App");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}