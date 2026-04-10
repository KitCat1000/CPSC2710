/*
 * Project: Module 4 - Airline Flight Designator Application
 * Author: [Your Name]
 * Email: [Your Auburn Email]
 * Date: April 2026
 * Description: IO operations for persisting airline database to file
 */

package com.airline;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class AirlineDatabaseIO {

    /**
     * Save the database to an output stream
     * @param ad the AirlineDatabase to save
     * @param strm the OutputStream to write to
     * @throws IOException if an I/O error occurs
     */
    public static void save(AirlineDatabase ad, OutputStream strm) throws IOException {
        try (OutputStreamWriter osw = new OutputStreamWriter(strm, StandardCharsets.UTF_8);
             PrintWriter writer = new PrintWriter(osw, true)) {

            // Write the number of flights
            writer.println(ad.getScheduledFlights().size());

            // Write each flight
            for (ScheduledFlight flight : ad.getScheduledFlights()) {
                writer.println(flight.getFlightDesignator());
                writer.println(flight.getDepartureAirportIdent());
                writer.println(flight.getDepartureTime());
                writer.println(flight.getArrivalAirportIdent());
                writer.println(flight.getArrivalTime());

                // Write days of week
                Set<DayOfWeek> days = flight.getDaysOfWeek();
                writer.println(days.size());
                for (DayOfWeek day : days) {
                    writer.println(day.toString());
                }
            }
            writer.flush();
        }
    }

    /**
     * Load the database from an input stream
     * @param strm the InputStream to read from
     * @return the loaded AirlineDatabase
     * @throws IOException if an I/O error occurs
     */
    public static AirlineDatabase load(InputStream strm) throws IOException {
        AirlineDatabase database = new AirlineDatabase();

        try (InputStreamReader isr = new InputStreamReader(strm, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {

            String line = reader.readLine();
            if (line == null) {
                return database; // Empty database
            }

            int flightCount = Integer.parseInt(line);

            for (int i = 0; i < flightCount; i++) {
                String flightDesignator = reader.readLine();
                String departureAirportIdent = reader.readLine();
                String departureTimeStr = reader.readLine();
                String arrivalAirportIdent = reader.readLine();
                String arrivalTimeStr = reader.readLine();

                LocalTime departureTime = LocalTime.parse(departureTimeStr);
                LocalTime arrivalTime = LocalTime.parse(arrivalTimeStr);

                // Read days of week
                int dayCount = Integer.parseInt(reader.readLine());
                Set<DayOfWeek> daysOfWeek = new HashSet<>();
                for (int j = 0; j < dayCount; j++) {
                    String dayStr = reader.readLine();
                    daysOfWeek.add(DayOfWeek.valueOf(dayStr));
                }

                ScheduledFlight flight = new ScheduledFlight(
                        flightDesignator,
                        departureAirportIdent,
                        departureTime,
                        arrivalAirportIdent,
                        arrivalTime,
                        daysOfWeek
                );

                database.addScheduledFlight(flight);
            }
        }

        return database;
    }

    /**
     * Save database to a file at the given path
     * @param ad the AirlineDatabase to save
     * @param filePath the path to save to
     * @throws IOException if an I/O error occurs
     */
    public static void saveToFile(AirlineDatabase ad, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            save(ad, fos);
        }
    }

    /**
     * Load database from a file at the given path
     * @param filePath the path to load from
     * @return the loaded AirlineDatabase
     * @throws IOException if an I/O error occurs
     */
    public static AirlineDatabase loadFromFile(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            return load(fis);
        }
    }
}