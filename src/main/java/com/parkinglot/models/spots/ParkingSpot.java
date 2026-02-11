package com.parkinglot.models.spots;

import java.util.Objects;

import com.parkinglot.models.vehicles.Vehicle;

/**
 * Base abstraction for parking spots.
 *
 * <p>Concrete implementations define compatibility rules with vehicle types.</p>
 */
public abstract class ParkingSpot {

    private final String id;
    private final SpotType type;
    private boolean occupied;
    private boolean reserved;
    private Vehicle currentVehicle;

    protected ParkingSpot(String id, SpotType type) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Spot id must not be blank");
        }
        this.id = id;
        this.type = Objects.requireNonNull(type, "type must not be null");
    }

    public String getId() {
        return id;
    }

    public SpotType getType() {
        return type;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public Vehicle getCurrentVehicle() {
        return currentVehicle;
    }

    /**
     * @return {@code true} if the spot is available for parking.
     */
    public boolean isAvailable() {
        return !occupied && !reserved;
    }

    /**
     * Indicates whether the provided vehicle can be accommodated in this spot.
     *
     * @param vehicle target vehicle
     * @return {@code true} if compatible, {@code false} otherwise
     */
    public abstract boolean canFitVehicle(Vehicle vehicle);

    /**
     * Assigns the given vehicle to this spot.
     *
     * @param vehicle vehicle to assign
     */
    public void assignVehicle(Vehicle vehicle) {
        if (!isAvailable()) {
            throw new IllegalStateException("Spot is not available");
        }
        if (!canFitVehicle(vehicle)) {
            throw new IllegalArgumentException("Vehicle cannot fit in this spot");
        }
        this.currentVehicle = vehicle;
        this.occupied = true;
    }

    /**
     * Removes the currently assigned vehicle from this spot.
     */
    public void removeVehicle() {
        this.currentVehicle = null;
        this.occupied = false;
        this.reserved = false;
    }
}

