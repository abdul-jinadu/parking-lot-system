package com.parkinglot.strategies;

import com.parkinglot.models.vehicles.VehicleType;

// Strategy interface for different pricing models
public interface PricingStrategy {

    // Calculates price based on vehicle type and parking duration
    double calculatePrice(VehicleType vehicleType, long durationInMinutes);

    // Returns the name of this pricing strategy
    String getStrategyName();
}
