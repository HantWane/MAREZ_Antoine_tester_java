package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        //int inHour = ticket.getInTime().getHours();
        //int outHour = ticket.getOutTime().getHours();

        long inTimeMillis = ticket.getInTime().getTime();
        long outTimeMillis = ticket.getOutTime().getTime();


        //TODO: Some tests are failing here. Need to check if this logic is correct
        //int duration = outHour - inHour;

        // Conversion en minutes
        double durationMinutes = (outTimeMillis - inTimeMillis) / 60000.0;


        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(durationMinutes * Fare.CAR_RATE_PER_HOUR / 60); // Convertir les tarifs horaires en minutes
                break;
            }
            case BIKE: {
                ticket.setPrice(durationMinutes * Fare.BIKE_RATE_PER_HOUR / 60);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}