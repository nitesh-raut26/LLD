// =====================================================
// FOOD DELIVERY SYSTEM - 
// =====================================================

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

// Main Class - Must be first and public
public class FoodDeliverySystem {
    public static void main(String[] args) {
        System.out.println("=== FOOD DELIVERY SYSTEM DEMO ===");
        System.out.println("=================================\n");
        
        // Create food delivery service
        FoodDeliveryService deliveryService = new FoodDeliveryService();
        
        // Add restaurants
        System.out.println("1. Adding restaurants...");
        Location restaurant1Loc = new Location(28.6139, 77.2090, "CP Restaurant, Connaught Place");
        Restaurant restaurant1 = new Restaurant("R001", "Pizza Palace", restaurant1Loc);
        restaurant1.addMenuItem(new MenuItem("I001", "Margherita Pizza", "Classic tomato and mozzarella", 299.0, "Pizza"));
        restaurant1.addMenuItem(new MenuItem("I002", "Pepperoni Pizza", "Spicy pepperoni with cheese", 399.0, "Pizza"));
        restaurant1.addMenuItem(new MenuItem("I003", "Garlic Bread", "Crispy garlic bread with herbs", 149.0, "Sides"));
        restaurant1.addMenuItem(new MenuItem("I004", "Coke", "Chilled soft drink", 50.0, "Beverages"));
        
        Location restaurant2Loc = new Location(28.6100, 77.2000, "Burger House, Khan Market");
        Restaurant restaurant2 = new Restaurant("R002", "Burger House", restaurant2Loc);
        restaurant2.addMenuItem(new MenuItem("I005", "Classic Burger", "Beef patty with lettuce and tomato", 250.0, "Burgers"));
        restaurant2.addMenuItem(new MenuItem("I006", "Chicken Burger", "Grilled chicken with mayo", 280.0, "Burgers"));
        restaurant2.addMenuItem(new MenuItem("I007", "French Fries", "Crispy golden fries", 120.0, "Sides"));
        restaurant2.addMenuItem(new MenuItem("I008", "Milkshake", "Thick vanilla milkshake", 150.0, "Beverages"));
        
        Location restaurant3Loc = new Location(28.5355, 77.3910, "Pasta Point, Noida");
        Restaurant restaurant3 = new Restaurant("R003", "Pasta Point", restaurant3Loc);
        restaurant3.addMenuItem(new MenuItem("I009", "Spaghetti Carbonara", "Creamy pasta with bacon", 350.0, "Pasta"));
        restaurant3.addMenuItem(new MenuItem("I010", "Penne Arrabbiata", "Spicy tomato pasta", 320.0, "Pasta"));
        restaurant3.addMenuItem(new MenuItem("I011", "Caesar Salad", "Fresh lettuce with parmesan", 200.0, "Salads"));
        
        deliveryService.registerRestaurant(restaurant1);
        deliveryService.registerRestaurant(restaurant2);
        deliveryService.registerRestaurant(restaurant3);
        
        // Add customers
        System.out.println("\n2. Adding customers...");
        Customer customer1 = new Customer("C001", "John Doe", "555-1111", "john@email.com");
        customer1.addAddress(new Location(28.6200, 77.2150, "John's Home, Defence Colony"));
        
        Customer customer2 = new Customer("C002", "Jane Smith", "555-2222", "jane@email.com");
        customer2.addAddress(new Location(28.5500, 77.4000, "Jane's Office, Noida Sector 62"));
        
        Customer customer3 = new Customer("C003", "Mike Johnson", "555-3333", "mike@email.com");
        customer3.addAddress(new Location(28.6300, 77.2200, "Mike's Apartment, Lajpat Nagar"));
        
        deliveryService.registerCustomer(customer1);
        deliveryService.registerCustomer(customer2);
        deliveryService.registerCustomer(customer3);
        
        // Add delivery partners
        System.out.println("\n3. Adding delivery partners...");
        DeliveryPartner partner1 = new DeliveryPartner("P001", "Alex Rider", "555-4444", "DL01AB1234");
        partner1.setCurrentLocation(new Location(28.6100, 77.2050, "Near CP"));
        partner1.setStatus(DeliveryPartnerStatus.AVAILABLE);
        
        DeliveryPartner partner2 = new DeliveryPartner("P002", "Sarah Connor", "555-5555", "DL02CD5678");
        partner2.setCurrentLocation(new Location(28.5400, 77.3950, "Near Noida"));
        partner2.setStatus(DeliveryPartnerStatus.AVAILABLE);
        
        DeliveryPartner partner3 = new DeliveryPartner("P003", "Bruce Wayne", "555-6666", "DL03EF9012");
        partner3.setCurrentLocation(new Location(28.6250, 77.2100, "Near Defence Colony"));
        partner3.setStatus(DeliveryPartnerStatus.AVAILABLE);
        
        deliveryService.registerDeliveryPartner(partner1);
        deliveryService.registerDeliveryPartner(partner2);
        deliveryService.registerDeliveryPartner(partner3);
        
        // Display service stats
        deliveryService.displayServiceStats();
        
        // Search restaurants
        System.out.println("\n4. Searching restaurants...");
        Location customerLocation = customer1.getAddresses().get(0);
        List<Restaurant> nearbyRestaurants = deliveryService.searchRestaurants(customerLocation, "pizza");
        
        // Place orders
        System.out.println("\n5. Placing orders...");
        
        // Order 1: Pizza order
        Map<String, Integer> items1 = new HashMap<>();
        items1.put("I001", 2); // 2 Margherita Pizza
        items1.put("I003", 1); // 1 Garlic Bread
        items1.put("I004", 2); // 2 Cokes
        
        FoodOrder order1 = deliveryService.placeOrder("C001", "R001", items1, customer1.getAddresses().get(0));
        
        // Order 2: Burger order
        Map<String, Integer> items2 = new HashMap<>();
        items2.put("I005", 1); // 1 Classic Burger
        items2.put("I007", 1); // 1 French Fries
        items2.put("I008", 1); // 1 Milkshake
        
        FoodOrder order2 = deliveryService.placeOrder("C002", "R002", items2, customer2.getAddresses().get(0));
        
        // Order 3: Pasta order
        Map<String, Integer> items3 = new HashMap<>();
        items3.put("I009", 1); // 1 Spaghetti Carbonara
        items3.put("I011", 1); // 1 Caesar Salad
        
        FoodOrder order3 = deliveryService.placeOrder("C003", "R003", items3, customer3.getAddresses().get(0));
        
        // Confirm orders
        System.out.println("\n6. Confirming orders...");
        if (order1 != null) {
            deliveryService.confirmOrder(order1.getOrderId());
        }
        if (order2 != null) {
            deliveryService.confirmOrder(order2.getOrderId());
        }
        if (order3 != null) {
            deliveryService.confirmOrder(order3.getOrderId());
        }
        
        // Update order statuses
        System.out.println("\n7. Processing orders...");
        if (order1 != null) {
            deliveryService.updateOrderStatus(order1.getOrderId(), OrderStatus.PREPARING);
            deliveryService.updateOrderStatus(order1.getOrderId(), OrderStatus.READY);
            deliveryService.updateOrderStatus(order1.getOrderId(), OrderStatus.PICKED_UP);
        }
        
        if (order2 != null) {
            deliveryService.updateOrderStatus(order2.getOrderId(), OrderStatus.PREPARING);
            deliveryService.updateOrderStatus(order2.getOrderId(), OrderStatus.READY);
        }
        
        // Simulate delivery tracking
        System.out.println("\n8. Tracking deliveries...");
        if (order1 != null && order1.getAssignedPartner() != null) {
            // Update delivery partner location
            Location enRoute = new Location(28.6150, 77.2100, "En route to customer");
            deliveryService.updatePartnerLocation(order1.getAssignedPartner().getPartnerId(), enRoute);
            
            // Complete delivery
            deliveryService.updateOrderStatus(order1.getOrderId(), OrderStatus.DELIVERED);
        }
        
        // Test edge cases
        System.out.println("\n9. Testing edge cases...");
        
        // Try to order from closed restaurant
        restaurant2.setOpen(false);
        Map<String, Integer> items4 = new HashMap<>();
        items4.put("I005", 1);
        FoodOrder order4 = deliveryService.placeOrder("C001", "R002", items4, customer1.getAddresses().get(0));
        
        // Try to order unavailable item
        restaurant1.getMenu().get(0).setAvailable(false); // Make Margherita Pizza unavailable
        Map<String, Integer> items5 = new HashMap<>();
        items5.put("I001", 1);
        FoodOrder order5 = deliveryService.placeOrder("C001", "R001", items5, customer1.getAddresses().get(0));
        
        // Display final statistics
        deliveryService.displayServiceStats();
        deliveryService.displayOrderHistory("C001");
        deliveryService.displayPartnerStats();
        
        System.out.println("\n=== FOOD DELIVERY DEMO COMPLETED ===");
    }
}

