package com.parkinglot.services;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkinglot.exceptions.ParkingFullException;
import com.parkinglot.exceptions.VehicleNotFoundException;
import com.parkinglot.models.ParkingLot;
import com.parkinglot.models.ParkingTicket;
import com.parkinglot.models.Receipt;
import com.parkinglot.models.vehicles.Car;
import com.parkinglot.models.vehicles.Truck;
import com.parkinglot.payment.CashPayment;
import com.parkinglot.strategies.HourlyPricingStrategy;

class ParkingServiceTest {

    private ParkingLot lot;
    private ParkingService service;

    @BeforeEach
    void setUp() {
        ParkingLot.resetInstance();
        lot = ParkingLot.getInstance("Test Lot");
        lot.addFloor(1, 3, 1, 1, 1); // 7 spots
        FeeCalculationService feeService = new FeeCalculationService(new HourlyPricingStrategy());
        service = new ParkingService(lot, feeService);
    }

    @AfterEach
    void tearDown() {
        ParkingLot.resetInstance();
    }

    @Test
    void entryReturnsTicket() {
        Car car = new Car("SVC1", "Red", 4);
        ParkingTicket ticket = service.entry(car);
        assertNotNull(ticket);
        assertEquals("SVC1", ticket.getVehicle().getLicensePlate());
    }

    @Test
    void entryThrowsWhenFull() {
        // fill all large spots
        service.entry(new Truck("T1", "W", 5.0));

        // no more large spots available
        assertThrows(ParkingFullException.class,
                () -> service.entry(new Truck("T2", "W", 3.0)));
    }

    @Test
    void exitProcessesPaymentAndReturnsReceipt() {
        Car car = new Car("EXIT1", "Blue", 4);
        service.entry(car);

        CashPayment payment = new CashPayment(50.0);
        Receipt receipt = service.exit("EXIT1", payment);

        assertNotNull(receipt);
        assertNotNull(receipt.getTicket());
    }

    @Test
    void exitThrowsWhenVehicleNotFound() {
        assertThrows(VehicleNotFoundException.class,
                () -> service.exit("NOPE", new CashPayment(10.0)));
    }

    @Test
    void searchFindsParkedVehicle() {
        Car car = new Car("SRCH1", "Grey", 4);
        service.entry(car);

        Optional<ParkingTicket> found = service.search("SRCH1");
        assertTrue(found.isPresent());
    }

    @Test
    void searchReturnsEmptyForUnknownPlate() {
        Optional<ParkingTicket> found = service.search("UNKNOWN");
        assertTrue(found.isEmpty());
    }
}
