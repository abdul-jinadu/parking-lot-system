package com.parkinglot.services;

import java.util.Objects;
import java.util.Optional;

import com.parkinglot.exceptions.ParkingFullException;
import com.parkinglot.exceptions.PaymentFailedException;
import com.parkinglot.exceptions.VehicleNotFoundException;
import com.parkinglot.models.ParkingLot;
import com.parkinglot.models.ParkingTicket;
import com.parkinglot.models.Receipt;
import com.parkinglot.models.vehicles.Vehicle;
import com.parkinglot.payment.Payment;
import com.parkinglot.payment.PaymentProcessor;
import com.parkinglot.payment.PaymentStatus;

// Orchestrates parking operations: entry, exit, payment
// Dependency: depends on FeeCalculationService and PaymentProcessor
public class ParkingService {

    private final ParkingLot parkingLot;
    private final FeeCalculationService feeService;
    private final PaymentProcessor paymentProcessor;

    public ParkingService(ParkingLot parkingLot, FeeCalculationService feeService) {
        this.parkingLot = Objects.requireNonNull(parkingLot);
        this.feeService = Objects.requireNonNull(feeService);
        this.paymentProcessor = PaymentProcessor.getInstance();
    }

    // Parks a vehicle and returns its ticket
    public ParkingTicket entry(Vehicle vehicle) {
        ParkingTicket ticket = parkingLot.parkVehicle(vehicle);
        if (ticket == null) {
            throw new ParkingFullException("No available spot for " + vehicle.getLicensePlate());
        }
        return ticket;
    }

    // Processes vehicle exit with payment
    public Receipt exit(String licensePlate, Payment payment) {
        Optional<ParkingTicket> found = parkingLot.findVehicle(licensePlate);
        if (found.isEmpty()) {
            throw new VehicleNotFoundException(licensePlate);
        }

        ParkingTicket ticket = found.get();

        // Calculate fee before closing ticket
        ParkingTicket exitTicket = parkingLot.exitVehicle(licensePlate);
        if (exitTicket == null) {
            throw new VehicleNotFoundException(licensePlate);
        }

        double fee = feeService.calculateFee(exitTicket);

        // Process payment
        boolean paid = paymentProcessor.processPayment(payment);
        if (!paid) {
            throw new PaymentFailedException("Payment failed for " + licensePlate);
        }

        return new Receipt(exitTicket, fee, PaymentStatus.SUCCESS);
    }

    // Calculates the current fee without exiting
    public double checkFee(String licensePlate) {
        Optional<ParkingTicket> found = parkingLot.findVehicle(licensePlate);
        if (found.isEmpty()) {
            throw new VehicleNotFoundException(licensePlate);
        }
        return feeService.calculateFee(found.get());
    }

    // Searches for a vehicle by license plate
    public Optional<ParkingTicket> search(String licensePlate) {
        return parkingLot.findVehicle(licensePlate);
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }
}
