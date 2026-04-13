/*
 * Project: Module 4 - Airline Flight Designator Application
 * Author: Nicole Tressler
 * Email: Nit0005@auburn.edu
 * Date: April 7 2026
 * Description: Database class for managing airline scheduled flights
 */

package com.airline;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

public class FlightScheduleController implements Initializable {
    private AirlineDatabase database;

    // Table components
    @FXML
    private TableView<ScheduledFlight> flightTable;
    @FXML
    private TableColumn<ScheduledFlight, String> designatorColumn;
    @FXML
    private TableColumn<ScheduledFlight, String> depAirportColumn;
    @FXML
    private TableColumn<ScheduledFlight, String> arrAirportColumn;
    @FXML
    private TableColumn<ScheduledFlight, String> depTimeColumn;
    @FXML
    private TableColumn<ScheduledFlight, String> arrTimeColumn;
    @FXML
    private TableColumn<ScheduledFlight, String> daysColumn;

    // Editor components
    @FXML
    private TextField flightDesignatorField;
    @FXML
    private TextField departureAirportField;
    @FXML
    private TextField departureTimeField;
    @FXML
    private TextField arrivalAirportField;
    @FXML
    private TextField arrivalTimeField;

    // Day buttons
    @FXML
    private ToggleButton mondayButton;
    @FXML
    private ToggleButton tuesdayButton;
    @FXML
    private ToggleButton wednesdayButton;
    @FXML
    private ToggleButton thursdayButton;
    @FXML
    private ToggleButton fridayButton;
    @FXML
    private ToggleButton saturdayButton;
    @FXML
    private ToggleButton sundayButton;

    // Action buttons
    @FXML
    private Button addUpdateButton;
    @FXML
    private Button newButton;
    @FXML
    private Button deleteButton;

    // State
    private ScheduledFlight currentFlight;
    private Map<DayOfWeek, ToggleButton> dayButtons;
    private static final String DATABASE_FILE = "flights.db";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize day buttons map
        dayButtons = new HashMap<>();
        dayButtons.put(DayOfWeek.MONDAY, mondayButton);
        dayButtons.put(DayOfWeek.TUESDAY, tuesdayButton);
        dayButtons.put(DayOfWeek.WEDNESDAY, wednesdayButton);
        dayButtons.put(DayOfWeek.THURSDAY, thursdayButton);
        dayButtons.put(DayOfWeek.FRIDAY, fridayButton);
        dayButtons.put(DayOfWeek.SATURDAY, saturdayButton);
        dayButtons.put(DayOfWeek.SUNDAY, sundayButton);

        // Load database
        loadDatabase();

        // Configure table columns
        setupTableColumns();

        // Set table data
        flightTable.setItems(database.getScheduledFlights());

