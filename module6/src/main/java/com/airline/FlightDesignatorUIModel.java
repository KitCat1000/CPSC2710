/*
 * Project: Module 6 Part 2 - Airline Flight Designator Application with UI Model
 * Author: Nicole Tressler
 * Email: Nit0005@auburn.edu
 * Date: April 22, 2026
 * Description: UI Model class that manages validation state and button enable/disable logic
 *              for the Flight Designator application.
 */

package com.airline;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.scene.paint.Color;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;

/**
 * UI Model for managing the state and validation of flight data in the UI.
 *
 * This class wraps a ScheduledFlight and adds:
 * - Individual validation properties for each field
 * - State tracking (isNew, isModified)
 * - Button enable/disable logic based on model state
 * - Real-time validation feedback
 */
public class FlightDesignatorUIModel {


    // UI Input Properties (wrappable fields)
    private final StringProperty flightDesignatorProperty = new SimpleStringProperty("");
    private final StringProperty departureAirportProperty = new SimpleStringProperty("");
    private final StringProperty departureTimeProperty = new SimpleStringProperty("");
    private final StringProperty arrivalAirportProperty = new SimpleStringProperty("");
    private final StringProperty arrivalTimeProperty = new SimpleStringProperty("");


    // Validation Properties (for each field)
    private final BooleanProperty flightDesignatorValidProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty departureAirportValidProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty departureTimeValidProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty arrivalAirportValidProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty arrivalTimeValidProperty = new SimpleBooleanProperty(false);


    // Computed Validation Properties
    private final BooleanProperty allFieldsValidProperty;
    private final BooleanProperty allFieldsEmptyProperty;
    private final BooleanProperty anyFieldsFilledProperty;


    // State Properties
    private final BooleanProperty isNewProperty = new SimpleBooleanProperty(true);
    private final BooleanProperty isModifiedProperty = new SimpleBooleanProperty(false);


    // Button Enable/Disable Properties (computed)
    private final BooleanProperty addButtonEnabledProperty;
    private final BooleanProperty newButtonEnabledProperty;
    private final BooleanProperty deleteButtonEnabledProperty;
    private final BooleanProperty updateButtonEnabledProperty;


    // Visual Feedback Properties (for field highlighting)
    private final ObjectProperty<Color> flightDesignatorHighlightProperty = new SimpleObjectProperty<>(Color.WHITE);
    private final ObjectProperty<Color> departureAirportHighlightProperty = new SimpleObjectProperty<>(Color.WHITE);
    private final ObjectProperty<Color> departureTimeHighlightProperty = new SimpleObjectProperty<>(Color.WHITE);
    private final ObjectProperty<Color> arrivalAirportHighlightProperty = new SimpleObjectProperty<>(Color.WHITE);
    private final ObjectProperty<Color> arrivalTimeHighlightProperty = new SimpleObjectProperty<>(Color.WHITE);


    // Current flight being edited
    private ScheduledFlight currentFlight;

    // Time format constant
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final String INVALID_COLOR = "#FFB6C6";  // Light red
    private static final String VALID_COLOR = "WHITE";

    /**
     * Constructor - initializes all bindings and listeners
     */
    public FlightDesignatorUIModel() {
        // Set up validation listeners for all text fields
        setupValidationListeners();

        // Compute allFieldsValid property (all fields must be valid)
        javafx.beans.binding.BooleanBinding allValidBinding = Bindings.and(
                Bindings.and(
                        Bindings.and(flightDesignatorValidProperty, departureAirportValidProperty),
                        Bindings.and(departureTimeValidProperty, arrivalAirportValidProperty)
                ),
                arrivalTimeValidProperty
        );
        this.allFieldsValidProperty = new SimpleBooleanProperty();
        allFieldsValidProperty.bind(allValidBinding);

        // Compute allFieldsEmpty property (all fields are empty)
        javafx.beans.binding.BooleanBinding allEmptyBinding = Bindings.and(
                Bindings.and(
                        Bindings.and(
                                flightDesignatorProperty.isEmpty(),
                                departureAirportProperty.isEmpty()
                        ),
                        Bindings.and(
                                departureTimeProperty.isEmpty(),
                                arrivalAirportProperty.isEmpty()
                        )
                ),
                arrivalTimeProperty.isEmpty()
        );
        this.allFieldsEmptyProperty = new SimpleBooleanProperty();
        allFieldsEmptyProperty.bind(allEmptyBinding);

        // Compute anyFieldsFilled property (opposite of allFieldsEmpty)
        javafx.beans.binding.BooleanBinding anyFilledBinding = Bindings.not(allFieldsEmptyProperty);
        this.anyFieldsFilledProperty = new SimpleBooleanProperty();
        anyFieldsFilledProperty.bind(anyFilledBinding);


        // BUTTON ENABLE/DISABLE LOGIC
        // Add button: Always enabled
        // Update button: Enabled when editing (isNew=false) AND fields are valid AND modified
        this.addButtonEnabledProperty = new SimpleBooleanProperty(true);

        // New button: Enabled when NOT editing AND fields have content (something is selected)
        // Disabled when editing OR all fields are empty
        this.newButtonEnabledProperty = new SimpleBooleanProperty();
        newButtonEnabledProperty.bind(
                Bindings.and(isNewProperty.not(), anyFieldsFilledProperty)
        );

        // Delete button: Enabled only when editing (NOT creating new)
        // Disabled when all fields are empty (nothing selected)
        this.deleteButtonEnabledProperty = new SimpleBooleanProperty();
        deleteButtonEnabledProperty.bind(
                Bindings.and(isNewProperty.not(), anyFieldsFilledProperty)
        );

        // Update button: Only enabled when editing AND fields are valid AND modified
        this.updateButtonEnabledProperty = new SimpleBooleanProperty();
        updateButtonEnabledProperty.bind(
                Bindings.and(
                        Bindings.and(isNewProperty.not(), allFieldsValidProperty),
                        isModifiedProperty
                )
        );
    }

