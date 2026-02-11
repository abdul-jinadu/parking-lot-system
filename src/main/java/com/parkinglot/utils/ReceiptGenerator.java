package com.parkinglot.utils;

import com.parkinglot.models.ParkingTicket;
import com.parkinglot.models.Receipt;

// Dependency: depends on ParkingTicket and Receipt to generate formatted output
public final class ReceiptGenerator {

    private ReceiptGenerator() {
        // utility class
    }

    // Generates a formatted receipt string
    public static String generate(Receipt receipt) {
        ParkingTicket ticket = receipt.getTicket();
        StringBuilder sb = new StringBuilder();

        sb.append("\n=========================================\n");
        sb.append("           PARKING RECEIPT\n");
        sb.append("=========================================\n");
        sb.append(String.format("Receipt ID:    %s%n", receipt.getId()));

        if (ticket != null) {
            sb.append(String.format("Ticket ID:     %s%n", ticket.getId()));
            sb.append(String.format("License Plate: %s%n", ticket.getVehicle().getLicensePlate()));
            sb.append(String.format("Vehicle Type:  %s%n", ticket.getVehicle().getType()));
            sb.append(String.format("Spot:          %s%n", ticket.getSpot().getId()));
            sb.append(String.format("Entry Time:    %s%n", ticket.getEntryTime()));
            sb.append(String.format("Exit Time:     %s%n", ticket.getExitTime()));
            sb.append(String.format("Duration:      %d minutes%n", ticket.getDurationInMinutes()));
        }

        sb.append(String.format("Amount:        R%.2f%n", receipt.getAmount()));
        sb.append(String.format("Status:        %s%n", receipt.getStatus()));
        sb.append(String.format("Issued At:     %s%n", receipt.getIssuedAt()));
        sb.append("=========================================\n");

        return sb.toString();
    }

    // Generates a short ticket confirmation
    public static String generateTicketConfirmation(ParkingTicket ticket) {
        return String.format(
                "[TICKET] %s | Vehicle: %s (%s) | Spot: %s | Entry: %s",
                ticket.getId(),
                ticket.getVehicle().getLicensePlate(),
                ticket.getVehicle().getType(),
                ticket.getSpot().getId(),
                ticket.getEntryTime()
        );
    }
}
