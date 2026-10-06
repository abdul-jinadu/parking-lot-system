// Class created with Help of Claude
package com.parkinglot.services;

import com.parkinglot.models.ParkingTicket;
import com.parkinglot.models.vehicles.Vehicle;

public class FindCar {
    private final ParkingService parkingService;

    public FindCar(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    public void findCar(String color, String vehicleType) {

        boolean found = false;

        for (Vehicle vehicle : parkingService.getParkingLot().getParkedVehicles()) {

            if (vehicle.getColor().equalsIgnoreCase(color)
                    && vehicle.getType().toString().equalsIgnoreCase(vehicleType)) {

                ParkingTicket ticket = vehicle.getActiveTicket();

                System.out.println("\nCar Found!");
                System.out.println("Color: " + vehicle.getColor());
                System.out.println("Vehicle Type: " + vehicle.getType());
                System.out.println("Parking Spot: " + ticket.getSpot().getId());

                found = true;
            }
        }

        if (!found) {
            System.out.println("\nNo matching car was found.");
        }
    }
}
