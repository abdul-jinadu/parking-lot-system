package com.parkinglot.exceptions;

// Thrown when the parking lot has no available spots
public class ParkingFullException extends RuntimeException {

    public ParkingFullException() {
        super("Parking lot is full");
    }

    public ParkingFullException(String message) {
        super(message);
    }
}
