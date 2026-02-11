package com.parkinglot.payment;

public class CashPayment extends Payment {

    public CashPayment(double amount) {
        super(amount);
    }

    @Override
    public void authorize() {
        markSuccess(); // cash is authorized on receipt
    }

    @Override
    public void capture() {
        // no capture step for cash
    }
}

