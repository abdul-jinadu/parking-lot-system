package com.parkinglot.interfaces;

import com.parkinglot.models.Receipt;
import com.parkinglot.payment.Payment;
import com.parkinglot.payment.PaymentStatus;

/**
 * Defines operations for components that can process payments.
 */
public interface Payable {

    /**
     * Processes the given payment.
     *
     * @param payment the payment to process
     * @return {@code true} if the payment was processed successfully, {@code false} otherwise
     */
    boolean processPayment(Payment payment);

    /**
     * Generates a receipt for the last processed payment.
     *
     * @return generated receipt
     */
    Receipt generateReceipt();

    /**
     * @return current status of the payment processing component.
     */
    PaymentStatus getPaymentStatus();
}

