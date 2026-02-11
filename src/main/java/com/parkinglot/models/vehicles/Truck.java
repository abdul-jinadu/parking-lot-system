package com.parkinglot.models.vehicles;

import com.parkinglot.models.spots.ParkingSpot;
import com.parkinglot.models.spots.SpotType;

public class Truck extends Vehicle {

    public Truck(String licensePlate, String color) {
        super(licensePlate, color, VehicleType.TRUCK);
    }

    @Override
    public double getSizeMultiplier() {
        return 1.5;
    }

    @Override
    public boolean canFitInSpot(ParkingSpot spot) {
        return spot.getType() == SpotType.LARGE;
    }

    @Override
    protected double getBaseHourlyRate() {
        return 25.0;
    }
}

