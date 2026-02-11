package com.parkinglot.payment;

public class DigitalWalletPayment extends Payment {

    private final String walletId;

    public DigitalWalletPayment(double amount, String walletId) {
        super(amount);
        this.walletId = walletId;
    }

    public String getWalletId() {
        return walletId;
    }

    @Override
    public void authorize() {
        // simulate wallet authorization
        markSuccess();
    }

    @Override
    public void capture() {
        // simulate capture
    }
}