// Enums
enum OrderStatus {
    PLACED, CONFIRMED, PREPARING, READY, PICKED_UP, DELIVERED, CANCELLED
}

enum DeliveryPartnerStatus {
    AVAILABLE, BUSY, OFFLINE
}

// Location class (shared with other systems)
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
        // Simplified distance calculation using Haversine formula
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
        return address;
    }
}

// Restaurant
class Restaurant {
    private String restaurantId;
    private String name;
    private Location location;
    private List<MenuItem> menu;
    private boolean isOpen;
    private double rating;
    private String cuisine;
    private int preparationTime;
    
    public Restaurant(String restaurantId, String name, Location location) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.location = location;
        this.menu = new ArrayList<>();
        this.isOpen = true;
        this.rating = 4.0;
        this.cuisine = "Multi-Cuisine";
        this.preparationTime = 30; // Default 30 minutes
    }
    
    public void addMenuItem(MenuItem item) {
        menu.add(item);
    }
    
    public void displayMenu() {
        System.out.println("\n=== " + name + " Menu ===");
        Map<String, List<MenuItem>> categorizedMenu = menu.stream()
                .collect(Collectors.groupingBy(MenuItem::getCategory));
        
        for (Map.Entry<String, List<MenuItem>> entry : categorizedMenu.entrySet()) {
            System.out.println("\n" + entry.getKey() + ":");
            for (MenuItem item : entry.getValue()) {
                System.out.println("  " + item.getName() + " - $" + item.getPrice() + 
                                 (item.isAvailable() ? "" : " (UNAVAILABLE)"));
                System.out.println("    " + item.getDescription());
            }
        }
    }
    
    // Getters and setters
    public String getRestaurantId() { return restaurantId; }
    public String getName() { return name; }
    public Location getLocation() { return location; }
    public List<MenuItem> getMenu() { return menu; }
    public boolean isOpen() { return isOpen; }
    public void setOpen(boolean open) { this.isOpen = open; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public String getCuisine() { return cuisine; }
    public int getPreparationTime() { return preparationTime; }
}

