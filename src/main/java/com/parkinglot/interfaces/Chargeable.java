package com.parkinglot.interfaces;

// Defines behaviour for electric vehicles that can be charged
public interface Chargeable {

    void startCharging();

    void stopCharging();

    // Returns charging rate in kW
    double getChargingRate();

    boolean isFullyCharged();
}
