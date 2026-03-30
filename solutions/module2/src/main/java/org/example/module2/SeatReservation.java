/*
* Project: Module 2
* Author: Nicole Tressler
* Email: nit0005@auburn.edu
* Date: 2026 - 03 - 29
* Description: Flight reservation app
 */


package org.example.module2;


public class SeatReservation {

    private String flightDesignator;
    private java.time.LocalDate flightDate;
    private String firstName;
    private String lastName;

    // New fields
    private int numberOfBags = 0;
    private boolean flyingWithInfant = false;
    private boolean travelInsurance = false;

    public String getFlightDesignator() {
        return flightDesignator;
    }

    public void setFlightDesignator(String fd) {
        if (fd == null || fd.length() < 4 || fd.length() > 6) {
            throw new IllegalArgumentException("Flight designator must be between 4 and 6 characters");
        }
        this.flightDesignator = fd;
    }

    public java.time.LocalDate getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(java.time.LocalDate date) {
        flightDate = date;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String fn) {
        firstName = fn;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String ln) {
        lastName = ln;
    }

    // New getters/setters and helpers for numberOfBags, infant, and insurance

    public int getNumberOfBags() {
        return numberOfBags;
    }

    public void setNumberOfBags(int numberOfBags) {
        if (numberOfBags < 0) {
            throw new IllegalArgumentException("Number of bags cannot be negative");
        }
        this.numberOfBags = numberOfBags;
    }

    public boolean isFlyingWithInfant() {
        return flyingWithInfant;
    }

    public void makeFlyingWithInfant() {
        this.flyingWithInfant = true;
    }

    public void makeNotFlyingWithInfant() {
        this.flyingWithInfant = false;
    }

    public boolean hasTravelInsurance() {
        return travelInsurance;
    }

    public void makeFlyingWithTravelInsurance() {
        this.travelInsurance = true;
    }

    public void makeNotFlyingWithTravelInsurance() {
        this.travelInsurance = false;
    }

    @Override
    public String toString() {
        return "SeatReservation{" +
                "flightDesignator=" + (flightDesignator == null ? "null" : flightDesignator) +
                ", flightDate=" + (flightDate == null ? "null" : flightDate) +
                ", firstName=" + (firstName == null ? "null" : firstName) +
                ", lastName=" + (lastName == null ? "null" : lastName) +
                ", numberOfBags=" + numberOfBags +
                ", flyingWithInfant=" + flyingWithInfant +
                ", travelInsurance=" + travelInsurance +
                '}';
    }
}
