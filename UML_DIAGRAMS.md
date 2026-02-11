## UML Diagrams

This document provides UML views of the parking lot management system, focusing on class structure, inheritance hierarchies, interfaces, relationships, and core interaction flows.

### Class Diagram (High-Level Overview)

Key packages and classes:

- **com.parkinglot.models.vehicles**
  - `Vehicle` (abstract)
    - `Car`
    - `Motorcycle`
    - `Truck`
    - `Bus`
    - `ElectricVehicle`
  - `VehicleType` (enum)
- **com.parkinglot.models.spots**
  - `ParkingSpot` (abstract)
    - `CompactSpot`
    - `RegularSpot`
    - `LargeSpot`
    - `ElectricSpot`
    - `DisabledSpot`
  - `SpotType` (enum)
- **com.parkinglot.models**
  - `ParkingFloor`
  - `ParkingLot`
  - `ParkingTicket`
  - `Customer`
  - `ParkingAttendant`
  - `Receipt`
- **com.parkinglot.payment**
  - `Payment` (abstract)
    - `CashPayment`
    - `CardPayment`
    - `DigitalWalletPayment`
  - `PaymentProcessor`
  - `PaymentStatus` (enum)
- **com.parkinglot.interfaces**
  - `Parkable`
  - `Chargeable`
  - `Payable`
  - `Reservable`
  - `Searchable`
- **com.parkinglot.services**
  - `ParkingService`
  - `FeeCalculationService`
  - `SpotAssignmentService`
  - `ReportService`
  - `ReservationService`
- **com.parkinglot.strategies**
  - `PricingStrategy`
  - `HourlyPricingStrategy`
  - `DailyPricingStrategy`
  - `FlatRatePricingStrategy`
- **com.parkinglot.utils**
  - `ParkingLotDisplay`
  - `ReceiptGenerator`
  - `InputValidator`

### Inheritance Hierarchies

#### Vehicle Hierarchy

- `Vehicle` (abstract)
  - Fields: `licensePlate`, `color`, `VehicleType type`
  - Methods:
    - `double getSizeMultiplier()`
    - `boolean canFitInSpot(ParkingSpot spot)`
    - `boolean park(ParkingSpot spot)`
    - `boolean exit()`
    - `double calculateFee(long durationInMinutes)`
  - Implements: `Parkable`

Subclasses:
- `Car` extends `Vehicle`
- `Motorcycle` extends `Vehicle`
- `Truck` extends `Vehicle`
- `Bus` extends `Vehicle`
- `ElectricVehicle` extends `Car` implements `Chargeable`

#### ParkingSpot Hierarchy

- `ParkingSpot` (abstract)
  - Fields: `id`, `SpotType type`, `boolean occupied`, `boolean reserved`
  - Methods:
    - `boolean canFitVehicle(Vehicle vehicle)`
    - `void assignVehicle(Vehicle vehicle)`
    - `void removeVehicle()`

Subclasses:
- `CompactSpot` extends `ParkingSpot`
- `RegularSpot` extends `ParkingSpot`
- `LargeSpot` extends `ParkingSpot`
- `ElectricSpot` extends `ParkingSpot`
- `DisabledSpot` extends `ParkingSpot`

#### Payment Hierarchy

- `Payment` (abstract)
  - Fields: `amount`, `LocalDateTime timestamp`, `PaymentStatus status`
  - Methods: `void authorize()`, `void capture()`, `void fail(String reason)`

Subclasses:
- `CashPayment` extends `Payment`
- `CardPayment` extends `Payment`
- `DigitalWalletPayment` extends `Payment`

### Interface Relationships

- `Vehicle` implements `Parkable`.
- `ElectricVehicle` implements `Chargeable`.
- `ParkingService` implements `Searchable`.
- `ReservationService` implements `Reservable`.
- `PaymentProcessor` implements `Payable`.

### Composition vs Aggregation

#### Composition

- `ParkingLot` **composes** `ParkingFloor`:
  - `ParkingLot` owns a `List<ParkingFloor> floors`.
  - Floors are instantiated and destroyed with the lot.
- `ParkingFloor` **composes** `ParkingSpot`:
  - `ParkingFloor` owns a `List<ParkingSpot> spots`.
  - Spots are created as part of the floor configuration.
- `ParkingTicket` **composes** the association between `Vehicle` and `ParkingSpot`:
  - Holds `Vehicle vehicle`, `ParkingSpot spot`, and timestamps for entry and exit.

#### Aggregation

- `ParkingLot` **aggregates** `Vehicle`:
  - `List<Vehicle> parkedVehicles`.
  - Vehicles exist independently of the lot and can be reused elsewhere.
- `Customer` **aggregates** `Vehicle`:
  - `List<Vehicle> ownedVehicles`.
- `PaymentProcessor` **aggregates** `Payment`:
  - Maintains a record of processed payments.

### Sequence Diagram: Park Vehicle Flow (Textual)

1. `Main` requests `ParkingService.parkVehicle(vehicle, customer)`.
2. `ParkingService` calls `SpotAssignmentService.findSpotForVehicle(vehicle)`.
3. `SpotAssignmentService` queries `ParkingLot` and its `ParkingFloor` instances for an available compatible `ParkingSpot`.
4. `SpotAssignmentService` returns the chosen `ParkingSpot` to `ParkingService`.
5. `ParkingService` creates a `ParkingTicket` for the `Vehicle` and `ParkingSpot` with entry timestamp.
6. `ParkingSpot.assignVehicle(vehicle)` is invoked; `ParkingFloor` and `ParkingLot` update occupancy counters.
7. `ParkingService` returns the created `ParkingTicket` to `Main`.
8. `Main` uses `ParkingLotDisplay` to render the updated occupancy.

### Sequence Diagram: Exit Vehicle Flow (Textual)

1. `Main` requests `ParkingService.exitVehicle(licensePlate, paymentDetails)`.
2. `ParkingService` looks up the active `ParkingTicket` using `Searchable.findVehicle(licensePlate)`.
3. `ParkingService` computes the duration and delegates to `FeeCalculationService.calculateFee(ticket)` (using the configured `PricingStrategy`).
4. `ParkingService` creates a `Payment` instance and passes it to `PaymentProcessor.processPayment(payment)`.
5. `PaymentProcessor` updates `Payment.status` and returns control to `ParkingService`.
6. On success, `ParkingService` sets exit timestamp on `ParkingTicket`, calls `ParkingSpot.removeVehicle()`, and updates occupancy.
7. `ParkingService` passes the `ParkingTicket` to `ReceiptGenerator.generate(ticket, payment)` to create a `Receipt`.
8. `Main` renders the receipt and updated occupancy via `ParkingLotDisplay`.

### Use Case Diagram (Textual)

**Actors**:
- `Customer`
- `ParkingAttendant`
- `System Administrator` (configuration and reporting)

**Use Cases**:
- `Customer`:
  - Park vehicle.
  - Exit vehicle and pay.
  - Reserve parking spot.
  - Cancel reservation.
- `ParkingAttendant`:
  - Manually assign or override spots.
  - Search vehicle by license plate.
  - View lot and floor status.
  - Manage valet parking operations.
- `System Administrator`:
  - Configure parking lot (floors, spot distributions).
  - Configure pricing strategies and peak-hour rules.
  - View revenue and utilization reports.

