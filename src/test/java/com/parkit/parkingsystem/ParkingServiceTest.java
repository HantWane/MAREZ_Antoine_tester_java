package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    /**
     * Sets up the test environment before each test.
     * This includes configuring mock objects for the parking service and its dependencies.
     */
    @BeforeEach
    private void setUpPerTest() {
        try {
            lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            lenient().when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            lenient().when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            lenient().when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }

    /**
     * Tests the process of exiting a vehicle from the parking system.
     * This includes updating the parking spot and ticket information.
     */
         @Test
            public void processExitingVehicleTest(){
            parkingService.processExitingVehicle();

            // Check method updateParking is called once
            verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));

            // Check method getNbTicket is called once
            verify(ticketDAO, Mockito.times(1)).getNbTicket("ABCDEF");

            verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
        }

        /**
     * Tests the process of incoming a vehicle into the parking system.
     * This includes selecting a parking spot, saving the ticket, and updating the parking spot.
     */
        @Test
        public void ProcessIncomingVehicleTest(){
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(1);
        when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);

        parkingService.processIncomingVehicle();

        verify(inputReaderUtil, Mockito.times(1)).readSelection();
        verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, Mockito.times(1)).getNbTicket("ABCDEF");
        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
    }
        
     /**
     * Tests the process of exiting a vehicle from the parking system when unable to update ticket information.
     * This scenario checks if the method handles the inability to update ticket information gracefully.
     */
    @Test
    public void processExitingVehicleTestUnableUpdate(){
        when(ticketDAO.getNbTicket("ABCDEF")).thenReturn(1);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);

        parkingService.processExitingVehicle();

        verify(ticketDAO, Mockito.times(1)).getTicket("ABCDEF");
        verify(ticketDAO, Mockito.times(1)).getNbTicket("ABCDEF");
        verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));
    }

/**
     * Tests the retrieval of the next available parking number for a vehicle.
     * This includes selecting the vehicle type and obtaining the next available parking spot.
     */

    @Test
    public void GetNextParkingNumberIfAvailable(){
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

        parkingService.getNextParkingNumberIfAvailable();

        verify(inputReaderUtil,Mockito.times(1)).readSelection();
        verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
    }

/**
     * Tests the scenario where no parking number is found available for a vehicle.
     * This scenario checks if the method handles the absence of available parking spots gracefully.
     */

    @Test
    public void GetNextParkingNumberIfAvailableParkingNumberNotFound(){
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(0);

        parkingService.getNextParkingNumberIfAvailable();

        verify(inputReaderUtil, Mockito.times(1)).readSelection();
        verify(parkingSpotDAO, Mockito.times(1)).getNextAvailableSlot(any(ParkingType.class));
    }


     /**
     * Tests the scenario where an invalid parking number is selected for a vehicle.
     * This scenario checks if the method handles incorrect user input gracefully.
     */
    @Test
    public void GetNextParkingNumberIfAvailableParkingNumberWrongArgument(){
        when(inputReaderUtil.readSelection()).thenReturn(3);

        parkingService.getNextParkingNumberIfAvailable();

        verify(inputReaderUtil, Mockito.times(1)).readSelection();
    }

    /**
 * Tests the retrieval of the next available parking number for a bike.
 * This includes simulating the selection of the bike type and obtaining the next available parking spot.
 */
    @Test
    public void GetNextParkingNumberIfAvailableForBike() {
        when(inputReaderUtil.readSelection()).thenReturn(2);

        ParkingType result = parkingService.getVehichleType();

        assertEquals(ParkingType.BIKE, result);
    }
    
    /**
 * Tests the process of exiting a vehicle from the parking system with exception handling.
 * This scenario checks if the method handles exceptions gracefully when retrieving ticket information.
 */
    @Test
    public void processExitingVehicleExceptionHandling() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
            when(ticketDAO.getNbTicket("ABCDEF")).thenThrow(new RuntimeException("Test Exception"));
            
            parkingService.processExitingVehicle();

        } catch (Exception e) {
            fail("Exception should be handled in the method");
        }
    }

}