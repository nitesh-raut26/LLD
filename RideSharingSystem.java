// =====================================================
// RIDE SHARING SYSTEM - FIXED VERSION
// =====================================================

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

// Main Class - Must be first and public
public class RideSharingSystem {
    public static void main(String[] args) {
        System.out.println("=== RIDE SHARING SYSTEM DEMO ===");
        System.out.println("================================\n");
        
        // Create ride sharing service
        RideService rideService = new RideService();
        
        // Register riders
        System.out.println("1. Registering riders...");
        Rider rider1 = new Rider("R001", "Alice Johnson", "555-1111", "alice@email.com");
        Rider rider2 = new Rider("R002", "Bob Smith", "555-2222", "bob@email.com");
        
        rideService.registerRider(rider1);
        rideService.registerRider(rider2);
        
        // Register drivers
        System.out.println("\n2. Registering drivers...");
        RideVehicle vehicle1 = new RideVehicle("DL01AB1234", VehicleCategory.ECONOMY, "Swift Dzire", "White");
        RideVehicle vehicle2 = new RideVehicle("DL02CD5678", VehicleCategory.PREMIUM, "Honda City", "Silver");
        RideVehicle vehicle3 = new RideVehicle("DL03EF9012", VehicleCategory.LUXURY, "BMW 5 Series", "Black");
        
        Driver driver1 = new Driver("D001", "Charlie Brown", "555-3333", "charlie@email.com", "DL123456", vehicle1);
        Driver driver2 = new Driver("D002", "Diana Prince", "555-4444", "diana@email.com", "DL789012", vehicle2);
        Driver driver3 = new Driver("D003", "Edward Wilson", "555-5555", "edward@email.com", "DL345678", vehicle3);
        
        // Set driver locations and status
        driver1.setCurrentLocation(new Location(28.6139, 77.2090, "Connaught Place, Delhi"));
        driver1.setStatus(DriverStatus.AVAILABLE);
        
        driver2.setCurrentLocation(new Location(28.6100, 77.2000, "Near India Gate, Delhi"));
        driver2.setStatus(DriverStatus.AVAILABLE);
        
        driver3.setCurrentLocation(new Location(28.6200, 77.2150, "Khan Market, Delhi"));
        driver3.setStatus(DriverStatus.AVAILABLE);
        
        rideService.registerDriver(driver1);
        rideService.registerDriver(driver2);
        rideService.registerDriver(driver3);
        
        // Display service statistics
        rideService.displayServiceStats();
        
        // Request rides
        System.out.println("\n3. Requesting rides...");
        Location pickup1 = new Location(28.6129, 77.2295, "India Gate, Delhi");
        Location drop1 = new Location(28.5355, 77.3910, "Noida Sector 18");
        
        Location pickup2 = new Location(28.6139, 77.2090, "Connaught Place, Delhi");
        Location drop2 = new Location(28.4595, 77.0266, "Gurgaon Cyber City");
        
        Ride ride1 = rideService.requestRide("R001", pickup1, drop1, VehicleCategory.ECONOMY);
        Ride ride2 = rideService.requestRide("R002", pickup2, drop2, VehicleCategory.PREMIUM);
        
        // Start rides
        System.out.println("\n4. Starting rides...");
        if (ride1 != null) {
            rideService.startRide(ride1.getRideId());
        }
        if (ride2 != null) {
            rideService.startRide(ride2.getRideId());
        }
        
        // Simulate ride progress
        System.out.println("\n5. Simulating ride progress...");
        try {
            Thread.sleep(2000); // Simulate 2 seconds of travel
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Update driver locations during ride
        if (ride1 != null && ride1.getDriver() != null) {
            Location newLocation1 = new Location(28.6000, 77.2500, "En route to Noida");
            rideService.updateDriverLocation(ride1.getDriver().getUserId(), newLocation1);
        }
        
        // Complete rides
        System.out.println("\n6. Completing rides...");
        if (ride1 != null) {
            rideService.completeRide(ride1.getRideId());
        }
        if (ride2 != null) {
            rideService.completeRide(ride2.getRideId());
        }
        
        // Test edge cases
        System.out.println("\n7. Testing edge cases...");
        
        // Try to request ride when no drivers available
        driver1.setStatus(DriverStatus.OFFLINE);
        driver2.setStatus(DriverStatus.BUSY);
        driver3.setStatus(DriverStatus.OFFLINE);
        
        Location pickup3 = new Location(28.7041, 77.1025, "Red Fort, Delhi");
        Location drop3 = new Location(28.6562, 77.2410, "Lotus Temple, Delhi");
        Ride ride3 = rideService.requestRide("R001", pickup3, drop3, VehicleCategory.ECONOMY);
        
        // Reset driver status and try again
        driver1.setStatus(DriverStatus.AVAILABLE);
        Ride ride4 = rideService.requestRide("R001", pickup3, drop3, VehicleCategory.ECONOMY);
        
        // Final service statistics
        rideService.displayServiceStats();
        rideService.displayRideHistory();
        
        System.out.println("\n=== RIDE SHARING DEMO COMPLETED ===");
    }
}

// Enums
enum RideStatus {
    REQUESTED, ACCEPTED, STARTED, COMPLETED, CANCELLED
}

enum DriverStatus {
    AVAILABLE, BUSY, OFFLINE
}

enum VehicleCategory {
    ECONOMY, PREMIUM, LUXURY
}

enum VehicleType {
    CAR, BIKE, TRUCK
}

// Location class
class Location {
    private double latitude;
    private double longitude;
    private String address;
    
