package com.parkinglot.models.vehicles;

import com.parkinglot.models.spots.ParkingSpot;
import com.parkinglot.models.spots.SpotType;

public class Truck extends Vehicle {

    private final double cargoCapacityTons;

    public Truck(String licensePlate, String color) {
        this(licensePlate, color, 0.0);
    }

    // Overloaded constructor with cargo capacity
    public Truck(String licensePlate, String color, double cargoCapacityTons) {
        super(licensePlate, color, VehicleType.TRUCK);
        this.cargoCapacityTons = cargoCapacityTons;
    }

    public double getCargoCapacityTons() {
        return cargoCapacityTons;
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

