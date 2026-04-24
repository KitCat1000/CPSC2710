/*
 * Project: Module 6 Part 2 - Airline Flight Designator Application with UI Model
 * Author: Nicole Tressler
 * Email: Nit0005@auburn.edu
 * Date: April 23, 2026
 * Description: Main application class for the enhanced Flight Designator App with UI Model
 */

package com.airline;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FlightScheduleApplication extends Application {
    private static final String DATABASE_FILE = "flights.db";

    @Override
    public void start(Stage stage) throws Exception {
        try {
            // Load the FXML file from resources
            FXMLLoader fxmlLoader = new FXMLLoader(
                    FlightScheduleApplication.class.getResource("/com/airline/flight-schedule-view.fxml")
            );
            Scene scene = new Scene(fxmlLoader.load(), 1000, 700);

            // Set the window title to the Part 2 version
            stage.setTitle("Nicole Tressler's Flight Designator App V2");
            stage.setScene(scene);
            stage.show();

            // Get the controller and set up database
            FlightScheduleController controller = fxmlLoader.getController();
            AirlineDatabase database = loadDatabase();
            controller.setDatabase(database);

            // Save database on close
            stage.setOnCloseRequest(event -> {
                try {
                    saveDatabase(database);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private AirlineDatabase loadDatabase() throws IOException {
        try {
            return AirlineDatabaseIO.loadFromFile(DATABASE_FILE);
        } catch (IOException e) {
            // Database file doesn't exist yet, create a new one
            return new AirlineDatabase();
        }
    }

    private void saveDatabase(AirlineDatabase database) throws IOException {
        AirlineDatabaseIO.saveToFile(database, DATABASE_FILE);
    }

    public static void main(String[] args) {
        launch();
    }
}