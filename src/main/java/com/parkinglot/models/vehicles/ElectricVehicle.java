package com.parkinglot.models.vehicles;

import com.parkinglot.interfaces.Chargeable;
import com.parkinglot.models.spots.ParkingSpot;
import com.parkinglot.models.spots.SpotType;

public class ElectricVehicle extends Car implements Chargeable {

    private final double batteryCapacityKwh;
    private double stateOfChargePercent;
    private boolean charging;

    public ElectricVehicle(String licensePlate, String color, int numberOfDoors, double batteryCapacityKwh) {
        super(licensePlate, color, numberOfDoors);
        this.batteryCapacityKwh = batteryCapacityKwh;
        this.stateOfChargePercent = 100.0;
    }

    public double getBatteryCapacityKwh() {
        return batteryCapacityKwh;
    }

    public double getStateOfChargePercent() {
        return stateOfChargePercent;
    }

    public void setStateOfChargePercent(double stateOfChargePercent) {
        if (stateOfChargePercent < 0 || stateOfChargePercent > 100) {
            throw new IllegalArgumentException("stateOfChargePercent must be between 0 and 100");
        }
        this.stateOfChargePercent = stateOfChargePercent;
    }

    @Override
    public void startCharging() {
        this.charging = true;
    }

    @Override
    public void stopCharging() {
        this.charging = false;
    }

    @Override
    public double getChargingRate() {
        return 11.0; // kW
    }

    @Override
    public boolean isFullyCharged() {
        return stateOfChargePercent >= 99.0;
    }

    @Override
    public boolean canFitInSpot(ParkingSpot spot) {
        SpotType type = spot.getType();
        return type == SpotType.ELECTRIC || type == SpotType.REGULAR || type == SpotType.LARGE || type == SpotType.DISABLED;
    }

    @Override
    protected double getBaseHourlyRate() {
        return 20.0;
    }
}

