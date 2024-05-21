package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

/**
 * Service to calculate the fare for parking based on the ticket information.
 */
public class FareCalculatorService {

    // Constant for the discount rate
    private static final double DISCOUNT_RATE = 0.95;

    /**
     * Calculates the fare for a given ticket.
     * 
     * @param ticket the ticket for which the fare is to be calculated
     */
    public void calculateFare(Ticket ticket) {
        // Call calculate method with default discount parameter set to false
        calculateFare(ticket, false);
    }

    /**
     * Calculates the fare for a given ticket, with an option to apply a discount.
     * 
     * @param ticket the ticket for which the fare is to be calculated
     * @param discount whether to apply a discount to the fare
     */
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

    /**
     * Validates the ticket and its data.
     * 
     * @param ticket the ticket to validate
     * @throws IllegalArgumentException if the out time is before the in time or is null
     */
    private void validateTicket(Ticket ticket) {
        // Check if out time is valid and after the in time
        if (ticket.getOutTime() == null || ticket.getOutTime().before(ticket.getInTime())) {
            // Throw exception if out time is invalid
            throw new IllegalArgumentException("Invalid out time provided: " + ticket.getOutTime());
        }
    }

    /**
     * Calculates the parking duration in minutes based on the ticket information.
     * 
     * @param ticket the ticket for which the duration is to be calculated
     * @return the parking duration in minutes
     */
    private double calculateDurationInMinutes(Ticket ticket) {
        long inTimeMillis = ticket.getInTime().getTime();
        long outTimeMillis = ticket.getOutTime().getTime();
        return (outTimeMillis - inTimeMillis) / 60000.0;
    }

    /**
     * Calculates the fare rate based on the vehicle type.
     * 
     * @param ticket the ticket for which the fare rate is to be calculated
     * @return the fare rate per hour
     * @throws IllegalArgumentException if the parking type is unknown
     */
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
