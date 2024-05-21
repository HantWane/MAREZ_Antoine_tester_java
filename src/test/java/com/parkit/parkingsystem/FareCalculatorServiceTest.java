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

/**
 * Unit tests for the {@link FareCalculatorService} class.
 * This class tests the fare calculation logic for different scenarios.
 */
public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    /**
     * Sets up the test environment before all tests.
     * This includes initializing the FareCalculatorService instance.
     */
    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    /**
     * Sets up the test environment before each test.
     * This includes initializing a new Ticket instance.
     */
    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    /**
     * Tests the fare calculation for a car with a parking duration of one hour.
     * The fare should be equal to the hourly rate for cars.
     */
    @Test
    public void should_CalculateFareForCar_When_ParkingDurationIsOneHour() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
    }

    /**
     * Tests the fare calculation for a bike with a parking duration of one hour.
     * The fare should be equal to the hourly rate for bikes.
     */
    @Test
    public void should_CalculateFareForBike_When_ParkingDurationIsOneHour() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
    }

    /**
     * Tests that a NullPointerException is thrown when the parking spot type is null.
     */
    @Test
    public void should_ThrowNullPointerException_When_ParkingSpotTypeIsNull() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    /**
     * Tests that an IllegalArgumentException is thrown when the entry time is in the future for a bike.
     */
    @Test
    public void should_ThrowIllegalArgumentException_When_EntryTimeIsInFutureForBike() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    /**
     * Tests the fare calculation for a bike with a parking duration of less than one hour.
     * The fare should be proportionate to the time parked (3/4th of the hourly rate for 45 minutes).
     */
    @Test
    public void should_calculateFareBikeWithLessThanOneHourParkingTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000)); // 45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }

    /**
     * Tests the fare calculation for a car with a parking duration of less than one hour.
     * The fare should be proportionate to the time parked (3/4th of the hourly rate for 45 minutes).
     */
    @Test
    public void should_calculateFareCarWithLessThanOneHourParkingTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000)); // 45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

    /**
     * Tests the fare calculation for a car with a parking duration of more than a day.
     * The fare should be equal to 24 times the hourly rate for cars.
     */
    @Test
    public void should_calculateFareCarWithMoreThanADayParkingTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000)); // 24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

    /**
     * Tests the fare calculation for a car with a parking duration of less than 30 minutes.
     * The fare should be zero.
     */
    @Test
    public void should_CalculateFareForCarWithLessThan30MinutesParkingTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (29 * 60 * 1000)); // 29 minutes parking time
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(0, ticket.getPrice());
    }

    /**
     * Tests the fare calculation for a bike with a parking duration of less than 30 minutes.
     * The fare should be zero.
     */
    @Test
    public void should_CalculateFareForBikeWithLessThan30MinutesParkingTime() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (29 * 60 * 1000)); // 29 minutes parking time
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(0, ticket.getPrice());
    }

    /**
     * Tests the fare calculation for a car with a 5% discount.
     * The fare should be equal to 95% of the hourly rate for cars.
     */
    @Test
    public void should_CalculateFareForCarWithDiscount() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000)); // 1 hour parking time
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setDiscount(true);

        fareCalculatorService.calculateFare(ticket, true);

        double fullFare = (outTime.getTime() - inTime.getTime()) * Fare.CAR_RATE_PER_HOUR / (1000 * 60 * 60); // Convert on hours
        double expectedPrice = fullFare * 0.95;

        assertEquals(expectedPrice, ticket.getPrice(), 0.01); // Using delta to handle small deviations due to rounding
    }

    /**
     * Tests the fare calculation for a bike with a 5% discount.
     * The fare should be equal to 95% of the hourly rate for bikes.
     */
    @Test
    public void should_CalculateFareForBikeWithDiscount() {
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000)); // 1 hour parking time
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setDiscount(true);

        fareCalculatorService.calculateFare(ticket, true);

        double fullFare = (outTime.getTime() - inTime.getTime()) * Fare.BIKE_RATE_PER_HOUR / (1000 * 60 * 60); // Convert on hours
        double expectedPrice = fullFare * 0.95;

        assertEquals(expectedPrice, ticket.getPrice(), 0.01); // Using delta to handle small deviations due to rounding
    }
}