    /**
     * Set up validation listeners for each field.
     * As user types, the field is validated and highlighting is updated.
     */
    private void setupValidationListeners() {
        // Flight Designator validation
        flightDesignatorProperty.addListener((obs, oldVal, newVal) -> {
            boolean isValid = isValidFlightDesignator(newVal);
            flightDesignatorValidProperty.set(isValid);
            updateFieldHighlight(flightDesignatorHighlightProperty, isValid, newVal.isEmpty());
            updateModified();
        });

        // Departure Airport validation (non-empty, reasonable length)
        departureAirportProperty.addListener((obs, oldVal, newVal) -> {
            boolean isValid = isValidAirportCode(newVal);
            departureAirportValidProperty.set(isValid);
            updateFieldHighlight(departureAirportHighlightProperty, isValid, newVal.isEmpty());
            updateModified();
        });

        // Departure Time validation (valid time format)
        departureTimeProperty.addListener((obs, oldVal, newVal) -> {
            boolean isValid = isValidTimeFormat(newVal);
            departureTimeValidProperty.set(isValid);
            updateFieldHighlight(departureTimeHighlightProperty, isValid, newVal.isEmpty());
            updateModified();
        });

        // Arrival Airport validation
        arrivalAirportProperty.addListener((obs, oldVal, newVal) -> {
            boolean isValid = isValidAirportCode(newVal);
            arrivalAirportValidProperty.set(isValid);
            updateFieldHighlight(arrivalAirportHighlightProperty, isValid, newVal.isEmpty());
            updateModified();
        });

        // Arrival Time validation
        arrivalTimeProperty.addListener((obs, oldVal, newVal) -> {
            boolean isValid = isValidTimeFormat(newVal);
            arrivalTimeValidProperty.set(isValid);
            updateFieldHighlight(arrivalTimeHighlightProperty, isValid, newVal.isEmpty());
            updateModified();
        });
    }

    /**
     * Update field highlighting based on validity
     * - Valid and filled: WHITE
     * - Invalid and filled: LIGHT RED
     * - Empty: WHITE (don't show error for empty fields)
     */
    private void updateFieldHighlight(ObjectProperty<Color> highlightProperty, boolean isValid, boolean isEmpty) {
        if (isEmpty) {
            highlightProperty.set(Color.web(VALID_COLOR));
        } else if (isValid) {
            highlightProperty.set(Color.web(VALID_COLOR));
        } else {
            highlightProperty.set(Color.web(INVALID_COLOR));
        }
    }

    /**
     * Update the modified flag when user changes a field
     */
    private void updateModified() {
        if (currentFlight != null) {
            boolean modified = !flightDesignatorProperty.get().equals(currentFlight.getFlightDesignator())
                    || !departureAirportProperty.get().equals(currentFlight.getDepartureAirportIdent())
                    || !departureTimeProperty.get().equals(currentFlight.getDepartureTime().toString())
                    || !arrivalAirportProperty.get().equals(currentFlight.getArrivalAirportIdent())
                    || !arrivalTimeProperty.get().equals(currentFlight.getArrivalTime().toString());
            isModifiedProperty.set(modified);
        }
    }

    /**
     * Validate flight designator
     * - Must not be null/empty
     * - Common format: 2-3 letter airline code + 1-4 digit flight number
     */
    private boolean isValidFlightDesignator(String designator) {
        if (designator == null || designator.trim().isEmpty()) {
            return false;
        }
        // Format: 2-3 letters followed by 1-4 digits (e.g., DL1331, AA100)
        return designator.matches("[A-Z]{2,3}\\d{1,4}");
    }