    public Location(double latitude, double longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }
    
    public double distanceTo(Location other) {
        // Simplified distance calculation using Haversine formula approximation
        double lat1Rad = Math.toRadians(latitude);
        double lat2Rad = Math.toRadians(other.latitude);
        double deltaLat = Math.toRadians(other.latitude - latitude);
        double deltaLon = Math.toRadians(other.longitude - longitude);
        
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                   Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                   Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return 6371 * c; // Earth's radius in kilometers
    }
    
    // Getters
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getAddress() { return address; }
    
    @Override
    public String toString() {
        return address + " (" + String.format("%.4f", latitude) + ", " + String.format("%.4f", longitude) + ")";
    }
}

// User classes
class User {
    protected String userId;
    protected String name;
    protected String phone;
    protected String email;
    protected double rating;
    protected LocalDateTime registrationDate;
    
    public User(String userId, String name, String phone, String email) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.rating = 5.0;
        this.registrationDate = LocalDateTime.now();
    }
    
    // Getters and setters
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public LocalDateTime getRegistrationDate() { return registrationDate; }
}

class Rider extends User {
    private List<String> rideHistory;
    private int totalRides;
    
    public Rider(String userId, String name, String phone, String email) {
        super(userId, name, phone, email);
        this.rideHistory = new ArrayList<>();
        this.totalRides = 0;
    }
    
    public void addRideToHistory(String rideId) {
        rideHistory.add(rideId);
        totalRides++;
    }
    
    // Getters
    public List<String> getRideHistory() { return rideHistory; }
    public int getTotalRides() { return totalRides; }
}

// Vehicle for ride sharing
class RideVehicle {
    private String licensePlate;
    private VehicleCategory category;
    private String model;
    private String color;
    private int year;
    
    public RideVehicle(String licensePlate, VehicleCategory category, String model, String color) {
        this.licensePlate = licensePlate;
        this.category = category;
        this.model = model;
        this.color = color;
        this.year = 2020; // Default year
    }
    
    // Getters
    public String getLicensePlate() { return licensePlate; }
    public VehicleCategory getCategory() { return category; }
    public String getModel() { return model; }
    public String getColor() { return color; }
    public int getYear() { return year; }
    
    @Override
    public String toString() {
        return color + " " + model + " (" + licensePlate + ")";
    }
}

class Driver extends User {
    private String licenseNumber;
    private RideVehicle vehicle;
    private DriverStatus status;
    private Location currentLocation;
    private List<String> rideHistory;
    private int totalRides;
    private double totalEarnings;
    
