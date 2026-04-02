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

    private String ident;           // Unique identifier
    private String type;            // Airport type (small_airport, large_airport, etc)
    private String name;            // Full name
    private Integer elevationFt;    // Can be null, so use Integer not int
    private String continent;       // Continental code
    private String isoCountry;      // Country code (ISO 3166-1 alpha-2)
    private String isoRegion;       // Region/state code
    private String municipality;    // City name
    private String gpsCode;         // GPS code (can be null)
    private String iataCode;        // IATA 3-letter code (can be null)
    private String localCode;       // Local code (can be null)
    private Double latitude;        // From coordinates
    private Double longitude;       // From coordinates

    @Override
    public String toString() {
        return "Airport{" +
                "ident='" + ident + '\'' +
                ", name='" + name + '\'' +
                ", iataCode='" + iataCode + '\'' +
                '}';
    }
}
