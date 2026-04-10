/*
 * Project: Module 4 - Airline Flight Designator Application
 * Author: [Your Name]
 * Email: [Your Auburn Email]
 * Date: April 2026
 * Description: Unit tests for AirlineDatabaseIO class
 */

package com.airline;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AirlineDatabaseIOTest {
    private AirlineDatabase database;
    private ScheduledFlight flight1;
    private ScheduledFlight flight2;

    @BeforeEach
    public void setUp() {
        database = new AirlineDatabase();

        Set<DayOfWeek> daysOfWeek1 = new HashSet<>();
        daysOfWeek1.add(DayOfWeek.MONDAY);
        daysOfWeek1.add(DayOfWeek.WEDNESDAY);
        daysOfWeek1.add(DayOfWeek.FRIDAY);

        flight1 = new ScheduledFlight(
            "DL1331",
            "KPIT",
            LocalTime.of(13, 30),
            "KATL",
            LocalTime.of(15, 0),
            daysOfWeek1
        );

        Set<DayOfWeek> daysOfWeek2 = new HashSet<>();
        daysOfWeek2.add(DayOfWeek.TUESDAY);
        daysOfWeek2.add(DayOfWeek.THURSDAY);
        daysOfWeek2.add(DayOfWeek.SATURDAY);

        flight2 = new ScheduledFlight(
            "DL2000",
            "KATL",
            LocalTime.of(11, 0),
            "KBOS",
            LocalTime.of(14, 30),
            daysOfWeek2
        );
    }

    @Test
    public void testSaveAndLoadEmptyDatabase() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        AirlineDatabaseIO.save(database, outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        AirlineDatabase loadedDatabase = AirlineDatabaseIO.load(inputStream);

        assertEquals(0, loadedDatabase.getScheduledFlights().size());
    }

    @Test
    public void testSaveAndLoadSingleFlight() throws IOException {
        database.addScheduledFlight(flight1);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        AirlineDatabaseIO.save(database, outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        AirlineDatabase loadedDatabase = AirlineDatabaseIO.load(inputStream);

        assertEquals(1, loadedDatabase.getScheduledFlights().size());
        ScheduledFlight loaded = loadedDatabase.getScheduledFlights().get(0);
        assertEquals("DL1331", loaded.getFlightDesignator());
        assertEquals("KPIT", loaded.getDepartureAirportIdent());
        assertEquals(LocalTime.of(13, 30), loaded.getDepartureTime());
        assertEquals("KATL", loaded.getArrivalAirportIdent());
        assertEquals(LocalTime.of(15, 0), loaded.getArrivalTime());
        assertEquals(3, loaded.getDaysOfWeek().size());
    }

    @Test
    public void testSaveAndLoadMultipleFlights() throws IOException {
        database.addScheduledFlight(flight1);
        database.addScheduledFlight(flight2);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        AirlineDatabaseIO.save(database, outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        AirlineDatabase loadedDatabase = AirlineDatabaseIO.load(inputStream);

        assertEquals(2, loadedDatabase.getScheduledFlights().size());
        
        ScheduledFlight loaded1 = loadedDatabase.getScheduledFlights().get(0);
        assertEquals("DL1331", loaded1.getFlightDesignator());
        
        ScheduledFlight loaded2 = loadedDatabase.getScheduledFlights().get(1);
        assertEquals("DL2000", loaded2.getFlightDesignator());
    }

    @Test
    public void testSaveToFileAndLoadFromFile(@TempDir Path tempDir) throws IOException {
        database.addScheduledFlight(flight1);
        database.addScheduledFlight(flight2);

        String filePath = tempDir.resolve("test_flights.db").toString();
        AirlineDatabaseIO.saveToFile(database, filePath);

        AirlineDatabase loadedDatabase = AirlineDatabaseIO.loadFromFile(filePath);

        assertEquals(2, loadedDatabase.getScheduledFlights().size());
        assertEquals("DL1331", loadedDatabase.getScheduledFlights().get(0).getFlightDesignator());
        assertEquals("DL2000", loadedDatabase.getScheduledFlights().get(1).getFlightDesignator());
    }

    @Test
    public void testLoadFromNonexistentFile() {
        assertThrows(FileNotFoundException.class, () -> {
            AirlineDatabaseIO.loadFromFile("/nonexistent/path/flights.db");
        });
    }

    @Test
    public void testSavePreservesFlightData() throws IOException {
        database.addScheduledFlight(flight1);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        AirlineDatabaseIO.save(database, outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        AirlineDatabase loadedDatabase = AirlineDatabaseIO.load(inputStream);

        ScheduledFlight loaded = loadedDatabase.getScheduledFlights().get(0);
        
        // Verify all fields are preserved
        assertEquals(flight1.getFlightDesignator(), loaded.getFlightDesignator());
        assertEquals(flight1.getDepartureAirportIdent(), loaded.getDepartureAirportIdent());
        assertEquals(flight1.getDepartureTime(), loaded.getDepartureTime());
        assertEquals(flight1.getArrivalAirportIdent(), loaded.getArrivalAirportIdent());
        assertEquals(flight1.getArrivalTime(), loaded.getArrivalTime());
        assertEquals(flight1.getDaysOfWeek(), loaded.getDaysOfWeek());
    }

    @Test
    public void testSavePreservesDaysOfWeek() throws IOException {
        Set<DayOfWeek> expectedDays = new HashSet<>();
        expectedDays.add(DayOfWeek.MONDAY);
        expectedDays.add(DayOfWeek.THURSDAY);
        expectedDays.add(DayOfWeek.SUNDAY);
        
        flight1.setDaysOfWeek(expectedDays);
        database.addScheduledFlight(flight1);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        AirlineDatabaseIO.save(database, outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        AirlineDatabase loadedDatabase = AirlineDatabaseIO.load(inputStream);

        ScheduledFlight loaded = loadedDatabase.getScheduledFlights().get(0);
        assertEquals(expectedDays, loaded.getDaysOfWeek());
    }
}
