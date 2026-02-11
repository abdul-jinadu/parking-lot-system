package com.parkinglot.services;

import java.util.Objects;

import com.parkinglot.models.ParkingTicket;
import com.parkinglot.models.spots.SpotType;
import com.parkinglot.strategies.PricingStrategy;

// Calculates parking fees using a pluggable pricing strategy
// Dependency: depends on PricingStrategy
public class FeeCalculationService {

    private PricingStrategy pricingStrategy;

    public FeeCalculationService(PricingStrategy pricingStrategy) {
        this.pricingStrategy = Objects.requireNonNull(pricingStrategy, "pricingStrategy must not be null");
    }

    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = Objects.requireNonNull(pricingStrategy, "pricingStrategy must not be null");
    }

    public PricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }

    // Calculates the fee for a completed ticket
    public double calculateFee(ParkingTicket ticket) {
        Objects.requireNonNull(ticket, "ticket must not be null");
        long minutes = ticket.getDurationInMinutes();
        double baseFee = pricingStrategy.calculatePrice(
                ticket.getVehicle().getType(), minutes);

        // Apply spot type adjustments
        double spotAdjustment = getSpotAdjustment(ticket.getSpot().getType(), minutes);
        return Math.max(0, baseFee + spotAdjustment);
    }

    // Spot type surcharges / discounts per hour
    private double getSpotAdjustment(SpotType spotType, long durationInMinutes) {
        double hours = Math.ceil(durationInMinutes / 60.0);
        return switch (spotType) {
            case COMPACT -> -2.0 * hours;   // discount for compact
            case ELECTRIC -> 5.0 * hours;   // surcharge includes charging
            default -> 0.0;
        };
    }
}
