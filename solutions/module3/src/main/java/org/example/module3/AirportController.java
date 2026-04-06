/*
 * Module 3 Assignment - Airport Information Display
 * Author: Nicole Tressler
 * Auburn Email: nit0005@auburn.edu
 * Date: 2026-04-05
 * Description: Controller class that handles user interactions and updates the UI
 *              based on airport searches and map displays.
 */

package org.example.module3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;

public class AirportController {

    //FXML Injected Fields
    // Search Fields
    @FXML
    private TextField identField;

    @FXML
    private TextField iataCodeField;

    @FXML
    private TextField localCodeField;

    // Display Fields (Read-only)
    @FXML
    private TextField typeField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField elevationField;

    @FXML
    private TextField countryField;

    @FXML
    private TextField regionField;

    @FXML
    private TextField municipalityField;

    // Map
    @FXML
    private Label mapView;

    // Button
    @FXML
    private Button searchButton;

    //Data Fields
    private List<Airport> airports;

    //Initialization

    /**
     * Called automatically when the FXML file loads.
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        try {
            // Load all airports from the CSV file
            airports = Airport.readAll();
            System.out.println("Loaded " + airports.size() + " airports");

            // Set up event listeners for user interactions
            setupSearchFieldListeners();
            setupSearchButtonListener();

            // Initialize the map with a default message
            mapView.setText("🗺️ Map will display here - enter airport code and search");
        } catch (IOException e) {
            System.err.println("Error initializing controller: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error in initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Event Listeners

    /**
     * When the user presses Enter in any search field, perform a search.
     */
    private void setupSearchFieldListeners() {
        identField.setOnAction(e -> searchAirport());
        iataCodeField.setOnAction(e -> searchAirport());
        localCodeField.setOnAction(e -> searchAirport());
    }

    /**
     * When the user clicks the Search button, perform a search.
     */
    private void setupSearchButtonListener() {
        searchButton.setOnAction(e -> searchAirport());
    }

    //Search Logic

    /**
     * Searches for an airport based on the first non-blank search field.
     * Priority: ident > iataCode > localCode
     */
    private void searchAirport() {
        String searchTerm = null;
        String searchType = null;

        // Determine which field to search by (priority order)
        if (!identField.getText().trim().isEmpty()) {
            searchTerm = identField.getText().trim();
            searchType = "ident";
        } else if (!iataCodeField.getText().trim().isEmpty()) {
            searchTerm = iataCodeField.getText().trim();
            searchType = "iataCode";
        } else if (!localCodeField.getText().trim().isEmpty()) {
            searchTerm = localCodeField.getText().trim();
            searchType = "localCode";
        }

        // If all fields are empty, clear the display
        if (searchTerm == null) {
            clearAirportFields();
            mapView.setText("Please enter an airport code");
            return;
        }

        // Search for the airport in our list
        Airport foundAirport = null;
        for (Airport airport : airports) {
            if (searchType.equals("ident") && airport.getIdent().equalsIgnoreCase(searchTerm)) {
                foundAirport = airport;
                break;
            } else if (searchType.equals("iataCode") && airport.getIataCode() != null
                    && airport.getIataCode().equalsIgnoreCase(searchTerm)) {
                foundAirport = airport;
                break;
            } else if (searchType.equals("localCode") && airport.getLocalCode() != null
                    && airport.getLocalCode().equalsIgnoreCase(searchTerm)) {
                foundAirport = airport;
                break;
            }
        }

        // Display the airport or show an error
        if (foundAirport != null) {
            displayAirport(foundAirport);
        } else {
            clearAirportFields();
            mapView.setText("Airport not found: " + searchTerm);
            System.err.println("Airport not found: " + searchTerm);
        }
    }

    /**
     * Displays airport information in the read-only text fields.
     * Also updates the map to show the airport location.
     */
    private void displayAirport(Airport airport) {
        // Set text fields
        typeField.setText(airport.getType() != null ? airport.getType() : "");
        nameField.setText(airport.getName() != null ? airport.getName() : "");
        elevationField.setText(airport.getElevationFt() != null ?
                airport.getElevationFt().toString() + " ft" : "");
        countryField.setText(airport.getIsoCountry() != null ? airport.getIsoCountry() : "");
        regionField.setText(airport.getIsoRegion() != null ? airport.getIsoRegion() : "");
        municipalityField.setText(airport.getMunicipality() != null ?
                airport.getMunicipality() : "");

        // Update map if we have coordinates
        if (airport.getLatitude() != null && airport.getLongitude() != null) {
            loadMapView(airport.getLatitude(), airport.getLongitude());
        } else {
            mapView.setText("No coordinates available for: " + airport.getName());
        }
    }

    /**
     * Clears all display fields.
     */
    private void clearAirportFields() {
        typeField.clear();
        nameField.clear();
        elevationField.clear();
        countryField.clear();
        regionField.clear();
        municipalityField.clear();
    }

    //Map Display

    /**
     * Loads a map view display with coordinates.
     * Shows the Windy.com URL that would be used to display the map.
     *
     * @param latitude The latitude of the airport
     * @param longitude The longitude of the airport
     */
    private void loadMapView(double latitude, double longitude) {
        int zoomLevel = 12;
        String url = String.format("https://www.windy.com/?%.6f,%.6f,%d",
                latitude, longitude, zoomLevel);

        // Format the map display text with coordinates first, then URL
        String mapText = String.format(
                "📍 AIRPORT MAP LOCATION\n\n" +
                        "Latitude:  %.6f\n" +
                        "Longitude: %.6f\n" +
                        "Zoom Level: %d\n\n" +
                        "Windy.com Weather Map:\n%s",
                latitude, longitude, zoomLevel, url);

        mapView.setText(mapText);
        System.out.println("Map URL: " + url);
    }
}