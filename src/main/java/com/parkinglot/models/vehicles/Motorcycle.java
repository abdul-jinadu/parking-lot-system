package com.parkinglot.models.vehicles;

import com.parkinglot.models.spots.ParkingSpot;

public class Motorcycle extends Vehicle {

    public Motorcycle(String licensePlate, String color) {
        super(licensePlate, color, VehicleType.MOTORCYCLE);
    }

    @Override
    public double getSizeMultiplier() {
        return 0.5;
    }

    @Override
    public boolean canFitInSpot(ParkingSpot spot) {
        return true; // motorcycle fits in any spot
    }

    @Override
    protected double getBaseHourlyRate() {
        return 10.0;
    }
}

