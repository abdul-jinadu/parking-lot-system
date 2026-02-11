package com.parkinglot.models.spots;

import java.util.Objects;

import com.parkinglot.models.vehicles.Vehicle;

// Abstract base class for parking spots
// Concrete subclasses define vehicle compatibility rules
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

    public boolean isAvailable() {
        return !occupied && !reserved;
    }

    // Whether the given vehicle can fit in this spot
    public abstract boolean canFitVehicle(Vehicle vehicle);

    // Assigns a vehicle to this spot
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

    // Removes the currently assigned vehicle
    public void removeVehicle() {
        this.currentVehicle = null;
        this.occupied = false;
        this.reserved = false;
    }
}
