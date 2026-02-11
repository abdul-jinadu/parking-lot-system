package com.parkinglot.payment;

import java.time.LocalDateTime;

public abstract class Payment {

    private final double amount;
    private final LocalDateTime timestamp;
    private PaymentStatus status;
    private String failureReason;

    protected Payment(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount must be non negative");
        }
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.status = PaymentStatus.PENDING;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getFailureReason() {
        return failureReason;
    }

    protected void markSuccess() {
        this.status = PaymentStatus.SUCCESS;
    }

    protected void markFailed(String reason) {
        this.status = PaymentStatus.FAILED;
        this.failureReason = reason;
    }

    public abstract void authorize();

    public abstract void capture();
}

