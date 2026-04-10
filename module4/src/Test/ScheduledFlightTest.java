/*
 * Project: Module 4 - Airline Flight Designator Application
 * Author: [Your Name]
 * Email: [Your Auburn Email]
 * Date: April 2026
 * Description: Unit tests for ScheduledFlight domain model
 */

package com.airline;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduledFlightTest {
    private ScheduledFlight flight;
    private Set<DayOfWeek> daysOfWeek;

    @BeforeEach
    public void setUp() {
        daysOfWeek = new HashSet<>();
        daysOfWeek.add(DayOfWeek.MONDAY);
        daysOfWeek.add(DayOfWeek.WEDNESDAY);
        daysOfWeek.add(DayOfWeek.FRIDAY);

        flight = new ScheduledFlight(
            "DL1331",
            "KPIT",
            LocalTime.of(13, 30),
            "KATL",
            LocalTime.of(15, 0),
            daysOfWeek
        );
    }

    @Test
    public void testConstructor() {
        assertEquals("DL1331", flight.getFlightDesignator());
        assertEquals("KPIT", flight.getDepartureAirportIdent());
        assertEquals(LocalTime.of(13, 30), flight.getDepartureTime());
        assertEquals("KATL", flight.getArrivalAirportIdent());
        assertEquals(LocalTime.of(15, 0), flight.getArrivalTime());
        assertEquals(3, flight.getDaysOfWeek().size());
    }

    @Test
    public void testConstructorNullFlightDesignator() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ScheduledFlight(null, "KPIT", LocalTime.of(13, 30),
                "KATL", LocalTime.of(15, 0), daysOfWeek);
        });
    }

    @Test
    public void testSetFlightDesignator() {
        flight.setFlightDesignator("DL2000");
        assertEquals("DL2000", flight.getFlightDesignator());
    }

    @Test
    public void testSetFlightDesignatorNull() {
        assertThrows(IllegalArgumentException.class, () -> flight.setFlightDesignator(null));
    }

    @Test
    public void testSetDepartureAirportIdent() {
        flight.setDepartureAirportIdent("KATLANT");
        assertEquals("KATLANT", flight.getDepartureAirportIdent());
    }

    @Test
    public void testSetDepartureAirportIdentNull() {
        assertThrows(IllegalArgumentException.class, () -> flight.setDepartureAirportIdent(null));
    }

    @Test
    public void testSetDepartureTime() {
        LocalTime newTime = LocalTime.of(14, 0);
        flight.setDepartureTime(newTime);
        assertEquals(newTime, flight.getDepartureTime());
    }

    @Test
    public void testSetDepartureTimeNull() {
        assertThrows(IllegalArgumentException.class, () -> flight.setDepartureTime(null));
    }

    @Test
    public void testSetArrivalAirportIdent() {
        flight.setArrivalAirportIdent("KJFK");
        assertEquals("KJFK", flight.getArrivalAirportIdent());
    }

    @Test
    public void testSetArrivalAirportIdentNull() {
        assertThrows(IllegalArgumentException.class, () -> flight.setArrivalAirportIdent(null));
    }

    @Test
    public void testSetArrivalTime() {
        LocalTime newTime = LocalTime.of(16, 30);
        flight.setArrivalTime(newTime);
        assertEquals(newTime, flight.getArrivalTime());
    }

    @Test
    public void testSetArrivalTimeNull() {
        assertThrows(IllegalArgumentException.class, () -> flight.setArrivalTime(null));
    }

    @Test
    public void testSetDaysOfWeek() {
        Set<DayOfWeek> newDays = new HashSet<>();
        newDays.add(DayOfWeek.TUESDAY);
        newDays.add(DayOfWeek.THURSDAY);
        flight.setDaysOfWeek(newDays);
        assertEquals(2, flight.getDaysOfWeek().size());
        assertTrue(flight.getDaysOfWeek().contains(DayOfWeek.TUESDAY));
    }

    @Test
    public void testSetDaysOfWeekNull() {
        assertThrows(IllegalArgumentException.class, () -> flight.setDaysOfWeek(null));
    }

    @Test
    public void testGetDaysOfWeekAsString() {
        String result = flight.getDaysOfWeekAsString();
        assertEquals("MWF", result); // Monday, Wednesday, Friday
    }

    @Test
    public void testGetDaysOfWeekAsStringWithThursdayAndSunday() {
        Set<DayOfWeek> days = new HashSet<>();
        days.add(DayOfWeek.THURSDAY);
        days.add(DayOfWeek.SUNDAY);
        flight.setDaysOfWeek(days);
        String result = flight.getDaysOfWeekAsString();
        assertEquals("RU", result); // Thursday, Sunday
    }

    @Test
    public void testEqualsAndHashCode() {
        ScheduledFlight flight2 = new ScheduledFlight(
            "DL1331",
            "KATY",
            LocalTime.of(14, 0),
            "KBOS",
            LocalTime.of(16, 0),
            new HashSet<>()
        );
        assertEquals(flight, flight2);
        assertEquals(flight.hashCode(), flight2.hashCode());
    }

    @Test
    public void testNotEquals() {
        ScheduledFlight flight2 = new ScheduledFlight(
            "DL1000",
            "KPIT",
            LocalTime.of(13, 30),
            "KATL",
            LocalTime.of(15, 0),
            daysOfWeek
        );
        assertNotEquals(flight, flight2);
    }

    @Test
    public void testEmptyConstructor() {
        ScheduledFlight emptyFlight = new ScheduledFlight();
        assertNull(emptyFlight.getFlightDesignator());
        assertTrue(emptyFlight.getDaysOfWeek().isEmpty());
    }
}
