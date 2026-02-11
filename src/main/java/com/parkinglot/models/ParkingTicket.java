package com.parkinglot.models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import com.parkinglot.models.spots.ParkingSpot;
import com.parkinglot.models.vehicles.Vehicle;

public class ParkingTicket {

    private final String id;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;

    private ParkingTicket(Vehicle vehicle, ParkingSpot spot, LocalDateTime entryTime) {
        this.id = UUID.randomUUID().toString();
        this.vehicle = Objects.requireNonNull(vehicle, "vehicle must not be null");
        this.spot = Objects.requireNonNull(spot, "spot must not be null");
        this.entryTime = Objects.requireNonNull(entryTime, "entryTime must not be null");
    }

    public static ParkingTicket start(Vehicle vehicle, ParkingSpot spot, LocalDateTime entryTime) {
        return new ParkingTicket(vehicle, spot, entryTime);
    }

    public void close(LocalDateTime exitTime) {
        this.exitTime = Objects.requireNonNull(exitTime, "exitTime must not be null");
    }

    public String getId() {
        return id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getSpot() {
        return spot;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public long getDurationInMinutes() {
        LocalDateTime end = exitTime != null ? exitTime : LocalDateTime.now();
        return Duration.between(entryTime, end).toMinutes();
    }
}

