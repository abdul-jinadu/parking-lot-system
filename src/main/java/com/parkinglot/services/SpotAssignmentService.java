package com.parkinglot.services;

import java.util.Optional;

import com.parkinglot.models.ParkingFloor;
import com.parkinglot.models.ParkingLot;
import com.parkinglot.models.spots.ParkingSpot;
import com.parkinglot.models.vehicles.Vehicle;

// Assigns the best available spot for a vehicle
public class SpotAssignmentService {

    private final ParkingLot parkingLot;

    public SpotAssignmentService(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    // Finds first available spot across all floors
    public Optional<ParkingSpot> assignSpot(Vehicle vehicle) {
        return parkingLot.findAvailableSpot(vehicle);
    }

    // Finds an available spot on a specific floor
    public Optional<ParkingSpot> assignSpotOnFloor(Vehicle vehicle, int floorNumber) {
        return parkingLot.getFloors().stream()
                .filter(f -> f.getFloorNumber() == floorNumber)
                .findFirst()
                .flatMap(f -> f.findAvailableSpot(vehicle));
    }

    // Returns the floor where the vehicle is parked
    public Optional<ParkingFloor> findVehicleFloor(String licensePlate) {
        for (ParkingFloor floor : parkingLot.getFloors()) {
            if (floor.findVehicleSpot(licensePlate).isPresent()) {
                return Optional.of(floor);
            }
        }
        return Optional.empty();
    }
}
