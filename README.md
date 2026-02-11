## Parking Lot Management System

A comprehensive parking lot management system implemented in Java and designed to showcase robust object-oriented architecture, rich domain modeling, and production-ready design patterns.

### Features
- **Multi-level parking management**: Manage multiple floors with heterogeneous spot types (compact, regular, large, electric, disabled).
- **Advanced vehicle support**: Handle cars, motorcycles, trucks, buses, and electric vehicles with type-specific behavior.
- **Intelligent spot assignment**: Assign spots based on vehicle type, size, and reservation rules, including reserved, VIP, disabled, and electric charging spots.
- **Dynamic pricing and fee calculation**: Apply different pricing strategies (hourly, daily, flat rate) with vehicle-specific and spot-specific adjustments.
- **Reservation system**: Reserve, cancel, and validate reservations with prevention of double-booking.
- **Payment processing**: Process payments via cash, card, or digital wallet, with receipt generation and status tracking.
- **Real-time occupancy tracking**: Track available and occupied spots per floor and per spot type, including lot-wide statistics.
- **Search and reporting**: Search vehicles by license plate and generate operational and revenue reports.
- **Console-based operations**: Interactive console interface for parking, exiting, searching, reservations, and reporting.

### Architecture

The system is built around a rich domain model with clear separation of concerns between models, services, payment, strategies, utilities, and exceptions.

#### OOP Principles Demonstrated
- **Inheritance**: 
  - `Vehicle` as an abstract base for `Car`, `Motorcycle`, `Truck`, `Bus`, and `ElectricVehicle`.
  - `ParkingSpot` as an abstract base for `CompactSpot`, `RegularSpot`, `LargeSpot`, `ElectricSpot`, and `DisabledSpot`.
  - `Payment` as an abstract base for `CashPayment`, `CardPayment`, and `DigitalWalletPayment`.
- **Polymorphism**:
  - Fee calculation overridden per vehicle type and delegated via `PricingStrategy` implementations.
  - `Parkable`, `Payable`, and `Reservable` interfaces referenced by services to operate on heterogeneous implementations.
- **Encapsulation**:
  - All domain fields are private with validated accessors and controlled mutations through services.
  - Complex operations such as spot assignment and fee calculation are hidden behind dedicated service APIs.
- **Abstraction**:
  - Abstract base classes (`Vehicle`, `ParkingSpot`, `Payment`) capture shared properties and behavior.
  - Interfaces (`Parkable`, `Chargeable`, `Payable`, `Reservable`, `Searchable`) define capabilities separate from concrete implementations.

#### Relationships
- **Composition**:
  - `ParkingLot` composes `ParkingFloor` instances; floors are created and destroyed with the lot.
  - `ParkingFloor` composes `ParkingSpot` instances; spots are managed exclusively by their floor.
  - `ParkingTicket` composes the association between a vehicle and a specific spot and timestamps.
- **Aggregation**:
  - `ParkingLot` aggregates active `Vehicle` instances that exist independently of the lot lifecycle.
  - `Customer` aggregates owned `Vehicle` instances.
  - `PaymentProcessor` aggregates `Payment` instances processed over time.
- **Association**:
  - `Vehicle` is associated with `ParkingSpot` via `ParkingTicket`.
  - `Customer` owns one or more `Vehicle` instances.
  - `ParkingAttendant` manages operations on a `ParkingLot` through the service layer.

#### Design Patterns
- **Strategy Pattern**:
  - `PricingStrategy` with `HourlyPricingStrategy`, `DailyPricingStrategy`, and `FlatRatePricingStrategy`.
  - Spot assignment encapsulated in `SpotAssignmentService` to allow alternative strategies (e.g., first-available, closest-to-entrance).
- **Factory Pattern**:
  - `VehicleFactory` and `ParkingSpotFactory` (under `services` or `utils`) to centralize creation of domain objects.
- **Singleton Pattern**:
  - `ParkingLot` and `PaymentProcessor` implemented as controlled singletons to model a single managed lot and global payment gateway instance.
- **Observer Pattern (optional extension)**:
  - Hook points for listeners when occupancy thresholds are crossed or when spots become free.
- **Builder Pattern (optional extension)**:
  - A builder for complex `ParkingLot` configurations (floors, spot distributions, and pricing defaults).

### Project Structure

The codebase follows a standard Maven layout:

- **`src/main/java/com/parkinglot`**: Core application code.
  - `models/vehicles`: Vehicle hierarchy and vehicle-related enums.
  - `models/spots`: Parking spot hierarchy and spot type enums.
  - `models`: `ParkingFloor`, `ParkingLot`, `ParkingTicket`, `Customer`, `ParkingAttendant`, `Receipt`.
  - `interfaces`: Behavioral interfaces (`Parkable`, `Chargeable`, `Payable`, `Reservable`, `Searchable`).
  - `payment`: Payment hierarchy, processor, and status.
  - `services`: Parking, fee calculation, spot assignment, reporting, and reservation services.
  - `strategies`: Pricing strategies.
  - `utils`: Display, receipt generator, and input validation helpers.
  - `exceptions`: Custom domain exceptions.
  - `Main`: Console entry point.
- **`src/test/java/com/parkinglot`**: JUnit 5 and Mockito-based test suite organized in parallel to main packages.

### Installation

1. Ensure a Java 17+ runtime and Maven 3.8+ are installed and available on your system PATH.
2. Clone the repository and navigate into the project directory.
3. Build the project and run tests:

```bash
mvn clean verify
```

This will compile the application and execute the full JUnit test suite.

### Usage

To run the console-based parking lot management application:

```bash
mvn clean package
java -jar target/parking-lot-system-1.0.0.jar
```

The console will display the main menu:

```text
╔════════════════════════════════════════╗
║     PARKING LOT MANAGEMENT SYSTEM      ║
╚════════════════════════════════════════╝

Current Status:
-----------------------------------------
Total Capacity: 120 spots
Occupied: 87 spots (72.5%)
Available: 33 spots (27.5%)

Available Spots by Type:
  Compact:   5 spots
  Regular:   18 spots
  Large:     7 spots
  Electric:  2 spots
  Disabled:  1 spot

Menu:
1. Park Vehicle
2. Exit Vehicle
3. Search Vehicle
4. View Parking Status
5. Make Reservation
6. Generate Reports
7. Exit System
```

Follow the on-screen prompts to park vehicles, process exits and payments, manage reservations, and view reports.

### Testing

Run the automated test suite with:

```bash
mvn test
```

The test suite includes:
- **Vehicle and parking spot tests**: Validate inheritance, polymorphism, and interface contracts.
- **Service tests**: Exercise parking operations, spot assignment rules, fee calculation, and reservations.
- **Payment tests**: Verify payment processing, failure handling, and multiple payment methods.
- **Relationship tests**: Confirm composition, aggregation, and association semantics where applicable.

### API Overview

Key types and entry points:
- **`ParkingService`**: High-level operations for parking, exiting, and searching vehicles.
- **`FeeCalculationService`**: Calculates parking fees based on duration, vehicle type, spot type, and pricing strategy.
- **`SpotAssignmentService`**: Determines the best available spot for a given vehicle and reservation context.
- **`ReservationService`**: Manages spot reservations, cancellations, and conflict prevention.
- **`PaymentProcessor`**: Coordinates payment flows and status tracking.
- **`ParkingLotDisplay`**: Renders human-readable occupancy and capacity information.

For detailed class and method documentation, refer to the JavaDoc comments throughout the codebase.