    public Driver(String userId, String name, String phone, String email, String licenseNumber, RideVehicle vehicle) {
        super(userId, name, phone, email);
        this.licenseNumber = licenseNumber;
        this.vehicle = vehicle;
        this.status = DriverStatus.OFFLINE;
        this.rideHistory = new ArrayList<>();
        this.totalRides = 0;
        this.totalEarnings = 0.0;
    }
    
    public void addRideToHistory(String rideId) {
        rideHistory.add(rideId);
        totalRides++;
    }
    
    public void addEarnings(double amount) {
        totalEarnings += amount;
    }
    
    // Getters and setters
    public String getLicenseNumber() { return licenseNumber; }
    public RideVehicle getVehicle() { return vehicle; }
    public DriverStatus getStatus() { return status; }
    public void setStatus(DriverStatus status) { this.status = status; }
    public Location getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(Location location) { this.currentLocation = location; }
    public List<String> getRideHistory() { return rideHistory; }
    public int getTotalRides() { return totalRides; }
    public double getTotalEarnings() { return totalEarnings; }
}

// Ride class
class Ride {
    private String rideId;
    private Rider rider;
    private Driver driver;
    private Location pickupLocation;
    private Location dropLocation;
    private LocalDateTime requestTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private RideStatus status;
    private double fare;
    private double distance;
    private VehicleCategory requestedCategory;
    
    public Ride(String rideId, Rider rider, Location pickup, Location drop, VehicleCategory category) {
        this.rideId = rideId;
        this.rider = rider;
        this.pickupLocation = pickup;
        this.dropLocation = drop;
        this.requestedCategory = category;
        this.requestTime = LocalDateTime.now();
        this.status = RideStatus.REQUESTED;
        this.distance = pickup.distanceTo(drop);
    }
    
    public void assignDriver(Driver driver) {
        this.driver = driver;
        this.status = RideStatus.ACCEPTED;
    }
    
    public void startRide() {
        this.status = RideStatus.STARTED;
        this.startTime = LocalDateTime.now();
    }
    
    public void completeRide(double fare) {
        this.status = RideStatus.COMPLETED;
        this.endTime = LocalDateTime.now();
        this.fare = fare;
    }
    
    public void displayRideDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        System.out.println("\n=== Ride Details ===");
        System.out.println("Ride ID: " + rideId);
        System.out.println("Rider: " + rider.getName());
        System.out.println("Driver: " + (driver != null ? driver.getName() : "Not assigned"));
        System.out.println("Vehicle: " + (driver != null ? driver.getVehicle() : "Not assigned"));
        System.out.println("Pickup: " + pickupLocation);
        System.out.println("Drop: " + dropLocation);
        System.out.println("Distance: " + String.format("%.2f", distance) + " km");
        System.out.println("Status: " + status);
        System.out.println("Request Time: " + requestTime.format(formatter));
        if (startTime != null) {
            System.out.println("Start Time: " + startTime.format(formatter));
        }
        if (endTime != null) {
            System.out.println("End Time: " + endTime.format(formatter));
            System.out.println("Fare: $" + String.format("%.2f", fare));
        }
    }
    
    // Getters
    public String getRideId() { return rideId; }
    public Rider getRider() { return rider; }
    public Driver getDriver() { return driver; }
    public Location getPickupLocation() { return pickupLocation; }
    public Location getDropLocation() { return dropLocation; }
    public RideStatus getStatus() { return status; }
    public VehicleCategory getRequestedCategory() { return requestedCategory; }
    public double getDistance() { return distance; }
    public double getFare() { return fare; }
    public LocalDateTime getRequestTime() { return requestTime; }
    public void setStatus(RideStatus status) { this.status = status; }
}

// Pricing Strategy
class PricingService {
    private Map<VehicleCategory, Double> baseFares;
    private double pricePerKm;
    private double surgeMultiplier;
    
    public PricingService() {
        baseFares = new HashMap<>();
        baseFares.put(VehicleCategory.ECONOMY, 50.0);
        baseFares.put(VehicleCategory.PREMIUM, 80.0);
        baseFares.put(VehicleCategory.LUXURY, 150.0);
        pricePerKm = 12.0;
        surgeMultiplier = 1.0; // Normal pricing
    }
    
