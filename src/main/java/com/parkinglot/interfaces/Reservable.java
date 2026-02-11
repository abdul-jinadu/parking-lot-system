package com.parkinglot.interfaces;

import java.time.LocalDateTime;

import com.parkinglot.models.Customer;

// Defines reservation capabilities for reservable resources
public interface Reservable {

    boolean reserve(Customer customer, LocalDateTime startTime, int durationHours);

    boolean cancelReservation(String reservationId);

    boolean isReserved();
}