        // Handle table selection
        flightTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadFlight(newVal);
            }
        });
    }

    private void setupTableColumns() {
        // Flight Designator
        designatorColumn.setCellValueFactory(new PropertyValueFactory<>("flightDesignator"));

        // Departure Airport
        depAirportColumn.setCellValueFactory(new PropertyValueFactory<>("departureAirportIdent"));

        // Arrival Airport
        arrAirportColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalAirportIdent"));

        // Departure Time
        depTimeColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getDepartureTime().toString()
                )
        );

        // Arrival Time
        arrTimeColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getArrivalTime().toString()
                )
        );

        // Days of Week
        daysColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getDaysOfWeekAsString()
                )
        );
    }

    private void loadDatabase() {
        try {
            database = AirlineDatabaseIO.loadFromFile(DATABASE_FILE);
        } catch (IOException e) {
            // Database file doesn't exist yet, create a new one
            database = new AirlineDatabase();
        }
    }

    public void loadFlight(ScheduledFlight flight) {
        currentFlight = flight;

        // Populate fields
        flightDesignatorField.setText(flight.getFlightDesignator());
        departureAirportField.setText(flight.getDepartureAirportIdent());
        departureTimeField.setText(flight.getDepartureTime().toString());
        arrivalAirportField.setText(flight.getArrivalAirportIdent());
        arrivalTimeField.setText(flight.getArrivalTime().toString());

        // IMPORTANT: Clear all buttons FIRST before setting selected ones
        for (ToggleButton btn : dayButtons.values()) {
            btn.setSelected(false);
        }

        // Then set only the days that this flight has
        for (DayOfWeek day : flight.getDaysOfWeek()) {
            ToggleButton btn = dayButtons.get(day);
            if (btn != null) {
                btn.setSelected(true);
            }
        }

        // Change button to Update
        addUpdateButton.setText("Update");
    }

    public void clearForm() {
        currentFlight = null;
        flightDesignatorField.clear();
        departureAirportField.clear();
        departureTimeField.clear();
        arrivalAirportField.clear();
        arrivalTimeField.clear();

        // Clear all day selections
        for (ToggleButton btn : dayButtons.values()) {
            btn.setSelected(false);
        }

        // Reset button to Add
        addUpdateButton.setText("Add");
    }

    @FXML
    public void handleAddUpdate() {
        try {
            // Validate inputs
            if (flightDesignatorField.getText().trim().isEmpty()) {
                showError("Flight designator cannot be empty");
                return;
            }
            if (departureAirportField.getText().trim().isEmpty()) {
                showError("Departure airport cannot be empty");
                return;
            }
            if (arrivalAirportField.getText().trim().isEmpty()) {
                showError("Arrival airport cannot be empty");
                return;
            }

            String flightDesignator = flightDesignatorField.getText().trim();
            String departureAirport = departureAirportField.getText().trim();
            LocalTime departureTime = LocalTime.parse(departureTimeField.getText().trim());
            String arrivalAirport = arrivalAirportField.getText().trim();
            LocalTime arrivalTime = LocalTime.parse(arrivalTimeField.getText().trim());

            // Get selected days - ONLY include days where button is selected
            Set<DayOfWeek> daysOfWeek = new HashSet<>();
            if (mondayButton.isSelected()) daysOfWeek.add(DayOfWeek.MONDAY);
            if (tuesdayButton.isSelected()) daysOfWeek.add(DayOfWeek.TUESDAY);
            if (wednesdayButton.isSelected()) daysOfWeek.add(DayOfWeek.WEDNESDAY);
            if (thursdayButton.isSelected()) daysOfWeek.add(DayOfWeek.THURSDAY);
            if (fridayButton.isSelected()) daysOfWeek.add(DayOfWeek.FRIDAY);
            if (saturdayButton.isSelected()) daysOfWeek.add(DayOfWeek.SATURDAY);
            if (sundayButton.isSelected()) daysOfWeek.add(DayOfWeek.SUNDAY);

            if (daysOfWeek.isEmpty()) {
                showError("Please select at least one day of operation");
                return;
            }

            ScheduledFlight flight = new ScheduledFlight(
                    flightDesignator,
                    departureAirport,
                    departureTime,
                    arrivalAirport,
                    arrivalTime,
                    daysOfWeek
            );

            if (currentFlight == null) {
                // Add new flight
                database.addScheduledFlight(flight);
            } else {
                // Update existing flight
                int index = database.getScheduledFlights().indexOf(currentFlight);
                if (index >= 0) {
                    database.getScheduledFlights().set(index, flight);
                }
                currentFlight = null;
            }

            saveDatabase();
            clearForm();
            flightTable.refresh();

        } catch (Exception e) {
            showError("Invalid input: " + e.getMessage());
        }
    }

    @FXML
    public void handleNew() {
        clearForm();
        flightTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleDelete() {
        if (currentFlight != null) {
            database.removeScheduledFlight(currentFlight);
            try {
                saveDatabase();
            } catch (IOException e) {
                showError("Error saving database: " + e.getMessage());
            }
            clearForm();
            flightTable.refresh();
        } else {
            showError("Please select a flight to delete");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void saveDatabase() throws IOException {
        AirlineDatabaseIO.saveToFile(database, DATABASE_FILE);
    }

    public void setDatabase(AirlineDatabase db) {
        this.database = db;
        if (flightTable != null) {
            flightTable.setItems(database.getScheduledFlights());
        }
    }
}