    public double calculateFare(Ride ride) {
        double baseFare = baseFares.get(ride.getRequestedCategory());
        double totalFare = (baseFare + (ride.getDistance() * pricePerKm)) * surgeMultiplier;
        return Math.round(totalFare * 100.0) / 100.0; // Round to 2 decimal places
    }
    
    public void setSurgeMultiplier(double multiplier) {
        this.surgeMultiplier = multiplier;
    }
    
    public double getSurgeMultiplier() {
        return surgeMultiplier;
    }
}

// Driver Matching Service
class DriverMatchingService {
    public Driver findNearestDriver(Location pickupLocation, VehicleCategory category, List<Driver> availableDrivers) {
        return availableDrivers.stream()
                .filter(driver -> driver.getStatus() == DriverStatus.AVAILABLE)
                .filter(driver -> driver.getVehicle().getCategory() == category)
                .filter(driver -> driver.getCurrentLocation() != null)
                .min((d1, d2) -> Double.compare(
                    d1.getCurrentLocation().distanceTo(pickupLocation),
                    d2.getCurrentLocation().distanceTo(pickupLocation)))
                .orElse(null);
    }
    
    public List<Driver> findNearbyDrivers(Location pickupLocation, double radiusKm, List<Driver> availableDrivers) {
        return availableDrivers.stream()
                .filter(driver -> driver.getStatus() == DriverStatus.AVAILABLE)
                .filter(driver -> driver.getCurrentLocation() != null)
                .filter(driver -> driver.getCurrentLocation().distanceTo(pickupLocation) <= radiusKm)
                .collect(Collectors.toList());
    }
}

// Main Ride Sharing Service
class RideService {
    private Map<String, Rider> riders;
    private Map<String, Driver> drivers;
    private Map<String, Ride> rides;
    private PricingService pricingService;
    private DriverMatchingService matchingService;
    private int totalRidesCompleted;
    private double totalRevenue;
    
    public RideService() {
        riders = new HashMap<>();
        drivers = new HashMap<>();
        rides = new HashMap<>();
        pricingService = new PricingService();
        matchingService = new DriverMatchingService();
        totalRidesCompleted = 0;
        totalRevenue = 0.0;
    }
    
    public void registerRider(Rider rider) {
        riders.put(rider.getUserId(), rider);
        System.out.println("Rider registered: " + rider.getName() + " (ID: " + rider.getUserId() + ")");
    }
    
    public void registerDriver(Driver driver) {
        drivers.put(driver.getUserId(), driver);
        System.out.println("Driver registered: " + driver.getName() + " (ID: " + driver.getUserId() + 
                          ", Vehicle: " + driver.getVehicle() + ")");
    }
    
    public Ride requestRide(String riderId, Location pickup, Location drop, VehicleCategory category) {
        Rider rider = riders.get(riderId);
        if (rider == null) {
            System.out.println("Rider not found: " + riderId);
            return null;
        }
        
        String rideId = "RIDE_" + UUID.randomUUID().toString().substring(0, 8);
        Ride ride = new Ride(rideId, rider, pickup, drop, category);
        
        // Find available driver
        List<Driver> availableDrivers = new ArrayList<>(drivers.values());
        Driver assignedDriver = matchingService.findNearestDriver(pickup, category, availableDrivers);
        
        if (assignedDriver == null) {
            System.out.println("No " + category + " drivers available for pickup location: " + pickup.getAddress());
            return null;
        }
        
        ride.assignDriver(assignedDriver);
        assignedDriver.setStatus(DriverStatus.BUSY);
        rides.put(rideId, ride);
        
        double estimatedFare = pricingService.calculateFare(ride);
        double driverDistance = assignedDriver.getCurrentLocation().distanceTo(pickup);
        
        System.out.println("Ride requested successfully!");
        System.out.println("Ride ID: " + rideId);
        System.out.println("Driver: " + assignedDriver.getName());
        System.out.println("Vehicle: " + assignedDriver.getVehicle());
        System.out.println("Driver distance: " + String.format("%.2f", driverDistance) + " km");
        System.out.println("Estimated fare: $" + String.format("%.2f", estimatedFare));
        
        rider.addRideToHistory(rideId);
        assignedDriver.addRideToHistory(rideId);
        
        return ride;
    }
    
