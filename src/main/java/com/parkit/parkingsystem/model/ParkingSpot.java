package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 * Represents a parking spot in the parking system.
 */
public class ParkingSpot {
    private int number;
    private ParkingType parkingType;
    private boolean isAvailable;

    /**
     * Constructs a ParkingSpot with the given number, parking type, and availability status.
     *
     * @param number      the identifier for the parking spot
     * @param parkingType the type of parking (e.g., CAR, BIKE)
     * @param isAvailable the availability status of the parking spot
     */
    public ParkingSpot(int number, ParkingType parkingType, boolean isAvailable) {
        this.number = number;
        this.parkingType = parkingType;
        this.isAvailable = isAvailable;
    }

    /**
     * Gets the identifier of the parking spot.
     *
     * @return the parking spot identifier
     */
    public int getId() {
        return number;
    }

    /**
     * Sets the identifier of the parking spot.
     *
     * @param number the new identifier for the parking spot
     */
    public void setId(int number) {
        this.number = number;
    }

    /**
     * Gets the type of parking.
     *
     * @return the type of parking
     */
    public ParkingType getParkingType() {
        return parkingType;
    }

    /**
     * Sets the type of parking.
     *
     * @param parkingType the new type of parking
     */
    public void setParkingType(ParkingType parkingType) {
        this.parkingType = parkingType;
    }

    /**
     * Checks if the parking spot is available.
     *
     * @return true if the parking spot is available, false otherwise
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Sets the availability status of the parking spot.
     *
     * @param available the new availability status
     */
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    /**
     * Checks if this parking spot is equal to another object.
     * Two parking spots are considered equal if they have the same identifier.
     *
     * @param o the object to compare with
     * @return true if this parking spot is equal to the specified object, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSpot that = (ParkingSpot) o;
        return number == that.number;
    }

    /**
     * Returns the hash code of this parking spot.
     *
     * @return the hash code of this parking spot
     */
    @Override
    public int hashCode() {
        return number;
    }
}