// Menu Item
class MenuItem {
    private String itemId;
    private String name;
    private String description;
    private double price;
    private boolean isAvailable;
    private String category;
    private int calories;
    
    public MenuItem(String itemId, String name, String description, double price, String category) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.isAvailable = true;
        this.calories = 0;
    }
    
    // Getters and setters
    public String getItemId() { return itemId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { this.isAvailable = available; }
    public String getCategory() { return category; }
    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }
}

// Customer
class Customer {
    private String customerId;
    private String name;
    private String phone;
    private String email;
    private List<Location> addresses;
    private List<String> orderHistory;
    private double totalSpent;
    private int totalOrders;
    
    public Customer(String customerId, String name, String phone, String email) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.addresses = new ArrayList<>();
        this.orderHistory = new ArrayList<>();
        this.totalSpent = 0.0;
        this.totalOrders = 0;
    }
    
    public void addAddress(Location address) {
        addresses.add(address);
    }
    
    public void addOrderToHistory(String orderId, double amount) {
        orderHistory.add(orderId);
        totalSpent += amount;
        totalOrders++;
    }
    
    // Getters
    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public List<Location> getAddresses() { return addresses; }
    public List<String> getOrderHistory() { return orderHistory; }
    public double getTotalSpent() { return totalSpent; }
    public int getTotalOrders() { return totalOrders; }
}

// Delivery Partner
class DeliveryPartner {
    private String partnerId;
    private String name;
    private String phone;
    private String vehicleNumber;
    private Location currentLocation;
    private DeliveryPartnerStatus status;
    private List<String> deliveryHistory;
    private double totalEarnings;
    private int totalDeliveries;
    private double rating;
    
