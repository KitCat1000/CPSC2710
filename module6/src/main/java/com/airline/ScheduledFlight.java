/*
 * Project: Module 4 - Airline Flight Designator Application
 * Author: Nicole Tressler
 * Email: Nit0005@auburn.edu
 * Date: April 7 2026
 * Description: Database class for managing airline scheduled flights
 */

package com.airline;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.time.DayOfWeek;
import java.util.Objects;

public class ScheduledFlight {
    private String flightDesignator;
    private String departureAirportIdent;
    private LocalTime departureTime;
    private String arrivalAirportIdent;
    private LocalTime arrivalTime;
    private Set<DayOfWeek> daysOfWeek;

    public ScheduledFlight() {
        this.daysOfWeek = new HashSet<>();
    }

    public ScheduledFlight(String flightDesignator, String departureAirportIdent,
                           LocalTime departureTime, String arrivalAirportIdent,
                           LocalTime arrivalTime, Set<DayOfWeek> daysOfWeek) {
        this.flightDesignator = Objects.requireNonNull(flightDesignator, "Flight designator cannot be null");
        this.departureAirportIdent = Objects.requireNonNull(departureAirportIdent, "Departure airport ident cannot be null");
        this.departureTime = Objects.requireNonNull(departureTime, "Departure time cannot be null");
        this.arrivalAirportIdent = Objects.requireNonNull(arrivalAirportIdent, "Arrival airport ident cannot be null");
        this.arrivalTime = Objects.requireNonNull(arrivalTime, "Arrival time cannot be null");
        this.daysOfWeek = Objects.requireNonNull(daysOfWeek, "Days of week cannot be null");
    }

    // Getters and Setters with validation
    public String getFlightDesignator() {
        return flightDesignator;
    }

    public void setFlightDesignator(String flightDesignator) {
        if (flightDesignator == null) {
            throw new IllegalArgumentException("Flight designator cannot be null");
        }
        this.flightDesignator = flightDesignator;
    }

    public String getDepartureAirportIdent() {
        return departureAirportIdent;
    }

    public void setDepartureAirportIdent(String departureAirportIdent) {
        if (departureAirportIdent == null) {
            throw new IllegalArgumentException("Departure airport ident cannot be null");
        }
        this.departureAirportIdent = departureAirportIdent;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        if (departureTime == null) {
            throw new IllegalArgumentException("Departure time cannot be null");
        }
        this.departureTime = departureTime;
    }

    public String getArrivalAirportIdent() {
        return arrivalAirportIdent;
    }

    public void setArrivalAirportIdent(String arrivalAirportIdent) {
        if (arrivalAirportIdent == null) {
            throw new IllegalArgumentException("Arrival airport ident cannot be null");
        }
        this.arrivalAirportIdent = arrivalAirportIdent;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        if (arrivalTime == null) {
            throw new IllegalArgumentException("Arrival time cannot be null");
        }
        this.arrivalTime = arrivalTime;
    }

    public Set<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(Set<DayOfWeek> daysOfWeek) {
        if (daysOfWeek == null) {
            throw new IllegalArgumentException("Days of week cannot be null");
        }
        this.daysOfWeek = daysOfWeek;
    }

    // Helper method to convert days of week to string representation
    public String getDaysOfWeekAsString() {
        StringBuilder sb = new StringBuilder();
        DayOfWeek[] daysInOrder = {
                DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY
        };

        for (DayOfWeek day : daysInOrder) {
            if (daysOfWeek.contains(day)) {
                switch (day) {
                    case MONDAY:
                        sb.append("M");
                        break;
                    case TUESDAY:
                        sb.append("T");
                        break;
                    case WEDNESDAY:
                        sb.append("W");
                        break;
                    case THURSDAY:
                        sb.append("R");
                        break;
                    case FRIDAY:
                        sb.append("F");
                        break;
                    case SATURDAY:
                        sb.append("S");
                        break;
                    case SUNDAY:
                        sb.append("U");
                        break;
                }
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "ScheduledFlight{" +
                "flightDesignator='" + flightDesignator + '\'' +
                ", departureAirportIdent='" + departureAirportIdent + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalAirportIdent='" + arrivalAirportIdent + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", daysOfWeek=" + getDaysOfWeekAsString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduledFlight that = (ScheduledFlight) o;
        return Objects.equals(flightDesignator, that.flightDesignator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightDesignator);
    }
}