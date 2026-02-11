package com.parkinglot.exceptions;

// Thrown when a spot is invalid for the requested operation
public class InvalidSpotException extends RuntimeException {

    public InvalidSpotException(String message) {
        super(message);
    }
}
