package com.parkinglot.models.spots;

import com.parkinglot.models.vehicles.Vehicle;
import com.parkinglot.models.vehicles.VehicleType;

public class LargeSpot extends ParkingSpot {

    public LargeSpot(String id) {
        super(id, SpotType.LARGE);
    }

    @Override
    public boolean canFitVehicle(Vehicle vehicle) {
        VehicleType type = vehicle.getType();
        return type == VehicleType.MOTORCYCLE
                || type == VehicleType.CAR
                || type == VehicleType.ELECTRIC_CAR
                || type == VehicleType.TRUCK
                || type == VehicleType.BUS;
    }
}

