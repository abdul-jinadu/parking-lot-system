package com.parkinglot;

import java.util.Optional;
import java.util.Scanner;

import com.parkinglot.exceptions.ParkingFullException;
import com.parkinglot.exceptions.PaymentFailedException;
import com.parkinglot.exceptions.VehicleNotFoundException;
import com.parkinglot.models.Customer;
import com.parkinglot.models.ParkingLot;
import com.parkinglot.models.ParkingTicket;
import com.parkinglot.models.Receipt;
import com.parkinglot.models.vehicles.Bus;
import com.parkinglot.models.vehicles.Car;
import com.parkinglot.models.vehicles.ElectricVehicle;
import com.parkinglot.models.vehicles.Motorcycle;
import com.parkinglot.models.vehicles.Truck;
import com.parkinglot.models.vehicles.Vehicle;
import com.parkinglot.payment.CardPayment;
import com.parkinglot.payment.CashPayment;
import com.parkinglot.payment.DigitalWalletPayment;
import com.parkinglot.payment.Payment;
import com.parkinglot.services.FeeCalculationService;
import com.parkinglot.services.FindCar;
import com.parkinglot.services.ParkingService;
import com.parkinglot.services.ReportService;
import com.parkinglot.services.ReservationService;
import com.parkinglot.strategies.HourlyPricingStrategy;
import com.parkinglot.utils.InputValidator;
import com.parkinglot.utils.ParkingLotDisplay;
import com.parkinglot.utils.ReceiptGenerator;

// Console entry point for the Parking Lot Management System
public class Main {

    private static ParkingLot parkingLot;
    private static ParkingService parkingService;
    private static ReportService reportService;
    private static ReservationService reservationService;
    private static Scanner scanner;

