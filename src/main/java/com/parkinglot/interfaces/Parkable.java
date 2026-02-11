package com.parkinglot.interfaces;

import com.parkinglot.models.spots.ParkingSpot;

/**
 * Defines parking-related operations that can be performed by a parkable entity.
 */
public interface Parkable {

    /**
     * Attempts to park in the provided spot.
     *
     * @param spot the target parking spot
     * @return {@code true} if parking was successful, {@code false} otherwise
     */
    boolean park(ParkingSpot spot);

    /**
     * Attempts to exit from the current parking spot.
     *
     * @return {@code true} if exit was successful, {@code false} otherwise
     */
    boolean exit();

    /**
     * Calculates the parking fee based on the provided duration.
     *
     * @param durationInMinutes parking duration in minutes
     * @return calculated fee
     */
    double calculateFee(long durationInMinutes);
}

