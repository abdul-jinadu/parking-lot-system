package com.parkinglot.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.parkinglot.models.vehicles.Bus;
import com.parkinglot.models.vehicles.Car;
import com.parkinglot.models.vehicles.Motorcycle;
import com.parkinglot.models.vehicles.Truck;

public class VehicleTest {

    @Test
    void carFeeUsesBaseRateAndSizeMultiplier() {
        Car car = new Car("CA12345", "Blue", 4);
        double fee = car.calculateFee(120);
        assertEquals(15.0 * 1.0 * 2, fee);
    }

    @Test
    void motorcycleFeeUsesSmallerMultiplier() {
        Motorcycle moto = new Motorcycle("MC123", "Red");
        double fee = moto.calculateFee(60);
        assertEquals(10.0 * 0.5 * 1, fee);
    }

    @Test
    void truckAndBusHaveHigherFees() {
        Truck truck = new Truck("TRK1", "White");
        Bus bus = new Bus("BUS1", "Yellow");

        double truckFee = truck.calculateFee(60);
        double busFee = bus.calculateFee(60);

        assertEquals(25.0 * 1.5 * 1, truckFee);
        assertEquals(30.0 * 2.0 * 1, busFee);
    }
}

