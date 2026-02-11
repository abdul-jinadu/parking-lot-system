package com.parkinglot.models.vehicles;

import com.parkinglot.models.spots.ParkingSpot;

public class Motorcycle extends Vehicle {

    private final int engineCapacityCc;

    public Motorcycle(String licensePlate, String color) {
        this(licensePlate, color, 0);
    }

    // Overloaded constructor with engine capacity
    public Motorcycle(String licensePlate, String color, int engineCapacityCc) {
        super(licensePlate, color, VehicleType.MOTORCYCLE);
        this.engineCapacityCc = engineCapacityCc;
    }

    public int getEngineCapacityCc() {
        return engineCapacityCc;
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