    public DeliveryPartner(String partnerId, String name, String phone, String vehicleNumber) {
        this.partnerId = partnerId;
        this.name = name;
        this.phone = phone;
        this.vehicleNumber = vehicleNumber;
        this.status = DeliveryPartnerStatus.OFFLINE;
        this.deliveryHistory = new ArrayList<>();
        this.totalEarnings = 0.0;
        this.totalDeliveries = 0;
        this.rating = 5.0;
    }
    
    public void addDeliveryToHistory(String orderId, double earnings) {
        deliveryHistory.add(orderId);
        totalEarnings += earnings;
        totalDeliveries++;
    }
    
    // Getters and setters
    public String getPartnerId() { return partnerId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getVehicleNumber() { return vehicleNumber; }
    public Location getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(Location location) { this.currentLocation = location; }
    public DeliveryPartnerStatus getStatus() { return status; }
    public void setStatus(DeliveryPartnerStatus status) { this.status = status; }
    public List<String> getDeliveryHistory() { return deliveryHistory; }
    public double getTotalEarnings() { return totalEarnings; }
    public int getTotalDeliveries() { return totalDeliveries; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}

// Order Item
class OrderItem {
    private MenuItem menuItem;
    private int quantity;
    private double price;
    private String specialInstructions;
    
    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.price = menuItem.getPrice() * quantity;
        this.specialInstructions = "";
    }
    
    // Getters and setters
    public MenuItem getMenuItem() { return menuItem; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String instructions) { this.specialInstructions = instructions; }
}

// Food Order
class FoodOrder {
    private String orderId;
    private Customer customer;
    private Restaurant restaurant;
    private List<OrderItem> items;
    private Location deliveryAddress;
    private LocalDateTime orderTime;
    private LocalDateTime deliveryTime;
    private OrderStatus status;
    private double itemsTotal;
    private double deliveryFee;
    private double tax;
    private double totalAmount;
    private DeliveryPartner assignedPartner;
    private String paymentMethod;
    private int estimatedDeliveryTime;
    
    public FoodOrder(String orderId, Customer customer, Restaurant restaurant, Location deliveryAddress) {
        this.orderId = orderId;
        this.customer = customer;
        this.restaurant = restaurant;
        this.deliveryAddress = deliveryAddress;
        this.orderTime = LocalDateTime.now();
        this.status = OrderStatus.PLACED;
        this.items = new ArrayList<>();
        this.deliveryFee = 40.0; // Base delivery fee
        this.tax = 0.0;
        this.paymentMethod = "Cash";
        this.estimatedDeliveryTime = 45; // 45 minutes default
    }
    
    public void addItem(OrderItem item) {
        items.add(item);
        calculateTotal();
    }
    
    private void calculateTotal() {
        itemsTotal = items.stream().mapToDouble(OrderItem::getPrice).sum();
        
        // Calculate distance-based delivery fee
        double distance = restaurant.getLocation().distanceTo(deliveryAddress);
        if (distance > 5) {
            deliveryFee += (distance - 5) * 5; // Extra $5 per km beyond 5km
        }
        
        // Calculate tax (5% GST)
        tax = (itemsTotal + deliveryFee) * 0.05;
        
        totalAmount = itemsTotal + deliveryFee + tax;
        
        // Update estimated delivery time based on distance
        estimatedDeliveryTime = restaurant.getPreparationTime() + (int)(distance * 2); // 2 min per km
    }
    
    public void assignDeliveryPartner(DeliveryPartner partner) {
        this.assignedPartner = partner;
    }
    
    public void displayOrderDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        System.out.println("\n=== Order Details ===");
        System.out.println("Order ID: " + orderId);
        System.out.println("Customer: " + customer.getName());
        System.out.println("Restaurant: " + restaurant.getName());
        System.out.println("Delivery Address: " + deliveryAddress);
        System.out.println("Order Time: " + orderTime.format(formatter));
        System.out.println("Status: " + status);
        
