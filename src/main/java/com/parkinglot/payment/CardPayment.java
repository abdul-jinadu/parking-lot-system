package com.parkinglot.payment;

public class CardPayment extends Payment {

    private final String cardNumberMasked;

    public CardPayment(double amount, String cardNumberMasked) {
        super(amount);
        this.cardNumberMasked = cardNumberMasked;
    }

    public String getCardNumberMasked() {
        return cardNumberMasked;
    }

    @Override
    public void authorize() {
        // simulate card authorization
        markSuccess();
    }

    @Override
    public void capture() {
        // simulate capture
    }
}

