// =====================================================
// PARKING LOT SYSTEM - FIXED VERSION
// =====================================================

import java.util.*;
import java.time.LocalDateTime;

// Main Class - Must be first and public
public class ParkingLotSystem {
    public static void main(String[] args) {
        System.out.println("=== PARKING LOT SYSTEM DEMO ===");
        System.out.println("===============================\n");

        // Create parking lot
        ParkingLot parkingLot = new ParkingLot("City Mall Parking", "Downtown");

        // Park vehicles
        Vehicle car1 = new Car("ABC123");
        Vehicle bike1 = new Bike("XYZ789");
        Vehicle truck1 = new Truck("TRK456");

        System.out.println("1. Parking vehicles...");
        ParkingTicket ticket1 = parkingLot.parkVehicle(car1);
        ParkingTicket ticket2 = parkingLot.parkVehicle(bike1);
        ParkingTicket ticket3 = parkingLot.parkVehicle(truck1);

        System.out.println("\n2. Current availability:");
        parkingLot.displayAvailability();

        // Simulate some time passing
        try {
            Thread.sleep(2000); // Wait 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Unpark vehicle
        System.out.println("\n3. Unparking car...");
        if (ticket1 != null) {
            parkingLot.unparkVehicle(ticket1.getTicketNumber());
        }

        System.out.println("\n4. Final availability:");
        parkingLot.displayAvailability();

        System.out.println("\n=== PARKING LOT DEMO COMPLETED ===");
    }
}

// Enums for Parking Lot
enum VehicleType {
    CAR, BIKE, TRUCK
}

enum ParkingSpotType {
    COMPACT, LARGE, HANDICAPPED
}

enum ParkingTicketStatus {
    ACTIVE, PAID, LOST
}

// Vehicle Classes
abstract class Vehicle {
    protected String licensePlate;
    protected VehicleType type;
    
    public Vehicle(String licensePlate, VehicleType type) {
        this.licensePlate = licensePlate;
        this.type = type;
    }
    
    public VehicleType getType() { return type; }
    public String getLicensePlate() { return licensePlate; }
}

class Car extends Vehicle {
    public Car(String licensePlate) {
        super(licensePlate, VehicleType.CAR);
    }
}

class Bike extends Vehicle {
    public Bike(String licensePlate) {
        super(licensePlate, VehicleType.BIKE);
    }
}

class Truck extends Vehicle {
    public Truck(String licensePlate) {
        super(licensePlate, VehicleType.TRUCK);
    }
}

// Parking Spot
class ParkingSpot {
    private int spotNumber;
    private ParkingSpotType type;
    private boolean isFree;
    private Vehicle vehicle;
    
    public ParkingSpot(int spotNumber, ParkingSpotType type) {
        this.spotNumber = spotNumber;
        this.type = type;
        this.isFree = true;
    }
    
    public boolean canFitVehicle(Vehicle vehicle) {
        if (!isFree) return false;
        
        switch (vehicle.getType()) {
            case BIKE:
                return true; // Bike can fit anywhere
            case CAR:
                return type == ParkingSpotType.COMPACT || type == ParkingSpotType.HANDICAPPED;
            case TRUCK:
                return type == ParkingSpotType.LARGE;
            default:
                return false;
        }
    }
    
    public void assignVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.isFree = false;
    }
    
    public void freeSpot() {
        this.vehicle = null;
        this.isFree = true;
    }
    
    // Getters
    public int getSpotNumber() { return spotNumber; }
    public ParkingSpotType getType() { return type; }
    public boolean isFree() { return isFree; }
    public Vehicle getVehicle() { return vehicle; }
}

// Parking Ticket
class ParkingTicket {
    private String ticketNumber;
    private LocalDateTime issuedAt;
    private LocalDateTime paidAt;
    private double amount;
    private ParkingTicketStatus status;
    private Vehicle vehicle;
    private ParkingSpot parkingSpot;
    
    public ParkingTicket(Vehicle vehicle, ParkingSpot spot) {
        this.ticketNumber = UUID.randomUUID().toString().substring(0, 8);
        this.issuedAt = LocalDateTime.now();
        this.status = ParkingTicketStatus.ACTIVE;
        this.vehicle = vehicle;
        this.parkingSpot = spot;
    }
    