        System.out.println("\nItems:");
        for (OrderItem item : items) {
            System.out.println("  " + item.getQuantity() + "x " + item.getMenuItem().getName() + 
                             " - $" + String.format("%.2f", item.getPrice()));
        }
        
        System.out.println("\nBill Summary:");
        System.out.println("  Items Total: $" + String.format("%.2f", itemsTotal));
        System.out.println("  Delivery Fee: $" + String.format("%.2f", deliveryFee));
        System.out.println("  Tax (5%): $" + String.format("%.2f", tax));
        System.out.println("  Total Amount: $" + String.format("%.2f", totalAmount));
        
        if (assignedPartner != null) {
            System.out.println("Delivery Partner: " + assignedPartner.getName() + 
                             " (" + assignedPartner.getVehicleNumber() + ")");
        }
        
        System.out.println("Estimated Delivery Time: " + estimatedDeliveryTime + " minutes");
        
        if (deliveryTime != null) {
            System.out.println("Delivered At: " + deliveryTime.format(formatter));
        }
    }
    
    // Getters and setters
    public String getOrderId() { return orderId; }
    public Customer getCustomer() { return customer; }
    public Restaurant getRestaurant() { return restaurant; }
    public List<OrderItem> getItems() { return items; }
    public Location getDeliveryAddress() { return deliveryAddress; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public double getTotalAmount() { return totalAmount; }
    public DeliveryPartner getAssignedPartner() { return assignedPartner; }
    public LocalDateTime getOrderTime() { return orderTime; }
    public void setDeliveryTime(LocalDateTime deliveryTime) { this.deliveryTime = deliveryTime; }
}

// Delivery Assignment Service
class DeliveryAssignmentService {
    public DeliveryPartner assignDeliveryPartner(Location restaurantLocation, List<DeliveryPartner> availablePartners) {
        return availablePartners.stream()
                .filter(partner -> partner.getStatus() == DeliveryPartnerStatus.AVAILABLE)
                .filter(partner -> partner.getCurrentLocation() != null)
                .min((p1, p2) -> Double.compare(
                    p1.getCurrentLocation().distanceTo(restaurantLocation),
                    p2.getCurrentLocation().distanceTo(restaurantLocation)))
                .orElse(null);
    }
    
    public List<DeliveryPartner> findNearbyPartners(Location location, double radiusKm, List<DeliveryPartner> partners) {
        return partners.stream()
                .filter(partner -> partner.getStatus() == DeliveryPartnerStatus.AVAILABLE)
                .filter(partner -> partner.getCurrentLocation() != null)
                .filter(partner -> partner.getCurrentLocation().distanceTo(location) <= radiusKm)
                .collect(Collectors.toList());
    }
}

// Main Food Delivery Service
class FoodDeliveryService {
    private Map<String, Restaurant> restaurants;
    private Map<String, Customer> customers;
    private Map<String, DeliveryPartner> deliveryPartners;
    private Map<String, FoodOrder> orders;
    private DeliveryAssignmentService assignmentService;
    private int totalOrdersProcessed;
    private double totalRevenue;
    
    public FoodDeliveryService() {
        restaurants = new HashMap<>();
        customers = new HashMap<>();
        deliveryPartners = new HashMap<>();
        orders = new HashMap<>();
        assignmentService = new DeliveryAssignmentService();
        totalOrdersProcessed = 0;
        totalRevenue = 0.0;
    }
    
    public void registerRestaurant(Restaurant restaurant) {
        restaurants.put(restaurant.getRestaurantId(), restaurant);
        System.out.println("Restaurant registered: " + restaurant.getName() + 
                          " at " + restaurant.getLocation());
    }
    
    public void registerCustomer(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
        System.out.println("Customer registered: " + customer.getName() + 
                          " (ID: " + customer.getCustomerId() + ")");
    }
    
    public void registerDeliveryPartner(DeliveryPartner partner) {
        deliveryPartners.put(partner.getPartnerId(), partner);
        System.out.println("Delivery partner registered: " + partner.getName() + 
                          " (Vehicle: " + partner.getVehicleNumber() + ")");
    }
    
