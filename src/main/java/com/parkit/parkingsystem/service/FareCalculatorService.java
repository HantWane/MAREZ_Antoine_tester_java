package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    // Constant for the discount rate
    private static final double DISCOUNT_RATE = 0.95;

    // Method to calculate ticket fare
    public void calculateFare(Ticket ticket) {
        // Call calculate method with default discount parameter set to false
        calculateFare(ticket, false);
    }

    // Method to calculate ticket fare with the option to specify a discount
    public void calculateFare(Ticket ticket, boolean discount) {
        // Validate ticket to ensure data consistency
        validateTicket(ticket);
        // Calculate parking duration in minutes
        double durationMinutes = calculateDurationInMinutes(ticket);
        // Calculate fare rate based on vehicle type
        double fareRate = calculateFareRate(ticket);
        // If parking duration is less than or equal to 30 minutes, fare is set to zero
        if (durationMinutes <= 30) {
            ticket.setPrice(0);
            return;
        }
        // If discount is enabled, apply discount rate
        if (discount) {
            fareRate *= DISCOUNT_RATE;
        }
        // Calculate total price of the ticket
        ticket.setPrice(durationMinutes * fareRate / 60);
    }

    // Method to validate the ticket and its data
    private void validateTicket(Ticket ticket) {
        // Check if out time is valid and after the in time
        if (ticket.getOutTime() == null || ticket.getOutTime().before(ticket.getInTime())) {
            // Throw exception if out time is invalid
            throw new IllegalArgumentException("Invalid out time provided: " + ticket.getOutTime());
        }
    }

    // Method to calculate parking duration in minutes
    private double calculateDurationInMinutes(Ticket ticket) {
        long inTimeMillis = ticket.getInTime().getTime();
        long outTimeMillis = ticket.getOutTime().getTime();
        return (outTimeMillis - inTimeMillis) / 60000.0;
    }

    // Method to calculate fare rate based on vehicle type
    private double calculateFareRate(Ticket ticket) {
        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR:
                return Fare.CAR_RATE_PER_HOUR;
            case BIKE:
                return Fare.BIKE_RATE_PER_HOUR;
            default:
                throw new IllegalArgumentException("Unknown Parking Type");
        }
    }
}