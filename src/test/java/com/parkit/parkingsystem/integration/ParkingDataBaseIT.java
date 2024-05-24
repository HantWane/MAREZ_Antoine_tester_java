package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Calendar;
import java.util.Locale;
import java.text.SimpleDateFormat;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private final String vehicleRegNumber = "ABCDEF";
    private final long hourInMillis = 60 * 60 * 1000;


    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        // when(inputReaderUtil.readSelection()).thenReturn(1);
        // when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        Mockito.lenient().when(inputReaderUtil.readSelection()).thenReturn(1);
        Mockito.lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

    @Test
    public void testParkingACar(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability

        Ticket ticket = ticketDAO.getTicket("ABCDEF");

        //check that a ticket is actually saved in DB and Parking table is updated with availability
        assertNotNull(ticket);
        assertEquals(ParkingType.CAR, ticket.getParkingSpot().getParkingType());
        assertFalse(ticket.getParkingSpot().isAvailable());

    }

    @Test
    public void testParkingLotExit(){
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();
        //TODO: check that the fare generated and out time are populated correctly in the database
        Date currentTime = new Date();
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(currentTime.getTime() - hourInMillis));
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR,false));
        ticketDAO.saveTicket(ticket);

        parkingService.processExitingVehicle();

        ticket = ticketDAO.getTicket(vehicleRegNumber);

        //check that the fare generated and out time are populated correctly in the database
        assertNotNull(ticket.getOutTime());
        assertTrue(ticket.getPrice() >= 0);

    }

    @Test
    public void testParkingLotExitRecurringUser(){

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        Date currentTime = new Date();
        Ticket firstTicket = new Ticket();
        firstTicket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR,false));
        firstTicket.setVehicleRegNumber(vehicleRegNumber);
        firstTicket.setInTime(new Date(currentTime.getTime() - 2 * hourInMillis));
        firstTicket.setOutTime(new Date(currentTime.getTime() - hourInMillis));
        ticketDAO.saveTicket(firstTicket);

        parkingService.processIncomingVehicle();

        Ticket secondTicket = ticketDAO.getTicket(vehicleRegNumber);
        currentTime.setTime(System.currentTimeMillis());
        secondTicket.setInTime(new Date(currentTime.getTime() - 2 * hourInMillis));
        secondTicket.setOutTime(new Date(currentTime.getTime() - hourInMillis));
        ticketDAO.updateTicket(secondTicket);

        parkingService.processExitingVehicle();

        secondTicket = ticketDAO.getTicket(vehicleRegNumber);
        long duration = (secondTicket.getOutTime().getTime() - secondTicket.getInTime().getTime())/hourInMillis;
        double expectedFare = Fare.CAR_RATE_PER_HOUR * 0.95 * duration;
        assertEquals(expectedFare, secondTicket.getPrice(), 0.005);

    }


}
