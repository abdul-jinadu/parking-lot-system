package com.parkinglot.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.parkinglot.models.Customer;
import com.parkinglot.models.ParkingLot;
import com.parkinglot.models.spots.ParkingSpot;
import com.parkinglot.models.vehicles.Vehicle;

// Manages spot reservations for customers
public class ReservationService {

    private final ParkingLot parkingLot;
    // Maps reservation id -> reserved spot
    private final Map<String, ParkingSpot> reservations = new HashMap<>();

    public ReservationService(ParkingLot parkingLot) {
        this.parkingLot = Objects.requireNonNull(parkingLot);
    }

    // Reserves a spot for a customer's vehicle
    public Optional<String> reserveSpot(Customer customer, Vehicle vehicle,
                                         LocalDateTime startTime, int durationHours) {
        Optional<ParkingSpot> available = parkingLot.findAvailableSpot(vehicle);
        if (available.isEmpty()) {
            return Optional.empty();
        }

        ParkingSpot spot = available.get();
        spot.setReserved(true);

        String reservationId = UUID.randomUUID().toString();
        reservations.put(reservationId, spot);
        return Optional.of(reservationId);
    }

    // Cancels an existing reservation
    public boolean cancelReservation(String reservationId) {
        ParkingSpot spot = reservations.remove(reservationId);
        if (spot == null) {
            return false;
        }
        spot.setReserved(false);
        return true;
    }

    // Checks if a reservation exists
    public boolean isReserved(String reservationId) {
        return reservations.containsKey(reservationId);
    }

    // Gets the reserved spot for a reservation
    public Optional<ParkingSpot> getReservedSpot(String reservationId) {
        return Optional.ofNullable(reservations.get(reservationId));
    }

    public int getActiveReservationCount() {
        return reservations.size();
    }
}
