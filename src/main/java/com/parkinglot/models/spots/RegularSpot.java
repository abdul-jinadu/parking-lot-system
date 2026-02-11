package com.parkinglot.models.spots;

import com.parkinglot.models.vehicles.Vehicle;
import com.parkinglot.models.vehicles.VehicleType;

public class RegularSpot extends ParkingSpot {

    public RegularSpot(String id) {
        super(id, SpotType.REGULAR);
    }

    @Override
    public boolean canFitVehicle(Vehicle vehicle) {
        VehicleType type = vehicle.getType();
        return type == VehicleType.MOTORCYCLE
                || type == VehicleType.CAR
                || type == VehicleType.ELECTRIC_CAR;
    }
}

