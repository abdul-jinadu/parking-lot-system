package com.parkinglot.strategies;

import java.util.Map;

import com.parkinglot.models.vehicles.VehicleType;

// Charges a flat daily rate based on vehicle type
public class DailyPricingStrategy implements PricingStrategy {

    private static final Map<VehicleType, Double> DAILY_RATES = Map.of(
            VehicleType.MOTORCYCLE, 60.0,
            VehicleType.CAR, 100.0,
            VehicleType.ELECTRIC_CAR, 130.0,
            VehicleType.TRUCK, 180.0,
            VehicleType.BUS, 220.0
    );

    @Override
    public double calculatePrice(VehicleType vehicleType, long durationInMinutes) {
        if (durationInMinutes <= 0) return 0.0;
        double days = Math.ceil(durationInMinutes / (60.0 * 24));
        double rate = DAILY_RATES.getOrDefault(vehicleType, 100.0);
        return rate * days;
    }

    @Override
    public String getStrategyName() {
        return "Daily Pricing";
    }
}
