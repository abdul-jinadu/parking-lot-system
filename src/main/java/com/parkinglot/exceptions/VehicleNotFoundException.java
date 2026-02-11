package com.parkinglot.exceptions;

// Thrown when a vehicle cannot be found by license plate
public class VehicleNotFoundException extends RuntimeException {

    public VehicleNotFoundException(String licensePlate) {
        super("Vehicle not found: " + licensePlate);
    }
}
