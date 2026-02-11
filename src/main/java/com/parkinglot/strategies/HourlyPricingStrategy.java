package com.parkinglot.strategies;

import java.util.Map;

import com.parkinglot.models.vehicles.VehicleType;

// Charges per hour based on vehicle type
public class HourlyPricingStrategy implements PricingStrategy {

    // Base hourly rates in Rands
    private static final Map<VehicleType, Double> HOURLY_RATES = Map.of(
            VehicleType.MOTORCYCLE, 10.0,
            VehicleType.CAR, 15.0,
            VehicleType.ELECTRIC_CAR, 20.0,
            VehicleType.TRUCK, 25.0,
            VehicleType.BUS, 30.0
    );

    @Override
    public double calculatePrice(VehicleType vehicleType, long durationInMinutes) {
        if (durationInMinutes <= 0) return 0.0;
        double hours = Math.ceil(durationInMinutes / 60.0);
        double rate = HOURLY_RATES.getOrDefault(vehicleType, 15.0);
        return rate * hours;
    }

    @Override
    public String getStrategyName() {
        return "Hourly Pricing";
    }
}
