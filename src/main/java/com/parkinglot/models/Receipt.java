package com.parkinglot.models;

import com.parkinglot.payment.PaymentStatus;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Receipt {

    private final String id;
    private final ParkingTicket ticket;
    private final double amount;
    private final LocalDateTime issuedAt;
    private final PaymentStatus status;

    public Receipt(ParkingTicket ticket, double amount, PaymentStatus status) {
        this.id = UUID.randomUUID().toString();
        this.ticket = Objects.requireNonNull(ticket, "ticket must not be null");
        this.amount = amount;
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.issuedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public ParkingTicket getTicket() {
        return ticket;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public PaymentStatus getStatus() {
        return status;
    }
}

