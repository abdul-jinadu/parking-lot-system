package com.parkinglot.models.vehicles;

import com.parkinglot.models.spots.ParkingSpot;
import com.parkinglot.models.spots.SpotType;

public class Bus extends Vehicle {

    public Bus(String licensePlate, String color) {
        super(licensePlate, color, VehicleType.BUS);
    }

    @Override
    public double getSizeMultiplier() {
        return 2.0;
    }

    @Override
    public boolean canFitInSpot(ParkingSpot spot) {
        return spot.getType() == SpotType.LARGE;
    }

    @Override
    protected double getBaseHourlyRate() {
        return 30.0;
    }
}

