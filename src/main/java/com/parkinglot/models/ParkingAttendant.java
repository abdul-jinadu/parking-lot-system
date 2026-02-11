package com.parkinglot.models;

import java.util.Objects;

import com.parkinglot.models.vehicles.Vehicle;

// Association: attendant manages a parking lot but does not own it
public class ParkingAttendant {

    private final String id;
    private final String name;
    private final ParkingLot parkingLot; // association

    public ParkingAttendant(String id, String name, ParkingLot parkingLot) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.parkingLot = Objects.requireNonNull(parkingLot, "parkingLot must not be null");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    // Attendant parks a vehicle on behalf of a customer
    public ParkingTicket parkVehicle(Vehicle vehicle) {
        return parkingLot.parkVehicle(vehicle);
    }

    // Attendant processes vehicle exit
    public ParkingTicket exitVehicle(String licensePlate) {
        return parkingLot.exitVehicle(licensePlate);
    }
}
