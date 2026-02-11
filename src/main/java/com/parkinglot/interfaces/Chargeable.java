package com.parkinglot.interfaces;

/**
 * Defines behavior for electric vehicles or devices that can be charged.
 */
public interface Chargeable {

    /**
     * Starts the charging process.
     */
    void startCharging();

    /**
     * Stops the charging process.
     */
    void stopCharging();

    /**
     * @return charging rate in kW or equivalent unit.
     */
    double getChargingRate();

    /**
     * @return {@code true} if the entity is fully charged, {@code false} otherwise.
     */
    boolean isFullyCharged();
}

