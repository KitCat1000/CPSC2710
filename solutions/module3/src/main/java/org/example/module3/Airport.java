/*
 * Module 3 Assignment - Airport Information Display
 * Author: Nicole Tressler
 * Auburn Email: nit0005@auburn.edu
 * Date: 2026 - 4 - 01
 * Description: This class represents an airport and loads airport data from a CSV file.
 *              It provides methods to search for airports by various identifiers and
 *              display their information including location on a map.
 */

package org.example.module3;

import java.io.*;
import java.util.*;

public class Airport {

    private String ident;
    private String type;            // Airport type
    private String name;            // Full name
    private Integer elevationFt;    // Can be null, so use Integer not int
    private String continent;       // Continental code
    private String isoCountry;      // Country code
    private String isoRegion;       // Region/state code
    private String municipality;    // City name
    private String gpsCode;         // GPS code (can be null)
    private String iataCode;        // IATA 3-letter code (can be null)
    private String localCode;       // Local code (can be null)
    private Double latitude;        // From coordinates
    private Double longitude;       // From coordinates


    /**
     * No-argument constructor for creating empty Airport objects
     */
    public Airport() {
    }

    /**
     * Constructor with all parameters
     */
    public Airport(String ident, String type, String name, Integer elevationFt,
                   String continent, String isoCountry, String isoRegion,
                   String municipality, String gpsCode, String iataCode,
                   String localCode, Double latitude, Double longitude) {
        this.ident = ident;
        this.type = type;
        this.name = name;
        this.elevationFt = elevationFt;
        this.continent = continent;
        this.isoCountry = isoCountry;
        this.isoRegion = isoRegion;
        this.municipality = municipality;
        this.gpsCode = gpsCode;
        this.iataCode = iataCode;
        this.localCode = localCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }




    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getElevationFt() {
        return elevationFt;
    }

    public void setElevationFt(Integer elevationFt) {
        this.elevationFt = elevationFt;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getIsoCountry() {
        return isoCountry;
    }

    public void setIsoCountry(String isoCountry) {
        this.isoCountry = isoCountry;
    }

    public String getIsoRegion() {
        return isoRegion;
    }

    public void setIsoRegion(String isoRegion) {
        this.isoRegion = isoRegion;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getGpsCode() {
        return gpsCode;
    }

    public void setGpsCode(String gpsCode) {
        this.gpsCode = gpsCode;
    }

    public String getIataCode() {
        return iataCode;
    }

    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    public String getLocalCode() {
        return localCode;
    }

    public void setLocalCode(String localCode) {
        this.localCode = localCode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


    /**
     * Reads all airports from the CSV file and returns them as a List.
     * The CSV file is expected to be in src/main/resources/data/airports.csv
     *
     * @return A List of Airport objects loaded from the CSV file
     * @throws IOException if the file cannot be read
     */
    public static List<Airport> readAll() throws IOException {
        List<Airport> airports = new ArrayList<>();

        // Load the CSV file from resources
        InputStream inputStream = Airport.class.getResourceAsStream("/data/airports.csv");
        if (inputStream == null) {
            throw new IOException("airports.csv not found in resources");
        }

        // Use try-with-resources to ensure the reader is closed
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                // Skip the header line
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // Parse the CSV line
                Airport airport = parseAirportFromCSV(line);
                if (airport != null) {
                    airports.add(airport);
                }
            }
        }

        return airports;
    }

    /**
     * Helper method to parse a single CSV line into an Airport object.
     *
     * CSV format:
     * ident,type,name,elevation_ft,continent,iso_country,iso_region,municipality,
     * gps_code,iata_code,local_code,coordinates
     *
     * @param line A single line from the CSV file
     * @return An Airport object, or null if parsing fails
     */
    private static Airport parseAirportFromCSV(String line) {
        try {
            // Split by comma, but handle quoted fields
            String[] fields = parseCSVLine(line);

            if (fields.length < 12) {
                return null; // Invalid line
            }

            Airport airport = new Airport();

            airport.setIdent(fields[0].trim());
            airport.setType(fields[1].trim());
            airport.setName(fields[2].trim());

            // Handle elevation (can be empty)
            if (!fields[3].trim().isEmpty()) {
                try {
                    airport.setElevationFt(Integer.parseInt(fields[3].trim()));
                } catch (NumberFormatException e) {
                    airport.setElevationFt(null);
                }
            }

            airport.setContinent(fields[4].trim());
            airport.setIsoCountry(fields[5].trim());
            airport.setIsoRegion(fields[6].trim());
            airport.setMunicipality(fields[7].trim());
            airport.setGpsCode(fields[8].trim().isEmpty() ? null : fields[8].trim());
            airport.setIataCode(fields[9].trim().isEmpty() ? null : fields[9].trim());
            airport.setLocalCode(fields[10].trim().isEmpty() ? null : fields[10].trim());

            // Parse coordinates (format: "[-125.243652, 49.969300]")
            String coordinates = fields[11].trim();
            if (!coordinates.isEmpty()) {
                parseCoordinates(airport, coordinates);
            }

            return airport;
        } catch (Exception e) {
            System.err.println("Error parsing line: " + line);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Helper method to properly parse a CSV line, handling quoted fields.
     */
    private static String[] parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString());

        return fields.toArray(new String[0]);
    }

    /**
     * Helper method to parse latitude and longitude from the coordinates field.
     * Format: "[-125.243652, 49.969300]"
     * Note: CSV format has longitude first, then latitude
     */
    private static void parseCoordinates(Airport airport, String coordinates) {
        try {
            // Remove brackets and whitespace
            String clean = coordinates.replace("[", "").replace("]", "").trim();
            String[] parts = clean.split(",");

            if (parts.length == 2) {
                // CSV format: longitude first, then latitude
                airport.setLongitude(Double.parseDouble(parts[0].trim()));
                airport.setLatitude(Double.parseDouble(parts[1].trim()));
            }
        } catch (Exception e) {
            System.err.println("Error parsing coordinates: " + coordinates);
        }
    }

    
    @Override
    public String toString() {
        return "Airport{" +
                "ident='" + ident + '\'' +
                ", name='" + name + '\'' +
                ", iataCode='" + iataCode + '\'' +
                '}';
    }
}
