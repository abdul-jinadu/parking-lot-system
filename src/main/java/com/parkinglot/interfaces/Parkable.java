package com.parkinglot.interfaces;

import com.parkinglot.models.spots.ParkingSpot;

// Defines parking operations for parkable entities
public interface Parkable {

    // Attempts to park in the given spot
    boolean park(ParkingSpot spot);

    // Attempts to exit from the current spot
    boolean exit();

    // Calculates the parking fee for the given duration
    double calculateFee(long durationInMinutes);
}
