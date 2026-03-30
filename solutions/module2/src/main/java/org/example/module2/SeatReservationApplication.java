/*
 * Project: Module 2
 * Author: Nicole Tressler
 * Email: nit0005@auburn.edu
 * Date: 2026 - 03 - 29
 * Description: Flight reservation app
 */

package org.example.module2;

import org.example.module2.SeatReservation;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.time.LocalDate;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class SeatReservationApplication extends Application {

    private SeatReservation seatReservation;

    // GUI Controls
    private TextField flightDesignatorField;
    private DatePicker flightDatePicker;
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField numberOfBagsField;
    private CheckBox flyingWithInfantCheckBox;
    private CheckBox flyingWithTravelInsuranceCheckBox;
    private TextField numberOfPassengersField;

    @Override
    public void start(Stage stage) {
        // Create and initialize the SeatReservation object
        seatReservation = new SeatReservation();
        seatReservation.setFlightDesignator("AA100");
        seatReservation.setFlightDate(LocalDate.now());
        seatReservation.setFirstName("Nicole");
        seatReservation.setLastName("Tressler");
        seatReservation.setNumberOfBags(2);
        seatReservation.makeNotFlyingWithInfant();
        seatReservation.makeNotFlyingWithTravelInsurance();


        // Create the GUI
        BorderPane root = createGUI();

        // Update UI with the initial values
        updateUI();

        // Create and show the scene
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle(seatReservation.getFirstName() + " " + seatReservation.getLastName() + "'s Seat Reservation App");
        stage.setScene(scene);
        stage.show();
    }

    private BorderPane createGUI() {
        BorderPane borderPane = new BorderPane();

        // Create the center GridPane with labels and controls
        GridPane gridPane = createGridPane();
        borderPane.setCenter(gridPane);

        // Create the bottom HBox with buttons
        HBox buttonBox = createButtonBox();
        borderPane.setBottom(buttonBox);

        return borderPane;
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(15));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        int row = 0;

        // Flight Designator
        gridPane.add(new Label("Flight Designator:"), 0, row);
        flightDesignatorField = new TextField();
        gridPane.add(flightDesignatorField, 1, row);
        row++;

        // Flight Date
        gridPane.add(new Label("Flight Date:"), 0, row);
        flightDatePicker = new DatePicker();
        gridPane.add(flightDatePicker, 1, row);
        row++;

        // First Name
        gridPane.add(new Label("First Name:"), 0, row);
        firstNameField = new TextField();
        gridPane.add(firstNameField, 1, row);
        row++;

        // Last Name
        gridPane.add(new Label("Last Name:"), 0, row);
        lastNameField = new TextField();
        gridPane.add(lastNameField, 1, row);
        row++;

        // Number of Bags
        gridPane.add(new Label("Number of Bags:"), 0, row);
        numberOfBagsField = new TextField();
        gridPane.add(numberOfBagsField, 1, row);
        row++;

        // Flying with Infant
        gridPane.add(new Label("Flying with Infant:"), 0, row);
        flyingWithInfantCheckBox = new CheckBox();
        flyingWithInfantCheckBox.setOnAction(e -> updateNumberOfPassengers());
        gridPane.add(flyingWithInfantCheckBox, 1, row);
        row++;

        // Flying with Travel Insurance
        gridPane.add(new Label("Travel Insurance:"), 0, row);
        flyingWithTravelInsuranceCheckBox = new CheckBox();
        gridPane.add(flyingWithTravelInsuranceCheckBox, 1, row);
        row++;

        // Number of Passengers (read-only)
        gridPane.add(new Label("Number of Passengers:"), 0, row);
        numberOfPassengersField = new TextField("1");
        numberOfPassengersField.setEditable(false);
        gridPane.add(numberOfPassengersField, 1, row);

        return gridPane;
    }

    private HBox createButtonBox() {
        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setAlignment(Pos.TOP_RIGHT);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> handleSaveButton());

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> handleCancelButton());

        buttonBox.getChildren().addAll(saveButton, cancelButton);

        return buttonBox;
    }

    private void updateUI() {
        flightDesignatorField.setText(seatReservation.getFlightDesignator());
        flightDatePicker.setValue(seatReservation.getFlightDate());
        firstNameField.setText(seatReservation.getFirstName());
        lastNameField.setText(seatReservation.getLastName());
        numberOfBagsField.setText(String.valueOf(seatReservation.getNumberOfBags()));
        flyingWithInfantCheckBox.setSelected(seatReservation.isFlyingWithInfant());
        flyingWithTravelInsuranceCheckBox.setSelected(seatReservation.hasTravelInsurance());
        updateNumberOfPassengers();
    }

    private void updateNumberOfPassengers() {
        if (flyingWithInfantCheckBox.isSelected()) {
            numberOfPassengersField.setText("2");
        } else {
            numberOfPassengersField.setText("1");
        }
    }

    private void handleSaveButton() {
        try {
            // Update the seatReservation object with values from the GUI
            seatReservation.setFlightDesignator(flightDesignatorField.getText());
            seatReservation.setFlightDate(flightDatePicker.getValue());
            seatReservation.setFirstName(firstNameField.getText());
            seatReservation.setLastName(lastNameField.getText());
            seatReservation.setNumberOfBags(Integer.parseInt(numberOfBagsField.getText()));

            if (flyingWithInfantCheckBox.isSelected()) {
                seatReservation.makeFlyingWithInfant();
            } else {
                seatReservation.makeNotFlyingWithInfant();
            }

            if (flyingWithTravelInsuranceCheckBox.isSelected()) {
                seatReservation.makeFlyingWithTravelInsurance();
            } else {
                seatReservation.makeNotFlyingWithTravelInsurance();
            }

            // Print the object to console
            System.out.println(seatReservation);

            // Exit the application
            Platform.exit();

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleCancelButton() {
        System.out.println("Cancel clicked");
        Platform.exit();
    }
}