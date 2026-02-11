package com.parkinglot.models;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkinglot.models.vehicles.Car;
import com.parkinglot.models.vehicles.Motorcycle;
import com.parkinglot.models.vehicles.Truck;

class ParkingLotTest {

    private ParkingLot lot;

    @BeforeEach
    void setUp() {
        ParkingLot.resetInstance();
        lot = ParkingLot.getInstance("Test Lot");
        // 2 compact, 5 regular, 2 large, 1 electric, 1 disabled = 11 spots
        lot.addFloor(2, 5, 2, 1, 1);
    }

    @AfterEach
    void tearDown() {
        ParkingLot.resetInstance();
    }

    @Test
    void singletonReturnsSameInstance() {
        ParkingLot same = ParkingLot.getInstance("Test Lot");
        assertEquals(lot, same);
    }

    @Test
    void totalCapacityMatchesFloorSpots() {
        assertEquals(11, lot.getTotalCapacity());
    }

    @Test
    void parkVehicleIncreasesOccupancy() {
        Car car = new Car("CA001", "Red", 4);
        ParkingTicket ticket = lot.parkVehicle(car);

        assertNotNull(ticket);
        assertEquals(1, lot.getTotalOccupied());
        assertEquals(10, lot.getTotalAvailable());
    }

    @Test
    void exitVehicleDecreasesOccupancy() {
        Car car = new Car("CA002", "Blue", 4);
        lot.parkVehicle(car);
        assertEquals(1, lot.getTotalOccupied());

        ParkingTicket exitTicket = lot.exitVehicle("CA002");
        assertNotNull(exitTicket);
        assertEquals(0, lot.getTotalOccupied());
    }

    @Test
    void findVehicleByLicensePlate() {
        Car car = new Car("FIND1", "Black", 4);
        lot.parkVehicle(car);

        Optional<ParkingTicket> found = lot.findVehicle("FIND1");
        assertTrue(found.isPresent());
        assertEquals("FIND1", found.get().getVehicle().getLicensePlate());
    }

    @Test
    void findVehicleReturnsEmptyWhenNotFound() {
        Optional<ParkingTicket> found = lot.findVehicle("NONE");
        assertFalse(found.isPresent());
    }

    @Test
    void parkReturnsNullWhenNoSpotAvailable() {
        // fill all large spots with trucks (only 2 large spots)
        lot.parkVehicle(new Truck("T1", "White"));
        lot.parkVehicle(new Truck("T2", "White"));

        // third truck should fail
        ParkingTicket ticket = lot.parkVehicle(new Truck("T3", "White"));
        assertNull(ticket);
    }

    @Test
    void motorcycleCanParkWhenRegularSpotsAvailable() {
        Motorcycle moto = new Motorcycle("M1", "Green");
        ParkingTicket ticket = lot.parkVehicle(moto);
        assertNotNull(ticket);
    }

    @Test
    void aggregationVehicleExistsAfterExit() {
        // demonstrates aggregation: vehicle exists independently of lot
        Car car = new Car("AGG1", "Silver", 4);
        lot.parkVehicle(car);
        lot.exitVehicle("AGG1");

        // vehicle still exists and is usable
        assertEquals("AGG1", car.getLicensePlate());
        assertEquals("Silver", car.getColor());
        assertNull(car.getActiveTicket());
    }

    @Test
    void compositionFloorsExistWithLot() {
        // demonstrates composition: floors belong to lot
        assertEquals(1, lot.getFloors().size());
        assertEquals(1, lot.getFloors().get(0).getFloorNumber());
    }

    @Test
    void multipleFloorsTrackSeparately() {
        lot.addFloor(1, 3, 1, 0, 0); // floor 2: 5 spots
        assertEquals(2, lot.getFloors().size());
        assertEquals(16, lot.getTotalCapacity()); // 11 + 5

        // park on floor 1 first (first available)
        Car car = new Car("MF1", "Blue", 4);
        lot.parkVehicle(car);
        assertTrue(lot.getTotalOccupied() > 0);
    }
}
