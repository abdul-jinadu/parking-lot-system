package com.parkinglot.interfaces;

import com.parkinglot.models.Receipt;
import com.parkinglot.payment.Payment;
import com.parkinglot.payment.PaymentStatus;

// Defines operations for components that process payments
public interface Payable {

    boolean processPayment(Payment payment);

    Receipt generateReceipt();

    PaymentStatus getPaymentStatus();
}