    public boolean startRide(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null || ride.getStatus() != RideStatus.ACCEPTED) {
            System.out.println("Invalid ride or ride not accepted: " + rideId);
            return false;
        }
        
        ride.startRide();
        System.out.println("Ride started: " + rideId);
        System.out.println("Driver " + ride.getDriver().getName() + " is en route to destination");
        return true;
    }
    
    public boolean completeRide(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null || ride.getStatus() != RideStatus.STARTED) {
            System.out.println("Invalid ride or ride not started: " + rideId);
            return false;
        }
        
        double fare = pricingService.calculateFare(ride);
        ride.completeRide(fare);
        ride.getDriver().setStatus(DriverStatus.AVAILABLE);
        ride.getDriver().addEarnings(fare * 0.8); // Driver gets 80%
        
        totalRidesCompleted++;
        totalRevenue += fare;
        
        System.out.println("Ride completed successfully!");
        System.out.println("Final fare: $" + String.format("%.2f", fare));
        System.out.println("Driver earnings: $" + String.format("%.2f", fare * 0.8));
        
        ride.displayRideDetails();
        return true;
    }
    
    public boolean cancelRide(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null) {
            System.out.println("Ride not found: " + rideId);
            return false;
        }
        
        if (ride.getStatus() == RideStatus.COMPLETED) {
            System.out.println("Cannot cancel completed ride");
            return false;
        }
        
        ride.setStatus(RideStatus.CANCELLED);
        if (ride.getDriver() != null) {
            ride.getDriver().setStatus(DriverStatus.AVAILABLE);
        }
        
        System.out.println("Ride cancelled: " + rideId);
        return true;
    }
    
    public void updateDriverLocation(String driverId, Location location) {
        Driver driver = drivers.get(driverId);
        if (driver != null) {
            driver.setCurrentLocation(location);
            System.out.println("Updated location for driver " + driver.getName() + ": " + location);
        }
    }
    
    public void displayServiceStats() {
        System.out.println("\n=== Ride Service Statistics ===");
        System.out.println("Total Riders: " + riders.size());
        System.out.println("Total Drivers: " + drivers.size());
        System.out.println("Total Rides: " + rides.size());
        System.out.println("Completed Rides: " + totalRidesCompleted);
        System.out.println("Total Revenue: $" + String.format("%.2f", totalRevenue));
        
        long availableDrivers = drivers.values().stream()
                .mapToLong(driver -> driver.getStatus() == DriverStatus.AVAILABLE ? 1 : 0)
                .sum();
        System.out.println("Available Drivers: " + availableDrivers + "/" + drivers.size());
        
        if (pricingService.getSurgeMultiplier() > 1.0) {
            System.out.println("Surge Pricing Active: " + pricingService.getSurgeMultiplier() + "x");
        }
    }
    
    public void displayRideHistory() {
        System.out.println("\n=== Ride History ===");
        if (rides.isEmpty()) {
            System.out.println("No rides found");
            return;
        }
        
        for (Ride ride : rides.values()) {
            System.out.println("Ride " + ride.getRideId() + ": " + 
                             ride.getRider().getName() + " -> " + 
                             ride.getStatus() + " -> " +
                             (ride.getFare() > 0 ? "$" + String.format("%.2f", ride.getFare()) : "N/A"));
        }
    }
    
    public void displayDriverStats() {
        System.out.println("\n=== Driver Statistics ===");
        for (Driver driver : drivers.values()) {
            System.out.println("Driver: " + driver.getName());
            System.out.println("  Status: " + driver.getStatus());
            System.out.println("  Total Rides: " + driver.getTotalRides());
            System.out.println("  Total Earnings: $" + String.format("%.2f", driver.getTotalEarnings()));
            System.out.println("  Rating: " + String.format("%.1f", driver.getRating()));
            if (driver.getCurrentLocation() != null) {
                System.out.println("  Location: " + driver.getCurrentLocation());
            }
            System.out.println();
        }
    }
}