package com.parkinglot.interfaces;

import java.util.Optional;

import com.parkinglot.models.ParkingTicket;
import com.parkinglot.models.spots.ParkingSpot;
import com.parkinglot.models.vehicles.Vehicle;

// Defines search capabilities for vehicles and spots
public interface Searchable {

    Optional<ParkingTicket> findVehicle(String licensePlate);

    Optional<ParkingSpot> findAvailableSpot(Vehicle vehicle);
}
