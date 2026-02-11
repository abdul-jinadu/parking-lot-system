package com.parkinglot.strategies;

import com.parkinglot.models.vehicles.VehicleType;

// Charges a single flat rate regardless of duration
public class FlatRatePricingStrategy implements PricingStrategy {

    private final double flatRate;

    public FlatRatePricingStrategy(double flatRate) {
        if (flatRate < 0) {
            throw new IllegalArgumentException("Flat rate must be non-negative");
        }
        this.flatRate = flatRate;
    }

    @Override
    public double calculatePrice(VehicleType vehicleType, long durationInMinutes) {
        if (durationInMinutes <= 0) return 0.0;
        return flatRate;
    }

    @Override
    public String getStrategyName() {
        return "Flat Rate (R" + String.format("%.2f", flatRate) + ")";
    }
}