    public static void main(String[] args) {
        initialize();
        scanner = new Scanner(System.in);

        boolean running = true;
        while (running) {
            System.out.print(ParkingLotDisplay.getMenu());
            int choice = InputValidator.parseIntSafe(scanner.nextLine());

            switch (choice) {
                case 1 -> parkVehicle();
                case 2 -> exitVehicle();
                case 3 -> searchVehicle();
                case 4 -> viewStatus();
                case 5 -> makeReservation();
                case 6 -> cancelReservation();
                case 7 -> generateReport();
                case 8 -> findMyCar();
                case 9 -> {
                    System.out.println("\nExiting system. Goodbye!");
                    running = false;
                }
                default -> System.out.println("\nInvalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    // Sets up the parking lot with 3 floors
    private static void initialize() {
        ParkingLot.resetInstance();
        parkingLot = ParkingLot.getInstance("City Centre Parking");

        // Floor 1: 5 compact, 15 regular, 5 large, 3 electric, 2 disabled = 30 spots
        parkingLot.addFloor(5, 15, 5, 3, 2);
        // Floor 2: 5 compact, 20 regular, 8 large, 4 electric, 3 disabled = 40 spots
        parkingLot.addFloor(5, 20, 8, 4, 3);
        // Floor 3: 3 compact, 20 regular, 10 large, 5 electric, 2 disabled = 40 spots
        parkingLot.addFloor(3, 20, 10, 5, 2);

        FeeCalculationService feeService = new FeeCalculationService(new HourlyPricingStrategy());
        parkingService = new ParkingService(parkingLot, feeService);
        reportService = new ReportService(parkingLot);
        reservationService = new ReservationService(parkingLot);

        System.out.println("\nParking lot initialized: " + parkingLot.getName());
        System.out.println("Total capacity: " + parkingLot.getTotalCapacity() + " spots across "
                + parkingLot.getFloors().size() + " floors");
    }

    private static void parkVehicle() {
        System.out.print(ParkingLotDisplay.getVehicleTypeMenu());
        int typeChoice = InputValidator.parseIntSafe(scanner.nextLine());

        System.out.print("Enter license plate: ");
        String plate = scanner.nextLine().trim();
        if (!InputValidator.isValidLicensePlate(plate)) {
            System.out.println("Invalid license plate");
            return;
        }

        System.out.print("Enter vehicle color: ");
        String color = scanner.nextLine().trim();

        Vehicle vehicle = createVehicle(typeChoice, plate, color);
        if (vehicle == null) {
            System.out.println("Invalid vehicle type");
            return;
        }

        try {
            ParkingTicket ticket = parkingService.entry(vehicle);
            System.out.println("\nVehicle parked successfully!");
            System.out.println(ReceiptGenerator.generateTicketConfirmation(ticket));
        } catch (ParkingFullException e) {
            System.out.println("\n" + e.getMessage());
        }
    }

    private static void exitVehicle() {
        System.out.print("\nEnter license plate: ");
        String plate = scanner.nextLine().trim();
        if (!InputValidator.isValidLicensePlate(plate)) {
            System.out.println("Invalid license plate");
            return;
        }

        try {
            // Show current fee
            double fee = parkingService.checkFee(plate);
            System.out.printf("Parking fee: R%.2f%n", fee);

            // Select payment method
            System.out.print(ParkingLotDisplay.getPaymentMenu());
            int payChoice = InputValidator.parseIntSafe(scanner.nextLine());
            Payment payment = createPayment(payChoice, fee);
            if (payment == null) {
                System.out.println("Invalid payment method");
                return;
            }

            Receipt receipt = parkingService.exit(plate, payment);
            System.out.println(ReceiptGenerator.generate(receipt));

        } catch (VehicleNotFoundException e) {
            System.out.println("\n" + e.getMessage());
        } catch (PaymentFailedException e) {
            System.out.println("\nPayment failed: " + e.getMessage());
        }
    }

    private static void searchVehicle() {
        System.out.print("\nEnter license plate: ");
        String plate = scanner.nextLine().trim();

        Optional<ParkingTicket> found = parkingService.search(plate);
        if (found.isPresent()) {
            ParkingTicket ticket = found.get();
            System.out.println("\nVehicle found!");
            System.out.println(ReceiptGenerator.generateTicketConfirmation(ticket));
            System.out.printf("Current duration: %d minutes%n", ticket.getDurationInMinutes());
        } else {
            System.out.println("\nVehicle not found: " + plate);
        }
    }

    private static void viewStatus() {
        System.out.println(reportService.generateStatusReport());
        System.out.println(ParkingLotDisplay.getFullMap(parkingLot));
    }

    private static void makeReservation() {
        System.out.print("\nEnter customer name: ");
        String name = scanner.nextLine().trim();
        if (!InputValidator.isNonBlank(name)) {
            System.out.println("Invalid name");
            return;
        }

        Customer customer = new Customer(name);

        System.out.print(ParkingLotDisplay.getVehicleTypeMenu());
        int typeChoice = InputValidator.parseIntSafe(scanner.nextLine());

        System.out.print("Enter license plate: ");
        String plate = scanner.nextLine().trim();

        Vehicle vehicle = createVehicle(typeChoice, plate, "N/A");
        if (vehicle == null) {
            System.out.println("Invalid vehicle type");
            return;
        }

        customer.addVehicle(vehicle);

        Optional<String> reservationId = reservationService.reserveSpot(
                customer, vehicle, java.time.LocalDateTime.now(), 2);

        if (reservationId.isPresent()) {
            System.out.println("\nReservation confirmed! ID: " + reservationId.get());
        } else {
            System.out.println("\nNo spots available for reservation");
        }
    }

    private static void cancelReservation() {
        System.out.print("\nEnter reservation ID: ");
        String id = scanner.nextLine().trim();

        if (reservationService.cancelReservation(id)) {
            System.out.println("Reservation cancelled successfully");
        } else {
            System.out.println("Reservation not found: " + id);
        }
    }

    private static void generateReport() {
        System.out.println(reportService.generateStatusReport());
    }
     // Created with assistance from Chatgpt
    private static void findMyCar() {

    System.out.print("\nEnter your car color: ");
    String color = scanner.nextLine().trim();

    System.out.print("Enter your vehicle type (CAR, MOTORCYCLE, TRUCK, BUS, ELECTRIC_VEHICLE): ");
    String vehicleType = scanner.nextLine().trim();

    FindCar finder = new FindCar(parkingService);
    finder.findCar(color, vehicleType);
   }
   
    // Factory method - creates vehicle based on type selection
    private static Vehicle createVehicle(int typeChoice, String plate, String color) {
        return switch (typeChoice) {
            case 1 -> new Car(plate, color, 4);
            case 2 -> new Motorcycle(plate, color, 150);
            case 3 -> new Truck(plate, color, 5.0);
            case 4 -> new Bus(plate, color, 50);
            case 5 -> new ElectricVehicle(plate, color, 4, 75.0);
            default -> null;
        };
    }

    // Factory method - creates payment based on method selection
    private static Payment createPayment(int payChoice, double amount) {
        return switch (payChoice) {
            case 1 -> new CashPayment(amount);
            case 2 -> {
                System.out.print("Enter card number (last 4 digits): ");
                String card = scanner.nextLine().trim();
                yield new CardPayment(amount, "****-****-****-" + card);
            }
            case 3 -> {
                System.out.print("Enter wallet ID: ");
                String wallet = scanner.nextLine().trim();
                yield new DigitalWalletPayment(amount, wallet);
            }
            default -> null;
        };
    }

}
