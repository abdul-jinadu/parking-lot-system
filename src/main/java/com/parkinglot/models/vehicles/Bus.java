package com.parkinglot.models.vehicles;

import com.parkinglot.models.spots.ParkingSpot;
import com.parkinglot.models.spots.SpotType;

public class Bus extends Vehicle {

    private final int passengerCapacity;

    public Bus(String licensePlate, String color) {
        this(licensePlate, color, 0);
    }

    // Overloaded constructor with passenger capacity
    public Bus(String licensePlate, String color, int passengerCapacity) {
        super(licensePlate, color, VehicleType.BUS);
        this.passengerCapacity = passengerCapacity;
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
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

