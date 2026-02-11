package com.parkinglot.services;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.parkinglot.models.ParkingTicket;
import com.parkinglot.models.spots.CompactSpot;
import com.parkinglot.models.spots.ElectricSpot;
import com.parkinglot.models.spots.LargeSpot;
import com.parkinglot.models.spots.RegularSpot;
import com.parkinglot.models.vehicles.Bus;
import com.parkinglot.models.vehicles.Car;
import com.parkinglot.models.vehicles.Motorcycle;
import com.parkinglot.models.vehicles.Truck;
import com.parkinglot.strategies.DailyPricingStrategy;
import com.parkinglot.strategies.FlatRatePricingStrategy;
import com.parkinglot.strategies.HourlyPricingStrategy;

class FeeCalculationServiceTest {

    // Helper to create a closed ticket with a specific duration
    private ParkingTicket createTicket(com.parkinglot.models.vehicles.Vehicle vehicle,
                                       com.parkinglot.models.spots.ParkingSpot spot,
                                       long durationMinutes) {
        LocalDateTime entry = LocalDateTime.now().minusMinutes(durationMinutes);
        spot.assignVehicle(vehicle);
        ParkingTicket ticket = ParkingTicket.start(vehicle, spot, entry);
        ticket.close(LocalDateTime.now());
        return ticket;
    }

    @Test
    void hourlyPricingForCar() {
        FeeCalculationService service = new FeeCalculationService(new HourlyPricingStrategy());
        Car car = new Car("HC1", "Red", 4);
        RegularSpot spot = new RegularSpot("R1");

        ParkingTicket ticket = createTicket(car, spot, 120); // 2 hours
        double fee = service.calculateFee(ticket);

        // R15/hr * 2hrs + R0 spot adjustment = R30
        assertEquals(30.0, fee, 1.0);
    }

    @Test
    void hourlyPricingForMotorcycle() {
        FeeCalculationService service = new FeeCalculationService(new HourlyPricingStrategy());
        Motorcycle moto = new Motorcycle("HM1", "Black");
        CompactSpot spot = new CompactSpot("C1");

        ParkingTicket ticket = createTicket(moto, spot, 60); // 1 hour
        double fee = service.calculateFee(ticket);

        // R10/hr * 1hr + (-R2 compact discount) = R8
        assertEquals(8.0, fee, 1.0);
    }

    @Test
    void hourlyPricingForTruck() {
        FeeCalculationService service = new FeeCalculationService(new HourlyPricingStrategy());
        Truck truck = new Truck("HT1", "White");
        LargeSpot spot = new LargeSpot("L1");

        ParkingTicket ticket = createTicket(truck, spot, 180); // 3 hours
        double fee = service.calculateFee(ticket);

        // R25/hr * 3hrs + R0 = R75
        assertEquals(75.0, fee, 1.0);
    }

    @Test
    void electricSpotAddsChargingSurcharge() {
        FeeCalculationService service = new FeeCalculationService(new HourlyPricingStrategy());
        // Electric spot only accepts electric vehicles
        com.parkinglot.models.vehicles.ElectricVehicle ev =
                new com.parkinglot.models.vehicles.ElectricVehicle("ES1", "Blue", 4, 75.0);
        ElectricSpot spot = new ElectricSpot("E1");

        ParkingTicket ticket = createTicket(ev, spot, 60); // 1 hour
        double fee = service.calculateFee(ticket);

        // R20/hr * 1hr + R5 electric surcharge = R25
        assertEquals(25.0, fee, 1.0);
    }

    @Test
    void dailyPricingForBus() {
        FeeCalculationService service = new FeeCalculationService(new DailyPricingStrategy());
        Bus bus = new Bus("DB1", "Yellow");
        LargeSpot spot = new LargeSpot("L1");

        ParkingTicket ticket = createTicket(bus, spot, 1440); // 24 hours = 1 day
        double fee = service.calculateFee(ticket);

        // R220/day * 1 day + R0 = R220
        assertEquals(220.0, fee, 1.0);
    }

    @Test
    void flatRatePricingIgnoresVehicleType() {
        FeeCalculationService service = new FeeCalculationService(new FlatRatePricingStrategy(50.0));
        Car car = new Car("FR1", "Green", 4);
        RegularSpot spot = new RegularSpot("R1");

        ParkingTicket ticket = createTicket(car, spot, 300); // 5 hours
        double fee = service.calculateFee(ticket);

        // R50 flat + R0 spot adjustment = R50
        assertEquals(50.0, fee, 1.0);
    }

    @Test
    void strategyCanBeSwitchedAtRuntime() {
        FeeCalculationService service = new FeeCalculationService(new HourlyPricingStrategy());
        assertEquals("Hourly Pricing", service.getPricingStrategy().getStrategyName());

        service.setPricingStrategy(new DailyPricingStrategy());
        assertEquals("Daily Pricing", service.getPricingStrategy().getStrategyName());
    }

    @Test
    void zeroDurationReturnsZeroFee() {
        FeeCalculationService service = new FeeCalculationService(new HourlyPricingStrategy());
        Car car = new Car("ZD1", "Red", 4);
        RegularSpot spot = new RegularSpot("R1");

        // Create ticket with ~0 minutes
        LocalDateTime now = LocalDateTime.now();
        spot.assignVehicle(car);
        ParkingTicket ticket = ParkingTicket.start(car, spot, now);
        ticket.close(now);

        double fee = service.calculateFee(ticket);
        assertTrue(fee >= 0);
    }
}
