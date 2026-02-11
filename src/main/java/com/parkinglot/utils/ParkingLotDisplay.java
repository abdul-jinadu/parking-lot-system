package com.parkinglot.utils;

import com.parkinglot.models.ParkingFloor;
import com.parkinglot.models.ParkingLot;
import com.parkinglot.models.spots.ParkingSpot;

// Formats parking lot state for console output
public final class ParkingLotDisplay {

    private ParkingLotDisplay() {
        // utility class
    }

    // Prints the main menu
    public static String getMenu() {
        return """
                
                ╔════════════════════════════════════════╗
                ║     PARKING LOT MANAGEMENT SYSTEM      ║
                ╚════════════════════════════════════════╝
                
                Menu:
                1. Park Vehicle
                2. Exit Vehicle
                3. Search Vehicle
                4. View Parking Status
                5. Make Reservation
                6. Cancel Reservation
                7. Generate Reports
                8. Exit System
                
                Enter choice:\s""";
    }

    // Prints the vehicle type selection menu
    public static String getVehicleTypeMenu() {
        return """
                
                Select Vehicle Type:
                1. Car
                2. Motorcycle
                3. Truck
                4. Bus
                5. Electric Vehicle
                
                Enter choice:\s""";
    }

    // Prints the payment method selection menu
    public static String getPaymentMenu() {
        return """
                
                Select Payment Method:
                1. Cash
                2. Card
                3. Digital Wallet
                
                Enter choice:\s""";
    }

    // Displays a visual map of a floor's spots
    public static String getFloorMap(ParkingFloor floor) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%nFloor %d:%n", floor.getFloorNumber()));
        sb.append("-".repeat(40)).append("\n");

        for (ParkingSpot spot : floor.getSpots()) {
            String status = spot.isOccupied() ? "X" : (spot.isReserved() ? "R" : "O");
            sb.append(String.format("[%s:%s] ", spot.getId(), status));
        }
        sb.append("\n");
        sb.append("O = Open, X = Occupied, R = Reserved\n");
        return sb.toString();
    }

    // Displays a full map of all floors
    public static String getFullMap(ParkingLot lot) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%n=== %s ===%n", lot.getName()));
        for (ParkingFloor floor : lot.getFloors()) {
            sb.append(getFloorMap(floor));
        }
        return sb.toString();
    }
}
