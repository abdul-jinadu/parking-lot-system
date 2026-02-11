package com.parkinglot.models.vehicles;

import com.parkinglot.interfaces.Parkable;
import com.parkinglot.models.ParkingTicket;
import com.parkinglot.models.spots.ParkingSpot;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Base abstraction for all vehicles managed by the parking lot.
 *
 * <p>This class encapsulates common state such as license plate and color,
 * and provides default implementations for {@link Parkable} operations that
 * can be specialized by concrete vehicle types.</p>
 */
public abstract class Vehicle implements Parkable {

    private final String licensePlate;
    private String color;
    private final VehicleType type;

    private ParkingTicket activeTicket;

    /**
     * Constructs a new vehicle.
     *
     * @param licensePlate unique license plate identifier (non-null, non-empty)
     * @param color        vehicle color
     * @param type         vehicle type
     */
    protected Vehicle(String licensePlate, String color, VehicleType type) {
        if (licensePlate == null || licensePlate.isBlank()) {
            throw new IllegalArgumentException("License plate must not be blank");
        }
        this.licensePlate = licensePlate.toUpperCase();
        this.color = Objects.requireNonNullElse(color, "UNKNOWN");
        this.type = Objects.requireNonNull(type, "type must not be null");
    }

    /**
     * @return license plate value in normalized form.
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        if (color == null || color.isBlank()) {
            throw new IllegalArgumentException("Color must not be blank");
        }
        this.color = color;
    }

    public VehicleType getType() {
        return type;
    }

    public ParkingTicket getActiveTicket() {
        return activeTicket;
    }

    public void setActiveTicket(ParkingTicket activeTicket) {
        this.activeTicket = activeTicket;
    }

    /**
     * @return vehicle-specific size multiplier used in pricing.
     */
    public abstract double getSizeMultiplier();

    /**
     * Indicates whether this vehicle can be accommodated by the given spot.
     *
     * @param spot the target parking spot
     * @return {@code true} if it can fit, {@code false} otherwise
     */
    public abstract boolean canFitInSpot(ParkingSpot spot);

    /**
     * @return base hourly rate (in local currency) for this vehicle type.
     */
    protected abstract double getBaseHourlyRate();

    @Override
    public boolean park(ParkingSpot spot) {
        if (spot == null || activeTicket != null) {
            return false;
        }
        if (!canFitInSpot(spot) || !spot.isAvailable()) {
            return false;
        }
        spot.assignVehicle(this);
        this.activeTicket = ParkingTicket.start(this, spot, LocalDateTime.now());
        return true;
    }

    @Override
    public boolean exit() {
        if (activeTicket == null) {
            return false;
        }
        ParkingSpot spot = activeTicket.getSpot();
        spot.removeVehicle();
        activeTicket.close(LocalDateTime.now());
        this.activeTicket = null;
        return true;
    }

    @Override
    public double calculateFee(long durationInMinutes) {
        if (durationInMinutes <= 0) {
            return 0.0;
        }
        double hours = Math.ceil(durationInMinutes / 60.0);
        return getBaseHourlyRate() * getSizeMultiplier() * hours;
    }
}


