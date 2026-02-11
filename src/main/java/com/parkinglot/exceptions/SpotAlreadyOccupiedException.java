package com.parkinglot.exceptions;

// Thrown when attempting to park in an already occupied spot
public class SpotAlreadyOccupiedException extends RuntimeException {

    public SpotAlreadyOccupiedException(String spotId) {
        super("Spot already occupied: " + spotId);
    }
}
