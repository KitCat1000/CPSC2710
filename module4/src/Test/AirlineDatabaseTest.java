/*
 * Project: Module 4 - Airline Flight Designator Application
 * Author: [Your Name]
 * Email: [Your Auburn Email]
 * Date: April 2026
 * Description: Unit tests for AirlineDatabase class
 */

package com.airline;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AirlineDatabaseTest {
    private AirlineDatabase database;
    private ScheduledFlight flight1;
    private ScheduledFlight flight2;

    @BeforeEach
    public void setUp() {
        database = new AirlineDatabase();

        Set<DayOfWeek> daysOfWeek = new HashSet<>();
        daysOfWeek.add(DayOfWeek.MONDAY);
        daysOfWeek.add(DayOfWeek.WEDNESDAY);

        flight1 = new ScheduledFlight(
            "DL1331",
            "KPIT",
            LocalTime.of(13, 30),
            "KATL",
            LocalTime.of(15, 0),
            daysOfWeek
        );

        flight2 = new ScheduledFlight(
            "DL2000",
            "KATL",
            LocalTime.of(11, 0),
            "KBOS",
            LocalTime.of(14, 30),
            daysOfWeek
        );
    }

    @Test
    public void testEmptyDatabase() {
        assertTrue(database.getScheduledFlights().isEmpty());
    }

    @Test
    public void testAddScheduledFlight() {
        database.addScheduledFlight(flight1);
        assertEquals(1, database.getScheduledFlights().size());
        assertTrue(database.getScheduledFlights().contains(flight1));
    }

    @Test
    public void testAddMultipleScheduledFlights() {
        database.addScheduledFlight(flight1);
        database.addScheduledFlight(flight2);
        assertEquals(2, database.getScheduledFlights().size());
    }

    @Test
    public void testAddScheduledFlightNull() {
        assertThrows(IllegalArgumentException.class, () -> database.addScheduledFlight(null));
    }

    @Test
    public void testAddDuplicateScheduledFlight() {
        database.addScheduledFlight(flight1);
        database.addScheduledFlight(flight1);
        assertEquals(1, database.getScheduledFlights().size()); // Should not add duplicate
    }

    @Test
    public void testRemoveScheduledFlight() {
        database.addScheduledFlight(flight1);
        database.addScheduledFlight(flight2);
        assertEquals(2, database.getScheduledFlights().size());

        database.removeScheduledFlight(flight1);
        assertEquals(1, database.getScheduledFlights().size());
        assertFalse(database.getScheduledFlights().contains(flight1));
    }

    @Test
    public void testRemoveScheduledFlightNull() {
        assertThrows(IllegalArgumentException.class, () -> database.removeScheduledFlight(null));
    }

    @Test
    public void testRemoveNonexistentScheduledFlight() {
        database.addScheduledFlight(flight1);
        // Should not throw, just do nothing
        database.removeScheduledFlight(flight2);
        assertEquals(1, database.getScheduledFlights().size());
    }

    @Test
    public void testUpdateScheduledFlight() {
        database.addScheduledFlight(flight1);
        assertEquals(1, database.getScheduledFlights().size());

        // Modify flight1
        Set<DayOfWeek> newDays = new HashSet<>();
        newDays.add(DayOfWeek.TUESDAY);
        newDays.add(DayOfWeek.THURSDAY);
        flight1.setDaysOfWeek(newDays);

        database.updateScheduledFlight(flight1);
        assertEquals(1, database.getScheduledFlights().size());
        assertTrue(database.getScheduledFlights().get(0).getDaysOfWeek().contains(DayOfWeek.TUESDAY));
    }

    @Test
    public void testUpdateNonexistentScheduledFlight() {
        database.addScheduledFlight(flight1);
        database.updateScheduledFlight(flight2); // Should add as new
        assertEquals(2, database.getScheduledFlights().size());
    }

    @Test
    public void testUpdateScheduledFlightNull() {
        assertThrows(IllegalArgumentException.class, () -> database.updateScheduledFlight(null));
    }

    @Test
    public void testGetScheduledFlights() {
        database.addScheduledFlight(flight1);
        database.addScheduledFlight(flight2);
        assertEquals(2, database.getScheduledFlights().size());
        assertEquals(flight1, database.getScheduledFlights().get(0));
        assertEquals(flight2, database.getScheduledFlights().get(1));
    }
}
