package com.parkit.parkingsystem.integration.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TicketDAOTest {

    private final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private final TicketDAO ticketDAO = new TicketDAO();
    private Ticket ticket;

    @BeforeEach
    public void setUp(){
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(new Date());
        ticket.setOutTime(null);
    }

    @Test
    public void testSaveTicket(){
        assertNotNull(ticket.getParkingSpot());
        int countTicketsBeforeSave = ticketDAO.getNbTicket("ABCDEF");
        ticketDAO.saveTicket(ticket);
        int countTicketsAfterSave = ticketDAO.getNbTicket("ABCDEF");
        assertEquals(countTicketsBeforeSave +1 , countTicketsAfterSave);
    }

    @Test
    public void testSaveTicketFail(){
        ticket.setParkingSpot(null);
        assertFalse(ticketDAO.saveTicket(ticket));
    }

    @Test
    public void testGetTicket(){
        ticketDAO.saveTicket(ticket);
        Ticket savedTicket = ticketDAO.getTicket("ABCDEF");
        assertNotNull(savedTicket);
        assertEquals("ABCDEF", savedTicket.getVehicleRegNumber());
    }

    @Test
    public void testUpdateTicket(){
        ticketDAO.saveTicket(ticket);
        Ticket savedTicket = ticketDAO.getTicket("ABCDEF");
        savedTicket.setPrice(10d);
        savedTicket.setOutTime(new Date());
        assertTrue(ticketDAO.updateTicket(savedTicket));
    }

    @Test
    public void testUpdateTicketFail(){
        ticketDAO.saveTicket(ticket);
        Ticket savedTicket = ticketDAO.getTicket("ABCDEF");
        savedTicket.setOutTime(null);
        assertFalse(ticketDAO.updateTicket(savedTicket));
    }

    @Test
    public void testGetNbTicket(){
        int countTicketsBeforeSave = ticketDAO.getNbTicket("ABCDEF");
        ticketDAO.saveTicket(ticket);
        int countTicketsAfterSave = ticketDAO.getNbTicket("ABCDEF");
        assertEquals(countTicketsBeforeSave + 1, countTicketsAfterSave);
    }

    @Test
    public void testGetNbTicketNotFound(){
        int countTickets = ticketDAO.getNbTicket(null);
        assertEquals(0, countTickets);
    }

}