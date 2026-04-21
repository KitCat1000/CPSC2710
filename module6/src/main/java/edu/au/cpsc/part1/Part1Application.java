/* CPSC
 * Project: CPSC 2710 - Module 6 JavaFX Assignment
 * Author: Nicole Tressler
 * Auburn Email: nit0005@auburn.edu
 * Date: April 20, 2026
 * Description: Practice with JavaFX properties and bindings.
 *
 */

package edu.au.cpsc.part1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Part1Application extends Application {

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(Part1Application.class.getResource("part1-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    stage.setTitle("Part 1");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}