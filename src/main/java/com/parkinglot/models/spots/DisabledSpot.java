package com.parkinglot.models.spots;

import com.parkinglot.models.vehicles.Vehicle;

public class DisabledSpot extends ParkingSpot {

    public DisabledSpot(String id) {
        super(id, SpotType.DISABLED);
        setReserved(true);
    }

    @Override
    public boolean canFitVehicle(Vehicle vehicle) {
        return true;
    }
}

