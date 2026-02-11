package com.parkinglot.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.parkinglot.interfaces.Searchable;
import com.parkinglot.models.spots.ParkingSpot;
import com.parkinglot.models.spots.SpotType;
import com.parkinglot.models.vehicles.Vehicle;

// Singleton parking lot
// Composition: floors are created and owned by the lot
// Aggregation: parked vehicles exist independently of the lot
public class ParkingLot implements Searchable {

    private static ParkingLot instance;

    private final String name;
    private final List<ParkingFloor> floors;              // composition - floors die with lot
    private final List<Vehicle> parkedVehicles;           // aggregation - vehicles exist independently
    private final List<ParkingTicket> activeTickets;

    private ParkingLot(String name) {
        this.name = name;
        this.floors = new ArrayList<>();
        this.parkedVehicles = new ArrayList<>();
        this.activeTickets = new ArrayList<>();
    }

    // Singleton access
    public static synchronized ParkingLot getInstance(String name) {
        if (instance == null) {
            instance = new ParkingLot(name);
        }
        return instance;
    }

    // Reset for testing
    public static synchronized void resetInstance() {
        instance = null;
    }

    // Adds a floor (composition - lot owns the floor)
    public void addFloor(int compactCount, int regularCount, int largeCount,
                         int electricCount, int disabledCount) {
        int floorNumber = floors.size() + 1;
        floors.add(new ParkingFloor(floorNumber, compactCount, regularCount,
                largeCount, electricCount, disabledCount));
    }

    public String getName() {
        return name;
    }

    public List<ParkingFloor> getFloors() {
        return Collections.unmodifiableList(floors);
    }

    public List<Vehicle> getParkedVehicles() {
        return Collections.unmodifiableList(parkedVehicles);
    }

    public List<ParkingTicket> getActiveTickets() {
        return Collections.unmodifiableList(activeTickets);
    }

    // Parks a vehicle in the lot, assigns a spot automatically
    public ParkingTicket parkVehicle(Vehicle vehicle) {
        Optional<ParkingSpot> spot = findAvailableSpot(vehicle);
        if (spot.isEmpty()) {
            return null;
        }
        boolean parked = vehicle.park(spot.get());
        if (!parked) {
            return null;
        }
        parkedVehicles.add(vehicle);
        ParkingTicket ticket = vehicle.getActiveTicket();
        activeTickets.add(ticket);
        return ticket;
    }

    // Exits a vehicle from the lot by license plate
    public ParkingTicket exitVehicle(String licensePlate) {
        Optional<ParkingTicket> found = findVehicle(licensePlate);
        if (found.isEmpty()) {
            return null;
        }
        ParkingTicket ticket = found.get();
        Vehicle vehicle = ticket.getVehicle();
        vehicle.exit();
        parkedVehicles.remove(vehicle);
        activeTickets.remove(ticket);
        return ticket;
    }

    // Total capacity across all floors
    public int getTotalCapacity() {
        return floors.stream().mapToInt(ParkingFloor::getTotalSpots).sum();
    }

    // Total occupied across all floors
    public int getTotalOccupied() {
        return floors.stream().mapToInt(ParkingFloor::getOccupiedCount).sum();
    }

    // Total available across all floors
    public int getTotalAvailable() {
        return getTotalCapacity() - getTotalOccupied();
    }

    // Available count by spot type
    public long getAvailableByType(SpotType type) {
        return floors.stream()
                .mapToLong(f -> f.getAvailableCountByType(type))
                .sum();
    }

    @Override
    public Optional<ParkingTicket> findVehicle(String licensePlate) {
        return activeTickets.stream()
                .filter(t -> t.getVehicle().getLicensePlate().equalsIgnoreCase(licensePlate))
                .findFirst();
    }

    @Override
    public Optional<ParkingSpot> findAvailableSpot(Vehicle vehicle) {
        for (ParkingFloor floor : floors) {
            Optional<ParkingSpot> spot = floor.findAvailableSpot(vehicle);
            if (spot.isPresent()) {
                return spot;
            }
        }
        return Optional.empty();
    }
}
