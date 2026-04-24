/*
 * Project: Module 6 Part 2 - Airline Flight Designator Application with UI Model
 * Author: Nicole Tressler
 * Email: Nit0005@auburn.edu
 * Date: April 22, 2026
 * Description: Enhanced controller that uses the FlightDesignatorUIModel to manage
 *              validation state and button enable/disable logic. All three buttons
 *              (Add/Update, New, Delete) work correctly based on model state.
 */

package com.airline;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.util.*;

public class FlightScheduleController implements Initializable {
    private AirlineDatabase database;
    private FlightDesignatorUIModel uiModel;

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
        // Initialize UI Model
        uiModel = new FlightDesignatorUIModel();

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

        // Bind text fields to UI Model properties
        flightDesignatorField.textProperty().bindBidirectional(uiModel.flightDesignatorProperty());
        departureAirportField.textProperty().bindBidirectional(uiModel.departureAirportProperty());
        departureTimeField.textProperty().bindBidirectional(uiModel.departureTimeProperty());
        arrivalAirportField.textProperty().bindBidirectional(uiModel.arrivalAirportProperty());
        arrivalTimeField.textProperty().bindBidirectional(uiModel.arrivalTimeProperty());

        // Bind field background colors to validation state (red for invalid, white for valid)
        bindFieldValidation(flightDesignatorField, uiModel.flightDesignatorHighlightProperty());
        bindFieldValidation(departureAirportField, uiModel.departureAirportHighlightProperty());
        bindFieldValidation(departureTimeField, uiModel.departureTimeHighlightProperty());
        bindFieldValidation(arrivalAirportField, uiModel.arrivalAirportHighlightProperty());
        bindFieldValidation(arrivalTimeField, uiModel.arrivalTimeHighlightProperty());

        // Bind button enable/disable to UI Model properties
        // Add/Update button: always enabled (shows validation feedback in fields)
        addUpdateButton.disableProperty().bind(
                uiModel.isNewProperty().not().and(uiModel.addButtonEnabledProperty().not())
        );

        // New button: enabled when editing AND fields have content
        newButton.disableProperty().bind(uiModel.newButtonEnabledProperty().not());

        // Delete button: enabled only when editing AND something is selected
        deleteButton.disableProperty().bind(uiModel.deleteButtonEnabledProperty().not());

        // Handle table selection
        flightTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadFlight(newVal);
            }
        });
    }

    /**
     * Bind a text field's background color to a color property for validation feedback
     */
    private void bindFieldValidation(TextField field, javafx.beans.property.ObjectProperty<Color> colorProperty) {
        colorProperty.addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String colorHex = String.format("#%02X%02X%02X",
                        (int) (newVal.getRed() * 255),
                        (int) (newVal.getGreen() * 255),
                        (int) (newVal.getBlue() * 255));
                field.setStyle("-fx-control-inner-background: " + colorHex + ";");
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
        uiModel.loadFlight(flight);

        // Load day selections
        for (ToggleButton btn : dayButtons.values()) {
            btn.setSelected(false);
        }
        for (DayOfWeek day : flight.getDaysOfWeek()) {
            ToggleButton btn = dayButtons.get(day);
            if (btn != null) {
                btn.setSelected(true);
            }
        }

        // Update button text based on state
        if (uiModel.isNew()) {
            addUpdateButton.setText("Add");
        } else {
            addUpdateButton.setText("Update");
        }
    }

    public void clearForm() {
        currentFlight = null;
        uiModel.clearForm();

        // Clear day selections
        for (ToggleButton btn : dayButtons.values()) {
            btn.setSelected(false);
        }

        // Reset button to Add
        addUpdateButton.setText("Add");
    }

    /**
     * Handle Add/Update button action
     * - If creating new: Add button must have valid input and days selected
     * - If updating: Update button must have valid input, be modified, and days selected
     */
    @FXML
    public void handleAddUpdate() {
        try {
            // Get selected days
            Set<DayOfWeek> daysOfWeek = new HashSet<>();
            if (mondayButton.isSelected()) daysOfWeek.add(DayOfWeek.MONDAY);
            if (tuesdayButton.isSelected()) daysOfWeek.add(DayOfWeek.TUESDAY);
            if (wednesdayButton.isSelected()) daysOfWeek.add(DayOfWeek.WEDNESDAY);
            if (thursdayButton.isSelected()) daysOfWeek.add(DayOfWeek.THURSDAY);
            if (fridayButton.isSelected()) daysOfWeek.add(DayOfWeek.FRIDAY);
            if (saturdayButton.isSelected()) daysOfWeek.add(DayOfWeek.SATURDAY);
            if (sundayButton.isSelected()) daysOfWeek.add(DayOfWeek.SUNDAY);

            // Validate all inputs
            if (!uiModel.allFieldsValidProperty().get()) {
                showError("All fields must be filled with valid data");
                return;
            }

            if (daysOfWeek.isEmpty()) {
                showError("Please select at least one day of operation");
                return;
            }

            // Build the flight object from UI Model
            ScheduledFlight flight = uiModel.buildScheduledFlight(daysOfWeek);
            if (flight == null) {
                showError("Invalid input: Check all fields");
                return;
            }

            // Perform add or update
            if (currentFlight == null) {
                // Adding new flight
                database.addScheduledFlight(flight);
            } else {
                // Updating existing flight
                int index = database.getScheduledFlights().indexOf(currentFlight);
                if (index >= 0) {
                    database.getScheduledFlights().set(index, flight);
                }
            }

            saveDatabase();
            clearForm();
            flightTable.refresh();
            flightTable.getSelectionModel().clearSelection();

        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }

    /**
     * Handle New button action
     * - Clears the form and deselects any table selection
     * - Only enabled when editing (not creating new)
     */
    @FXML
    public void handleNew() {
        clearForm();
        flightTable.getSelectionModel().clearSelection();
    }

    /**
     * Handle Delete button action
     * - Deletes the currently selected flight
     * - Only enabled when a flight is selected (editing)
     */
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
            flightTable.getSelectionModel().clearSelection();
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