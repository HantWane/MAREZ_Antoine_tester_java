package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * Data Access Object for the Ticket table.
 * Provides methods to interact with the database for ticket-related operations.
 */
public class TicketDAO {

    private static final Logger logger = LogManager.getLogger("TicketDAO");

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    /**
     * Saves a ticket in the database.
     *
     * @param ticket the ticket to save
     * @return true if the ticket was successfully saved, false otherwise
     */
    public boolean saveTicket(Ticket ticket) {
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_TICKET);
            ps.setInt(1, ticket.getParkingSpot().getId());
            ps.setString(2, ticket.getVehicleRegNumber());
            ps.setDouble(3, ticket.getPrice());
            ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
            ps.setTimestamp(5, (ticket.getOutTime() == null) ? null : new Timestamp(ticket.getOutTime().getTime()));
            return ps.execute();
        } catch (Exception ex) {
            logger.error("Error saving ticket", ex);
            return false;
        } finally {
            dataBaseConfig.closeConnection(con);
        }
    }

    /**
     * Retrieves a ticket from the database based on the vehicle registration number.
     *
     * @param vehicleRegNumber the vehicle registration number
     * @return the ticket associated with the vehicle registration number, or null if no ticket is found
     */
    public Ticket getTicket(String vehicleRegNumber) {
        Connection con = null;
        Ticket ticket = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET);
            ps.setString(1, vehicleRegNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ticket = new Ticket();
                ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), false);
                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(2));
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(rs.getDouble(3));
                ticket.setInTime(rs.getTimestamp(4));
                ticket.setOutTime(rs.getTimestamp(5));
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        } catch (Exception ex) {
            logger.error("Error fetching ticket", ex);
        } finally {
            dataBaseConfig.closeConnection(con);
        }
        return ticket;
    }

    /**
     * Updates a ticket in the database.
     *
     * @param ticket the ticket to update
     * @return true if the ticket was successfully updated, false otherwise
     */
    public boolean updateTicket(Ticket ticket) {
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
            ps.setDouble(1, ticket.getPrice());
            ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
            ps.setInt(3, ticket.getId());
            ps.execute();
            return true;
        } catch (Exception ex) {
            logger.error("Error updating ticket", ex);
        } finally {
            dataBaseConfig.closeConnection(con);
        }
        return false;
    }

    /**
     * Gets the number of tickets associated with a specific vehicle registration number.
     *
     * @param vehicleRegNumber the vehicle registration number
     * @return the number of tickets associated with the vehicle registration number
     */
    public int getNbTicket(String vehicleRegNumber) {
        Connection con = null;
        int nbTickets = 0;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_NB_TICKET);
            ps.setString(1, vehicleRegNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nbTickets = rs.getInt(1);
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        } catch (Exception ex) {
            logger.error("Error fetching ticket count", ex);
        } finally {
            dataBaseConfig.closeConnection(con);
        }
        return nbTickets;
    }
}
