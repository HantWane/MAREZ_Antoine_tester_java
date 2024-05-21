package com.parkit.parkingsystem.model;

import java.util.Calendar;
import java.util.Date;

/**
 * Represents a parking ticket in the parking system.
 */
public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;
    private boolean discount;

    /**
     * Gets the identifier of the ticket.
     *
     * @return the ticket identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the identifier of the ticket.
     *
     * @param id the new identifier for the ticket
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the parking spot associated with the ticket.
     *
     * @return the parking spot
     */
    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    /**
     * Sets the parking spot associated with the ticket.
     *
     * @param parkingSpot the new parking spot
     */
    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    /**
     * Gets the vehicle registration number.
     *
     * @return the vehicle registration number
     */
    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    /**
     * Sets the vehicle registration number.
     *
     * @param vehicleRegNumber the new vehicle registration number
     */
    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    /**
     * Gets the price of the ticket.
     *
     * @return the ticket price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the ticket.
     *
     * @param price the new ticket price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the entry time of the ticket.
     *
     * @return the entry time
     */
    public Date getInTime() {
        return inTime;
    }

    /**
     * Sets the entry time of the ticket.
     *
     * @param inTime the new entry time
     */
    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    /**
     * Gets the exit time of the ticket.
     *
     * @return the exit time
     */
    public Date getOutTime() {
        return outTime;
    }

    /**
     * Sets the exit time of the ticket.
     *
     * @param outTime the new exit time
     */
    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    /**
     * Sets whether the ticket is discounted.
     *
     * @param discount true if the ticket is discounted, false otherwise
     */
    public void setDiscount(boolean discount) {
        this.discount = discount;
    }

    /**
     * Checks if the ticket is discounted.
     *
     * @return true if the ticket is discounted, false otherwise
     */
    public boolean isDiscounted() {
        return discount;
    }
}
