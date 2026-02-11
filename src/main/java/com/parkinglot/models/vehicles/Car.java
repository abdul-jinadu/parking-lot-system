package com.parkinglot.models.vehicles;

import com.parkinglot.models.spots.ParkingSpot;
import com.parkinglot.models.spots.SpotType;

public class Car extends Vehicle {

    private int numberOfDoors;

    public Car(String licensePlate, String color, int numberOfDoors) {
        this(licensePlate, color, numberOfDoors, VehicleType.CAR);
    }

    // Protected constructor for subclasses to specify a different VehicleType
    protected Car(String licensePlate, String color, int numberOfDoors, VehicleType type) {
        super(licensePlate, color, type);
        this.numberOfDoors = numberOfDoors;
    }

    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(int numberOfDoors) {
        if (numberOfDoors <= 0) {
            throw new IllegalArgumentException("numberOfDoors must be positive");
        }
        this.numberOfDoors = numberOfDoors;
    }

    @Override
    public double getSizeMultiplier() {
        return 1.0;
    }

    @Override
    public boolean canFitInSpot(ParkingSpot spot) {
        SpotType type = spot.getType();
        return type == SpotType.REGULAR
                || type == SpotType.LARGE
                || type == SpotType.ELECTRIC
                || type == SpotType.DISABLED;
    }

    @Override
    protected double getBaseHourlyRate() {
        return 15.0;
    }
}

