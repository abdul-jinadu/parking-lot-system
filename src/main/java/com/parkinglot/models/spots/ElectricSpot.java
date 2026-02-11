package com.parkinglot.models.spots;

import com.parkinglot.models.vehicles.Vehicle;
import com.parkinglot.models.vehicles.VehicleType;

public class ElectricSpot extends ParkingSpot {

    public ElectricSpot(String id) {
        super(id, SpotType.ELECTRIC);
    }

    @Override
    public boolean canFitVehicle(Vehicle vehicle) {
        return vehicle.getType() == VehicleType.ELECTRIC_CAR
                || vehicle.getType() == VehicleType.MOTORCYCLE;
    }
}

