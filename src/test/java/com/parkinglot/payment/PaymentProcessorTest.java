package com.parkinglot.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.parkinglot.models.Receipt;

class PaymentProcessorTest {

    @Test
    void singletonReturnsSameInstance() {
        PaymentProcessor p1 = PaymentProcessor.getInstance();
        PaymentProcessor p2 = PaymentProcessor.getInstance();
        assertEquals(p1, p2);
    }

    @Test
    void cashPaymentSucceeds() {
        PaymentProcessor processor = PaymentProcessor.getInstance();
        CashPayment cash = new CashPayment(100.0);

        boolean result = processor.processPayment(cash);
        assertTrue(result);
        assertEquals(PaymentStatus.SUCCESS, processor.getPaymentStatus());
    }

    @Test
    void cardPaymentSucceeds() {
        PaymentProcessor processor = PaymentProcessor.getInstance();
        CardPayment card = new CardPayment(200.0, "****-****-****-1234");

        boolean result = processor.processPayment(card);
        assertTrue(result);
        assertEquals(PaymentStatus.SUCCESS, processor.getPaymentStatus());
    }

    @Test
    void digitalWalletPaymentSucceeds() {
        PaymentProcessor processor = PaymentProcessor.getInstance();
        DigitalWalletPayment wallet = new DigitalWalletPayment(150.0, "wallet-abc");

        boolean result = processor.processPayment(wallet);
        assertTrue(result);
    }

    @Test
    void nullPaymentReturnsFalse() {
        PaymentProcessor processor = PaymentProcessor.getInstance();
        boolean result = processor.processPayment(null);
        assertFalse(result);
    }

    @Test
    void generateReceiptAfterPayment() {
        PaymentProcessor processor = PaymentProcessor.getInstance();
        processor.processPayment(new CashPayment(75.0));

        Receipt receipt = processor.generateReceipt();
        assertNotNull(receipt);
        assertEquals(75.0, receipt.getAmount());
    }

    @Test
    void paymentHierarchyPolymorphism() {
        // demonstrates runtime polymorphism through Payment references
        Payment cash = new CashPayment(10.0);
        Payment card = new CardPayment(20.0, "****-1234");
        Payment wallet = new DigitalWalletPayment(30.0, "w-1");

        PaymentProcessor processor = PaymentProcessor.getInstance();

        // all processed through the same interface
        assertTrue(processor.processPayment(cash));
        assertTrue(processor.processPayment(card));
        assertTrue(processor.processPayment(wallet));
    }
}
