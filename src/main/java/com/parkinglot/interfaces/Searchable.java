package com.parkinglot.interfaces;

import com.parkinglot.models.spots.ParkingSpot;
import com.parkinglot.models.vehicles.Vehicle;
import com.parkinglot.models.ParkingTicket;

import java.util.Optional;

/**
 * Defines search capabilities for finding vehicles and available spots.
 */
public interface Searchable {

    /**
     * Finds an active parking ticket for the given license plate.
     *
     * @param licensePlate license plate value
     * @return optional parking ticket if found
     */
    Optional<ParkingTicket> findVehicle(String licensePlate);

    /**
     * Finds an available parking spot that can accommodate the given vehicle.
     *
     * @param vehicle vehicle to be parked
     * @return optional parking spot if found
     */
    Optional<ParkingSpot> findAvailableSpot(Vehicle vehicle);
}