    /**
     * Validate airport code
     * - Must not be null/empty
     * - Typically 3-4 letter codes (e.g., KPIT, KATL)
     */
    private boolean isValidAirportCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        // Airport codes are typically 3-4 characters
        return code.length() >= 3 && code.length() <= 4 && code.matches("[A-Z]+");
    }

    /**
     * Validate time format
     * - Must be in HH:MM:SS format
     */
    private boolean isValidTimeFormat(String timeStr) {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            return false;
        }
        try {
            LocalTime.parse(timeStr, TIME_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }


    // PUBLIC METHODS FOR CONTROLLER
    /**
     * Load a flight into the model (user selected an existing flight)
     */
    public void loadFlight(ScheduledFlight flight) {
        currentFlight = flight;
        isNewProperty.set(false);

        flightDesignatorProperty.set(flight.getFlightDesignator());
        departureAirportProperty.set(flight.getDepartureAirportIdent());
        departureTimeProperty.set(flight.getDepartureTime().toString());
        arrivalAirportProperty.set(flight.getArrivalAirportIdent());
        arrivalTimeProperty.set(flight.getArrivalTime().toString());

        isModifiedProperty.set(false);
    }

    /**
     * Clear the form (new flight or after saving)
     */
    public void clearForm() {
        currentFlight = null;
        isNewProperty.set(true);

        flightDesignatorProperty.set("");
        departureAirportProperty.set("");
        departureTimeProperty.set("");
        arrivalAirportProperty.set("");
        arrivalTimeProperty.set("");

        isModifiedProperty.set(false);

        // Reset all highlights to white
        flightDesignatorHighlightProperty.set(Color.WHITE);
        departureAirportHighlightProperty.set(Color.WHITE);
        departureTimeHighlightProperty.set(Color.WHITE);
        arrivalAirportHighlightProperty.set(Color.WHITE);
        arrivalTimeHighlightProperty.set(Color.WHITE);
    }

    /**
     * Get the current flight data as a ScheduledFlight object
     * Returns null if data is invalid
     */
    public ScheduledFlight buildScheduledFlight(java.util.Set<java.time.DayOfWeek> daysOfWeek) {
        if (!allFieldsValidProperty.get()) {
            return null;
        }

        try {
            return new ScheduledFlight(
                    flightDesignatorProperty.get(),
                    departureAirportProperty.get(),
                    LocalTime.parse(departureTimeProperty.get()),
                    arrivalAirportProperty.get(),
                    LocalTime.parse(arrivalTimeProperty.get()),
                    daysOfWeek
            );
        } catch (Exception e) {
            return null;
        }
    }


    // PROPERTY GETTERS (for controller binding)
    // Input field properties
    public StringProperty flightDesignatorProperty() { return flightDesignatorProperty; }
    public StringProperty departureAirportProperty() { return departureAirportProperty; }
    public StringProperty departureTimeProperty() { return departureTimeProperty; }
    public StringProperty arrivalAirportProperty() { return arrivalAirportProperty; }
    public StringProperty arrivalTimeProperty() { return arrivalTimeProperty; }

    // Validation properties
    public BooleanProperty flightDesignatorValidProperty() { return flightDesignatorValidProperty; }
    public BooleanProperty departureAirportValidProperty() { return departureAirportValidProperty; }
    public BooleanProperty departureTimeValidProperty() { return departureTimeValidProperty; }
    public BooleanProperty arrivalAirportValidProperty() { return arrivalAirportValidProperty; }
    public BooleanProperty arrivalTimeValidProperty() { return arrivalTimeValidProperty; }
    public BooleanProperty allFieldsValidProperty() { return allFieldsValidProperty; }

    // State properties
    public BooleanProperty isNewProperty() { return isNewProperty; }
    public BooleanProperty isModifiedProperty() { return isModifiedProperty; }

    // Button enable/disable properties
    public BooleanProperty addButtonEnabledProperty() { return addButtonEnabledProperty; }
    public BooleanProperty newButtonEnabledProperty() { return newButtonEnabledProperty; }
    public BooleanProperty deleteButtonEnabledProperty() { return deleteButtonEnabledProperty; }
    public BooleanProperty updateButtonEnabledProperty() { return updateButtonEnabledProperty; }

    // Highlight properties
    public ObjectProperty<Color> flightDesignatorHighlightProperty() { return flightDesignatorHighlightProperty; }
    public ObjectProperty<Color> departureAirportHighlightProperty() { return departureAirportHighlightProperty; }
    public ObjectProperty<Color> departureTimeHighlightProperty() { return departureTimeHighlightProperty; }
    public ObjectProperty<Color> arrivalAirportHighlightProperty() { return arrivalAirportHighlightProperty; }
    public ObjectProperty<Color> arrivalTimeHighlightProperty() { return arrivalTimeHighlightProperty; }

    // Convenience getters
    public String getFlightDesignator() { return flightDesignatorProperty.get(); }
    public String getDepartureAirport() { return departureAirportProperty.get(); }
    public String getDepartureTime() { return departureTimeProperty.get(); }
    public String getArrivalAirport() { return arrivalAirportProperty.get(); }
    public String getArrivalTime() { return arrivalTimeProperty.get(); }

    public boolean isNew() { return isNewProperty.get(); }
    public boolean isModified() { return isModifiedProperty.get(); }
}