    public List<Restaurant> searchRestaurants(Location customerLocation, String query) {
        List<Restaurant> results = restaurants.values().stream()
                .filter(restaurant -> restaurant.isOpen())
                .filter(restaurant -> {
                    // Search by name or cuisine
                    boolean nameMatch = restaurant.getName().toLowerCase().contains(query.toLowerCase());
                    boolean cuisineMatch = restaurant.getCuisine().toLowerCase().contains(query.toLowerCase());
                    
                    // Search in menu items
                    boolean menuMatch = restaurant.getMenu().stream()
                            .anyMatch(item -> item.getName().toLowerCase().contains(query.toLowerCase()) ||
                                            item.getCategory().toLowerCase().contains(query.toLowerCase()));
                    
                    return nameMatch || cuisineMatch || menuMatch;
                })
                .filter(restaurant -> restaurant.getLocation().distanceTo(customerLocation) <= 15) // Within 15km
                .sorted((r1, r2) -> Double.compare(
                    r1.getLocation().distanceTo(customerLocation),
                    r2.getLocation().distanceTo(customerLocation)))
                .collect(Collectors.toList());
        
        System.out.println("Found " + results.size() + " restaurants for query: '" + query + "'");
        for (Restaurant restaurant : results) {
            double distance = restaurant.getLocation().distanceTo(customerLocation);
            System.out.println("- " + restaurant.getName() + " (" + 
                             String.format("%.1f", distance) + " km away, Rating: " + 
                             restaurant.getRating() + "/5)");
        }
        
        return results;
    }
    
    public FoodOrder placeOrder(String customerId, String restaurantId, 
                               Map<String, Integer> itemQuantities, Location deliveryAddress) {
        Customer customer = customers.get(customerId);
        Restaurant restaurant = restaurants.get(restaurantId);
        
        if (customer == null) {
            System.out.println("Customer not found: " + customerId);
            return null;
        }
        
        if (restaurant == null || !restaurant.isOpen()) {
            System.out.println("Restaurant not available: " + restaurantId);
            return null;
        }
        
        String orderId = "ORD_" + UUID.randomUUID().toString().substring(0, 8);
        FoodOrder order = new FoodOrder(orderId, customer, restaurant, deliveryAddress);
        
        // Add items to order
        boolean hasValidItems = false;
        for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
            String itemId = entry.getKey();
            int quantity = entry.getValue();
            
            MenuItem menuItem = restaurant.getMenu().stream()
                    .filter(item -> item.getItemId().equals(itemId) && item.isAvailable())
                    .findFirst().orElse(null);
            
            if (menuItem != null && quantity > 0) {
                order.addItem(new OrderItem(menuItem, quantity));
                hasValidItems = true;
            } else if (menuItem != null) {
                System.out.println("Item not available: " + menuItem.getName());
            }
        }
        
        if (!hasValidItems) {
            System.out.println("No valid items in order");
            return null;
        }
        
        orders.put(orderId, order);
        customer.addOrderToHistory(orderId, order.getTotalAmount());
        
        System.out.println("Order placed successfully!");
        order.displayOrderDetails();
        
        return order;
    }
    
    public boolean confirmOrder(String orderId) {
        FoodOrder order = orders.get(orderId);
        if (order == null || order.getStatus() != OrderStatus.PLACED) {
            System.out.println("Invalid order: " + orderId);
            return false;
        }
        
        order.setStatus(OrderStatus.CONFIRMED);
        
        // Assign delivery partner
        List<DeliveryPartner> availablePartners = new ArrayList<>(deliveryPartners.values());
        DeliveryPartner partner = assignmentService.assignDeliveryPartner(
                order.getRestaurant().getLocation(), availablePartners);
        
        if (partner != null) {
            order.assignDeliveryPartner(partner);
            partner.setStatus(DeliveryPartnerStatus.BUSY);
            System.out.println("Order confirmed! Delivery partner assigned: " + partner.getName());
        } else {
            System.out.println("Order confirmed but no delivery partner available. Finding alternative...");
        }
        
        totalOrdersProcessed++;
        totalRevenue += order.getTotalAmount();
        
        return true;
    }
    
    public boolean updateOrderStatus(String orderId, OrderStatus newStatus) {
        FoodOrder order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order not found: " + orderId);
            return false;
        }
        
        OrderStatus oldStatus = order.getStatus();
        order.setStatus(newStatus);
        
