package com.parkinglot.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.parkinglot.models.spots.CompactSpot;
import com.parkinglot.models.spots.DisabledSpot;
import com.parkinglot.models.spots.ElectricSpot;
import com.parkinglot.models.spots.LargeSpot;
import com.parkinglot.models.spots.ParkingSpot;
import com.parkinglot.models.spots.RegularSpot;
import com.parkinglot.models.spots.SpotType;
import com.parkinglot.models.vehicles.Vehicle;

// Represents a single floor in the parking lot
// Composition: spots are created and owned by this floor
public class ParkingFloor {

    private final int floorNumber;
    private final List<ParkingSpot> spots; // composition - spots die with floor

    public ParkingFloor(int floorNumber, int compactCount, int regularCount,
                        int largeCount, int electricCount, int disabledCount) {
        if (floorNumber < 1) {
            throw new IllegalArgumentException("Floor number must be positive");
        }
        this.floorNumber = floorNumber;
        this.spots = new ArrayList<>();
        initializeSpots(compactCount, regularCount, largeCount, electricCount, disabledCount);
    }

    // Creates spots owned by this floor (composition)
    private void initializeSpots(int compact, int regular, int large, int electric, int disabled) {
        String prefix = "F" + floorNumber + "-";
        int counter = 1;

        for (int i = 0; i < compact; i++) {
            spots.add(new CompactSpot(prefix + "C" + counter++));
        }
        for (int i = 0; i < regular; i++) {
            spots.add(new RegularSpot(prefix + "R" + counter++));
        }
        for (int i = 0; i < large; i++) {
            spots.add(new LargeSpot(prefix + "L" + counter++));
        }
        for (int i = 0; i < electric; i++) {
            spots.add(new ElectricSpot(prefix + "E" + counter++));
        }
        for (int i = 0; i < disabled; i++) {
            spots.add(new DisabledSpot(prefix + "D" + counter++));
        }
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public List<ParkingSpot> getSpots() {
        return Collections.unmodifiableList(spots);
    }

    public int getTotalSpots() {
        return spots.size();
    }

    public int getOccupiedCount() {
        return (int) spots.stream().filter(ParkingSpot::isOccupied).count();
    }

    public int getAvailableCount() {
        return (int) spots.stream().filter(ParkingSpot::isAvailable).count();
    }

    // Returns occupancy as a percentage
    public double getOccupancyPercentage() {
        if (spots.isEmpty()) return 0.0;
        return (getOccupiedCount() * 100.0) / getTotalSpots();
    }

    // Counts available spots of a specific type
    public long getAvailableCountByType(SpotType type) {
        return spots.stream()
                .filter(s -> s.getType() == type && s.isAvailable())
                .count();
    }

    // Finds first available spot that fits the given vehicle
    public Optional<ParkingSpot> findAvailableSpot(Vehicle vehicle) {
        return spots.stream()
                .filter(s -> s.isAvailable() && s.canFitVehicle(vehicle))
                .findFirst();
    }

    // Finds a spot by id on this floor
    public Optional<ParkingSpot> findSpotById(String spotId) {
        return spots.stream()
                .filter(s -> s.getId().equals(spotId))
                .findFirst();
    }

    // Finds a vehicle by license plate on this floor
    public Optional<ParkingSpot> findVehicleSpot(String licensePlate) {
        return spots.stream()
                .filter(s -> s.isOccupied()
                        && s.getCurrentVehicle() != null
                        && s.getCurrentVehicle().getLicensePlate().equalsIgnoreCase(licensePlate))
                .findFirst();
    }
}
