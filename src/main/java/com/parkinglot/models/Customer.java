package com.parkinglot.models;

import com.parkinglot.models.vehicles.Vehicle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Customer {

    private final String id;
    private String name;
    private final List<Vehicle> ownedVehicles = new ArrayList<>();

    public Customer(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        this.name = name;
    }

    public List<Vehicle> getOwnedVehicles() {
        return Collections.unmodifiableList(ownedVehicles);
    }

    public void addVehicle(Vehicle vehicle) {
        ownedVehicles.add(Objects.requireNonNull(vehicle, "vehicle must not be null"));
    }

    public void removeVehicle(Vehicle vehicle) {
        ownedVehicles.remove(vehicle);
    }
}