        System.out.println("Order " + orderId + " status updated: " + oldStatus + " -> " + newStatus);
        
        if (newStatus == OrderStatus.DELIVERED) {
            order.setDeliveryTime(LocalDateTime.now());
            if (order.getAssignedPartner() != null) {
                order.getAssignedPartner().setStatus(DeliveryPartnerStatus.AVAILABLE);
                order.getAssignedPartner().addDeliveryToHistory(orderId, order.getTotalAmount() * 0.1); // 10% commission
            }
            System.out.println("Order delivered successfully!");
        }
        
        return true;
    }
    
    public List<FoodOrder> getOrderHistory(String customerId) {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            System.out.println("Customer not found: " + customerId);
            return new ArrayList<>();
        }
        
        return orders.values().stream()
                .filter(order -> order.getCustomer().getCustomerId().equals(customerId))
                .sorted((o1, o2) -> o2.getOrderTime().compareTo(o1.getOrderTime())) // Latest first
                .collect(Collectors.toList());
    }
    
    public void updatePartnerLocation(String partnerId, Location location) {
        DeliveryPartner partner = deliveryPartners.get(partnerId);
        if (partner != null) {
            partner.setCurrentLocation(location);
            System.out.println("Updated location for " + partner.getName() + ": " + location);
        }
    }
    
    public void displayServiceStats() {
        System.out.println("\n=== Food Delivery Service Statistics ===");
        System.out.println("Total Restaurants: " + restaurants.size());
        System.out.println("Total Customers: " + customers.size());
        System.out.println("Total Delivery Partners: " + deliveryPartners.size());
        System.out.println("Total Orders Processed: " + totalOrdersProcessed);
        System.out.println("Total Revenue: $" + String.format("%.2f", totalRevenue));
        
        long openRestaurants = restaurants.values().stream()
                .mapToLong(restaurant -> restaurant.isOpen() ? 1 : 0)
                .sum();
        System.out.println("Open Restaurants: " + openRestaurants + "/" + restaurants.size());
        
        long availablePartners = deliveryPartners.values().stream()
                .mapToLong(partner -> partner.getStatus() == DeliveryPartnerStatus.AVAILABLE ? 1 : 0)
                .sum();
        System.out.println("Available Delivery Partners: " + availablePartners + "/" + deliveryPartners.size());
        
        long activeOrders = orders.values().stream()
                .mapToLong(order -> order.getStatus() != OrderStatus.DELIVERED && 
                                  order.getStatus() != OrderStatus.CANCELLED ? 1 : 0)
                .sum();
        System.out.println("Active Orders: " + activeOrders);
    }
    
    public void displayOrderHistory(String customerId) {
        List<FoodOrder> customerOrders = getOrderHistory(customerId);
        Customer customer = customers.get(customerId);
        
        if (customer == null) return;
        
        System.out.println("\n=== Order History for " + customer.getName() + " ===");
        System.out.println("Total Orders: " + customer.getTotalOrders());
        System.out.println("Total Spent: $" + String.format("%.2f", customer.getTotalSpent()));
        
        if (customerOrders.isEmpty()) {
            System.out.println("No orders found");
            return;
        }
        
        System.out.println("\nRecent Orders:");
        for (FoodOrder order : customerOrders.stream().limit(5).collect(Collectors.toList())) {
            System.out.println("Order " + order.getOrderId() + " - " + order.getRestaurant().getName() + 
                             " - " + order.getStatus() + " - $" + String.format("%.2f", order.getTotalAmount()));
        }
    }
    
    public void displayPartnerStats() {
        System.out.println("\n=== Delivery Partner Statistics ===");
        for (DeliveryPartner partner : deliveryPartners.values()) {
            System.out.println("Partner: " + partner.getName());
            System.out.println("  Status: " + partner.getStatus());
            System.out.println("  Total Deliveries: " + partner.getTotalDeliveries());
            System.out.println("  Total Earnings: $" + String.format("%.2f", partner.getTotalEarnings()));
            System.out.println("  Rating: " + String.format("%.1f", partner.getRating()) + "/5");
            if (partner.getCurrentLocation() != null) {
                System.out.println("  Current Location: " + partner.getCurrentLocation());
            }
            System.out.println();
        }
    }
}