package com.parkinglot.payment;

import java.util.Optional;

import com.parkinglot.interfaces.Payable;
import com.parkinglot.models.Receipt;

public class PaymentProcessor implements Payable {

    // simple singleton
    private static final PaymentProcessor INSTANCE = new PaymentProcessor();

    private Payment lastPayment;
    private PaymentStatus lastStatus = PaymentStatus.PENDING;

    private PaymentProcessor() {
    }

    public static PaymentProcessor getInstance() {
        return INSTANCE;
    }

    @Override
    public synchronized boolean processPayment(Payment payment) {
        if (payment == null) {
            return false;
        }
        this.lastPayment = payment;
        payment.authorize();
        payment.capture();
        this.lastStatus = payment.getStatus();
        return lastStatus == PaymentStatus.SUCCESS;
    }

    @Override
    public synchronized Receipt generateReceipt() {
        Optional<Payment> maybePayment = Optional.ofNullable(lastPayment);
        if (maybePayment.isEmpty()) {
            throw new IllegalStateException("no payment to generate receipt for");
        }
        // ticket is attached later by services
        return new Receipt(null, lastPayment.getAmount(), lastStatus);
    }

    @Override
    public synchronized PaymentStatus getPaymentStatus() {
        return lastStatus;
    }
}

