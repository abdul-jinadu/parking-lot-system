package com.parkinglot.services;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkinglot.models.ParkingFloor;
import com.parkinglot.models.ParkingLot;
import com.parkinglot.models.spots.ParkingSpot;
import com.parkinglot.models.vehicles.Car;
import com.parkinglot.models.vehicles.Truck;

class SpotAssignmentServiceTest {

    private ParkingLot lot;
    private SpotAssignmentService assignmentService;

    @BeforeEach
    void setUp() {
        ParkingLot.resetInstance();
        lot = ParkingLot.getInstance("Assignment Test Lot");
        lot.addFloor(1, 3, 1, 0, 0); // floor 1: 5 spots
        lot.addFloor(0, 2, 1, 0, 0); // floor 2: 3 spots
        assignmentService = new SpotAssignmentService(lot);
    }

    @AfterEach
    void tearDown() {
        ParkingLot.resetInstance();
    }

    @Test
    void assignsSpotForCar() {
        Car car = new Car("AS1", "Red", 4);
        Optional<ParkingSpot> spot = assignmentService.assignSpot(car);
        assertTrue(spot.isPresent());
    }

    @Test
    void assignsSpotOnSpecificFloor() {
        Car car = new Car("AS2", "Blue", 4);
        Optional<ParkingSpot> spot = assignmentService.assignSpotOnFloor(car, 2);
        assertTrue(spot.isPresent());
    }

    @Test
    void returnsEmptyWhenFloorHasNoFittingSpot() {
        Truck truck = new Truck("AS3", "White");
        // floor 2 has 1 large - fill it
        ParkingSpot large = assignmentService.assignSpotOnFloor(truck, 2).orElseThrow();
        large.assignVehicle(truck);

        // now floor 2 has no more large spots
        Optional<ParkingSpot> spot = assignmentService.assignSpotOnFloor(new Truck("AS4", "Black"), 2);
        assertFalse(spot.isPresent());
    }

    @Test
    void findVehicleFloorReturnsCorrectFloor() {
        Car car = new Car("FVF1", "Green", 4);
        lot.parkVehicle(car);

        Optional<ParkingFloor> floor = assignmentService.findVehicleFloor("FVF1");
        assertTrue(floor.isPresent());
        assertEquals(1, floor.get().getFloorNumber());
    }

    @Test
    void findVehicleFloorReturnsEmptyWhenNotParked() {
        Optional<ParkingFloor> floor = assignmentService.findVehicleFloor("NOPE");
        assertFalse(floor.isPresent());
    }
}