    // Getters and setters
    public String getTicketNumber() { return ticketNumber; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public ParkingSpot getParkingSpot() { return parkingSpot; }
    public Vehicle getVehicle() { return vehicle; }
    public ParkingTicketStatus getStatus() { return status; }
    public void setStatus(ParkingTicketStatus status) { this.status = status; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}

// Parking Rate Calculator
class ParkingRate {
    private Map<VehicleType, Double> hourlyRates;
    
    public ParkingRate() {
        hourlyRates = new HashMap<>();
        hourlyRates.put(VehicleType.BIKE, 10.0);
        hourlyRates.put(VehicleType.CAR, 20.0);
        hourlyRates.put(VehicleType.TRUCK, 50.0);
    }
    
    public double calculateRate(Vehicle vehicle, LocalDateTime startTime, LocalDateTime endTime) {
        long hours = java.time.Duration.between(startTime, endTime).toHours();
        if (hours == 0) hours = 1; // Minimum 1 hour charge
        return hourlyRates.get(vehicle.getType()) * hours;
    }
}

// Main Parking Lot System
class ParkingLot {
    private String name;
    private String address;
    private List<ParkingSpot> parkingSpots;
    private Map<String, ParkingTicket> activeTickets;
    private ParkingRate parkingRate;
    
    public ParkingLot(String name, String address) {
        this.name = name;
        this.address = address;
        this.parkingSpots = new ArrayList<>();
        this.activeTickets = new HashMap<>();
        this.parkingRate = new ParkingRate();
        
        // Initialize parking spots
        initializeParkingSpots();
    }
    
    private void initializeParkingSpots() {
        // Add different types of parking spots
        for (int i = 1; i <= 100; i++) {
            if (i <= 50) {
                parkingSpots.add(new ParkingSpot(i, ParkingSpotType.COMPACT));
            } else if (i <= 80) {
                parkingSpots.add(new ParkingSpot(i, ParkingSpotType.LARGE));
            } else {
                parkingSpots.add(new ParkingSpot(i, ParkingSpotType.HANDICAPPED));
            }
        }
    }
    
    public ParkingTicket parkVehicle(Vehicle vehicle) {
        ParkingSpot spot = findAvailableSpot(vehicle);
        if (spot == null) {
            System.out.println("No available spot for vehicle: " + vehicle.getLicensePlate());
            return null;
        }
        
        spot.assignVehicle(vehicle);
        ParkingTicket ticket = new ParkingTicket(vehicle, spot);
        activeTickets.put(ticket.getTicketNumber(), ticket);
        
        System.out.println("Vehicle parked successfully. Ticket: " + ticket.getTicketNumber());
        return ticket;
    }
    
    private ParkingSpot findAvailableSpot(Vehicle vehicle) {
        for (ParkingSpot spot : parkingSpots) {
            if (spot.canFitVehicle(vehicle)) {
                return spot;
            }
        }
        return null;
    }
    
    public double unparkVehicle(String ticketNumber) {
        ParkingTicket ticket = activeTickets.get(ticketNumber);
        if (ticket == null || ticket.getStatus() != ParkingTicketStatus.ACTIVE) {
            System.out.println("Invalid ticket number or ticket already processed");
            return 0;
        }
        
        LocalDateTime endTime = LocalDateTime.now();
        double amount = parkingRate.calculateRate(ticket.getVehicle(), ticket.getIssuedAt(), endTime);
        
        ticket.setAmount(amount);
        ticket.setStatus(ParkingTicketStatus.PAID);
        ticket.getParkingSpot().freeSpot();
        activeTickets.remove(ticketNumber);
        
        System.out.println("Vehicle unparked. Amount: $" + amount);
        return amount;
    }
    
    public void displayAvailability() {
        Map<ParkingSpotType, Integer> availability = new HashMap<>();
        for (ParkingSpot spot : parkingSpots) {
            if (spot.isFree()) {
                availability.put(spot.getType(), availability.getOrDefault(spot.getType(), 0) + 1);
            }
        }
        
        System.out.println("Available spots:");
        for (Map.Entry<ParkingSpotType, Integer> entry : availability.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}