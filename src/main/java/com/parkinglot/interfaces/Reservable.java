package com.parkinglot.interfaces;

import com.parkinglot.models.Customer;

import java.time.LocalDateTime;

/**
 * Defines reservation capabilities for reservable resources.
 */
public interface Reservable {

    /**
     * Creates a reservation.
     *
     * @param customer     the customer for whom the reservation is made
     * @param startTime    reservation start time
     * @param durationHours duration of the reservation in hours
     * @return {@code true} if the reservation was successful, {@code false} otherwise
     */
    boolean reserve(Customer customer, LocalDateTime startTime, int durationHours);

    /**
     * Cancels an existing reservation.
     *
     * @param reservationId reservation identifier
     * @return {@code true} if the cancellation was successful, {@code false} otherwise
     */
    boolean cancelReservation(String reservationId);

    /**
     * @return {@code true} if this resource is currently reserved, {@code false} otherwise.
     */
    boolean isReserved();
}

