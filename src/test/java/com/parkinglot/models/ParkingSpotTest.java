package com.parkinglot.models;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.parkinglot.models.spots.CompactSpot;
import com.parkinglot.models.spots.ElectricSpot;
import com.parkinglot.models.spots.LargeSpot;
import com.parkinglot.models.spots.RegularSpot;
import com.parkinglot.models.vehicles.Car;
import com.parkinglot.models.vehicles.Motorcycle;
import com.parkinglot.models.vehicles.Truck;

public class ParkingSpotTest {

    @Test
    void motorcycleCanParkInAnySpot() {
        Motorcycle moto = new Motorcycle("MC1", "Black");

        assertTrue(new CompactSpot("C1").canFitVehicle(moto));
        assertTrue(new RegularSpot("R1").canFitVehicle(moto));
        assertTrue(new LargeSpot("L1").canFitVehicle(moto));
        assertTrue(new ElectricSpot("E1").canFitVehicle(moto));
    }

    @Test
    void carCannotParkInCompactSpot() {
        Car car = new Car("CA1", "Blue", 4);
        CompactSpot compact = new CompactSpot("C1");
        RegularSpot regular = new RegularSpot("R1");

        assertFalse(compact.canFitVehicle(car));
        assertTrue(regular.canFitVehicle(car));
    }

    @Test
    void truckNeedsLargeSpot() {
        Truck truck = new Truck("TRK1", "White");
        CompactSpot compact = new CompactSpot("C1");
        RegularSpot regular = new RegularSpot("R1");
        LargeSpot large = new LargeSpot("L1");

        assertFalse(compact.canFitVehicle(truck));
        assertFalse(regular.canFitVehicle(truck));
        assertTrue(large.canFitVehicle(truck));
    }
}

