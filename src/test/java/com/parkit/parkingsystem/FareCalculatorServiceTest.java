package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test
    //public void calculateFareCar(){
    public void should_CalculateFareForCar_When_ParkingDurationIsOneHour(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        //assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
        assertEquals(Fare.CAR_RATE_PER_HOUR, ticket.getPrice() );
    }

    @Test
    //public void calculateFareBike(){
    public void should_CalculateFareForBike_When_ParkingDurationIsOneHour(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        //assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
        assertEquals(Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
    }

    @Test
    //public void calculateFareUnkownType(){
    public void should_ThrowNullPointerException_When_ParkingSpotTypeIsNull() {
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    //public void calculateFareBikeWithFutureInTime(){
    public void should_ThrowIllegalArgumentException_When_EntryTimeIsInFutureForBike() {
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void should_calculateFareBikeWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }

    @Test
    public void should_calculateFareCarWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (0.75 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    public void should_calculateFareCarWithMoreThanADayParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  24 * 60 * 60 * 1000) );//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
public void should_CalculateFareForCarWithLessThan30MinutesParkingTime() {
    // Create a Date object representing the entry time less than 30 minutes ago
    Date inTime = new Date();
    inTime.setTime( System.currentTimeMillis() - (29 * 60 * 1000) );
    Date outTime = new Date();

    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);

    fareCalculatorService.calculateFare(ticket);

    // Check if the calculated price is equal to 0
    assertEquals(0, ticket.getPrice());
    }

    @Test
    public void should_CalculateFareForBikeWithLessThan30MinutesParkingTime() {
        // Create a Date object representing the entry time less than 30 minutes ago
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (29 * 60 * 1000) );
        Date outTime = new Date();
    
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
    
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
    
        fareCalculatorService.calculateFare(ticket);
    
        // Check if the calculated price is equal to 0
        assertEquals(0, ticket.getPrice());
        }

        @Test
        public void should_CalculateFareForCarWithDiscount() {
            // Create a Date object representing the input time
            Date inTime = new Date();
            inTime.setTime( System.currentTimeMillis() - (60 * 60 * 1000) );
            Date outTime = new Date();
        
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        
            Ticket ticket = new Ticket();
            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
        
            // Set discount parameter to true
            ticket.setDiscount(true);
        
            fareCalculatorService.calculateFare(ticket, true);
        
            // Calculate the full price for a car with a parking time greater than 30 minutes
            double fullFare = (outTime.getTime() - inTime.getTime()) * Fare.CAR_RATE_PER_HOUR / (1000 * 60 * 60); // Convert on hours
        
            // Calculate the expected price with the 5% reduction
            double expectedPrice = fullFare * 0.95;
        
            // Check if the calculated price is equal to 95% of the full price
            assertEquals(expectedPrice, ticket.getPrice(), 0.01); // Using delta to handle small deviations due to rounding
        }


        @Test
        public void should_CalculateFareForBikeWithDiscount() {
            // Create a Date object representing the input time
            Date inTime = new Date();
            inTime.setTime( System.currentTimeMillis() - (60 * 60 * 1000) );
            Date outTime = new Date();
        
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        
            Ticket ticket = new Ticket();
            ticket.setInTime(inTime);
            ticket.setOutTime(outTime);
            ticket.setParkingSpot(parkingSpot);
        
            // Set discount parameter to true
            ticket.setDiscount(true);
        
            fareCalculatorService.calculateFare(ticket, true);
        
            // Calculate the full price for a car with a parking time greater than 30 minutes
            double fullFare = (outTime.getTime() - inTime.getTime()) * Fare.CAR_RATE_PER_HOUR / (1000 * 60 * 60); // Convert on hours
        
            // Calculate the expected price with the 5% reduction
            double expectedPrice = fullFare * 0.95;
        
            // Check if the calculated price is equal to 95% of the full price
            assertEquals(expectedPrice, ticket.getPrice(), 0.01); // Using delta to handle small deviations due to rounding
        }
        


}
