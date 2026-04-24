/*
 * Project: Module 4 - Airline Flight Designator Application
 * Author: Nicole Tressler
 * Email: Nit0005@auburn.edu
 * Date: April 7 2026
 * Description: Database class for managing airline scheduled flights
 */

package com.airline;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AirlineDatabase {
    private final ObservableList<ScheduledFlight> scheduledFlights;

    public AirlineDatabase() {
        this.scheduledFlights = FXCollections.observableArrayList();
    }

    /**
     * Get all scheduled flights
     * @return ObservableList of scheduled flights
     */
    public ObservableList<ScheduledFlight> getScheduledFlights() {
        return scheduledFlights;
    }

    /**
     * Add a new scheduled flight to the database
     * @param sf the ScheduledFlight to add
     */
    public void addScheduledFlight(ScheduledFlight sf) {
        if (sf == null) {
            throw new IllegalArgumentException("ScheduledFlight cannot be null");
        }
        if (!scheduledFlights.contains(sf)) {
            scheduledFlights.add(sf);
        }
    }

    /**
     * Remove a scheduled flight from the database
     * @param sf the ScheduledFlight to remove
     */
    public void removeScheduledFlight(ScheduledFlight sf) {
        if (sf == null) {
            throw new IllegalArgumentException("ScheduledFlight cannot be null");
        }
        scheduledFlights.remove(sf);
    }

    /**
     * Update an existing scheduled flight in the database
     * @param sf the ScheduledFlight to update
     */
    public void updateScheduledFlight(ScheduledFlight sf) {
        if (sf == null) {
            throw new IllegalArgumentException("ScheduledFlight cannot be null");
        }
        // Find and replace the flight with matching designator
        for (int i = 0; i < scheduledFlights.size(); i++) {
            if (scheduledFlights.get(i).getFlightDesignator().equals(sf.getFlightDesignator())) {
                scheduledFlights.set(i, sf);
                return;
            }
        }
        // If not found, add it as new
        addScheduledFlight(sf);
    }
}