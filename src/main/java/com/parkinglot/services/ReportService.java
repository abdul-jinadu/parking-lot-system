package com.parkinglot.services;

import java.util.Objects;

import com.parkinglot.models.ParkingFloor;
import com.parkinglot.models.ParkingLot;
import com.parkinglot.models.spots.SpotType;

// Generates reports and statistics about the parking lot
public class ReportService {

    private final ParkingLot parkingLot;

    public ReportService(ParkingLot parkingLot) {
        this.parkingLot = Objects.requireNonNull(parkingLot);
    }

    // Generates a full status summary as a formatted string
    public String generateStatusReport() {
        StringBuilder sb = new StringBuilder();
        int total = parkingLot.getTotalCapacity();
        int occupied = parkingLot.getTotalOccupied();
        int available = parkingLot.getTotalAvailable();
        double occupancy = total > 0 ? (occupied * 100.0 / total) : 0.0;

        sb.append("\n");
        sb.append("╔════════════════════════════════════════╗\n");
        sb.append("║     PARKING LOT MANAGEMENT SYSTEM      ║\n");
        sb.append("╚════════════════════════════════════════╝\n");
        sb.append("\nCurrent Status:\n");
        sb.append("-----------------------------------------\n");
        sb.append(String.format("Total Capacity: %d spots%n", total));
        sb.append(String.format("Occupied: %d spots (%.1f%%)%n", occupied, occupancy));
        sb.append(String.format("Available: %d spots (%.1f%%)%n", available, 100.0 - occupancy));

        sb.append("\nAvailable Spots by Type:\n");
        for (SpotType type : SpotType.values()) {
            long count = parkingLot.getAvailableByType(type);
            sb.append(String.format("  %-12s %d spots%n", type.name() + ":", count));
        }

        sb.append("\nFloor-wise Occupancy:\n");
        for (ParkingFloor floor : parkingLot.getFloors()) {
            int floorTotal = floor.getTotalSpots();
            int floorOccupied = floor.getOccupiedCount();
            double floorPct = floor.getOccupancyPercentage();
            sb.append(String.format("  Floor %d: %d/%d (%.1f%%)%n",
                    floor.getFloorNumber(), floorOccupied, floorTotal, floorPct));
        }

        return sb.toString();
    }

    // Quick summary line
    public String generateQuickSummary() {
        return String.format("[%s] %d/%d occupied",
                parkingLot.getName(),
                parkingLot.getTotalOccupied(),
                parkingLot.getTotalCapacity());
    }
}
