// =====================================================
// MOVIE BOOKING SYSTEM - 
// =====================================================

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

// Main Class - Must be first and public
public class MovieBookingSystem {
    public static void main(String[] args) {
        System.out.println("=== MOVIE BOOKING SYSTEM DEMO ===");
        System.out.println("=================================\n");
        
        // Create movie theater
        MovieTheater theater = new MovieTheater();
        
        // Add movies
        System.out.println("1. Adding movies...");
        Movie movie1 = new Movie("M001", "Avengers: Endgame", "Epic superhero movie", 180, "English", "Action");
        Movie movie2 = new Movie("M002", "Inception", "Mind-bending thriller", 148, "English", "Sci-Fi");
        Movie movie3 = new Movie("M003", "The Dark Knight", "Batman fights Joker", 152, "English", "Action");
        
        theater.addMovie(movie1);
        theater.addMovie(movie2);
        theater.addMovie(movie3);
        
        // Add cinema halls
        System.out.println("\n2. Adding cinema halls...");
        CinemaHall hall1 = new CinemaHall("H001", "Hall 1 - IMAX", 8, 10); // 8 rows, 10 seats per row
        CinemaHall hall2 = new CinemaHall("H002", "Hall 2 - Standard", 10, 12); // 10 rows, 12 seats per row
        
        theater.addCinemaHall(hall1);
        theater.addCinemaHall(hall2);
        
        // Add shows
        System.out.println("\n3. Adding shows...");
        LocalDateTime showTime1 = LocalDateTime.now().plusHours(2);
        LocalDateTime showTime2 = LocalDateTime.now().plusHours(5);
        LocalDateTime showTime3 = LocalDateTime.now().plusDays(1).plusHours(3);
        
        Show show1 = new Show("S001", movie1, hall1, showTime1);
        Show show2 = new Show("S002", movie2, hall2, showTime2);
        Show show3 = new Show("S003", movie1, hall2, showTime3);
        
        theater.addShow(show1);
        theater.addShow(show2);
        theater.addShow(show3);
        
        // Display show details
        System.out.println("\n4. Show details and seating layout...");
        theater.displayShowDetails("S001");
        
        // Book tickets
        System.out.println("\n5. Booking tickets...");
        List<String> seats1 = Arrays.asList("A1", "A2", "A3"); // VIP seats
        List<String> seats2 = Arrays.asList("C5", "C6"); // Premium seats
        List<String> seats3 = Arrays.asList("F1", "F2", "F3", "F4"); // Regular seats
        
        Booking booking1 = theater.bookTickets("john@email.com", "John Doe", "S001", seats1);
        Booking booking2 = theater.bookTickets("jane@email.com", "Jane Smith", "S001", seats2);
        Booking booking3 = theater.bookTickets("bob@email.com", "Bob Johnson", "S002", seats3);
        
        // Confirm bookings
        System.out.println("\n6. Confirming bookings...");
        if (booking1 != null) {
            theater.confirmBooking(booking1.getBookingId());
        }
        if (booking2 != null) {
            theater.confirmBooking(booking2.getBookingId());
        }
        if (booking3 != null) {
            theater.confirmBooking(booking3.getBookingId());
        }
        
        // Try to book already booked seats
        System.out.println("\n7. Trying to book already booked seats...");
        List<String> conflictSeats = Arrays.asList("A1", "A4"); // A1 is already booked
        Booking conflictBooking = theater.bookTickets("alice@email.com", "Alice Brown", "S001", conflictSeats);
        
        // Display updated seating layout
        System.out.println("\n8. Updated seating layout after bookings...");
        theater.displayShowDetails("S001");
        
        // Test cancellation
        System.out.println("\n9. Testing booking cancellation...");
        if (booking2 != null) {
            theater.cancelBooking(booking2.getBookingId());
        }
        
        // Display final seating layout
        System.out.println("\n10. Final seating layout after cancellation...");
        theater.displayShowDetails("S001");
        
        System.out.println("\n=== MOVIE BOOKING DEMO COMPLETED ===");
    }
}

// Enums
enum SeatType {
    REGULAR, PREMIUM, VIP
}

enum SeatStatus {
    AVAILABLE, BOOKED, BLOCKED
}

enum BookingStatus {
    PENDING, CONFIRMED, CANCELLED
}

// Movie class
class Movie {
    private String movieId;
    private String title;
    private String description;
    private int durationMinutes;
    private String language;
    private String genre;
    private LocalDateTime releaseDate;
    private double rating;
    
    public Movie(String movieId, String title, String description, int durationMinutes, String language, String genre) {
        this.movieId = movieId;
        this.title = title;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.language = language;
        this.genre = genre;
        this.releaseDate = LocalDateTime.now();
        this.rating = 0.0;
    }
    
    // Getters
    public String getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getDurationMinutes() { return durationMinutes; }
    public String getLanguage() { return language; }
    public String getGenre() { return genre; }
    public LocalDateTime getReleaseDate() { return releaseDate; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}

// Seat class
class Seat {
    private String seatNumber;
    private SeatType type;
    private SeatStatus status;
    private String row;
    private int number;
    
    public Seat(String seatNumber, SeatType type) {
        this.seatNumber = seatNumber;
        this.type = type;
        this.status = SeatStatus.AVAILABLE;
        
        // Extract row and number from seat number (e.g., "A1" -> row="A", number=1)
        this.row = seatNumber.substring(0, 1);
        this.number = Integer.parseInt(seatNumber.substring(1));
    }
    
    // Getters and setters
    public String getSeatNumber() { return seatNumber; }
    public SeatType getType() { return type; }
    public SeatStatus getStatus() { return status; }
    public void setStatus(SeatStatus status) { this.status = status; }
    public String getRow() { return row; }
    public int getNumber() { return number; }
}

// Cinema Hall
class CinemaHall {
    private String hallId;
    private String name;
    private int totalSeats;
    private List<Seat> seats;
    private int rows;
    private int seatsPerRow;
    
    public CinemaHall(String hallId, String name, int rows, int seatsPerRow) {
        this.hallId = hallId;
        this.name = name;
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
        this.totalSeats = rows * seatsPerRow;
        this.seats = new ArrayList<>();
        initializeSeats();
    }
    
    private void initializeSeats() {
        for (int i = 0; i < rows; i++) {
            char rowLetter = (char) ('A' + i);
            for (int j = 1; j <= seatsPerRow; j++) {
                String seatNumber = rowLetter + String.valueOf(j);
                
                // First 2 rows are VIP, next 3 rows are Premium, rest are Regular
                SeatType seatType;
                if (i < 2) {
                    seatType = SeatType.VIP;
                } else if (i < 5) {
                    seatType = SeatType.PREMIUM;
                } else {
                    seatType = SeatType.REGULAR;
                }
                
                seats.add(new Seat(seatNumber, seatType));
            }
        }
    }
    
    public List<Seat> getAvailableSeats() {
        return seats.stream()
                .filter(seat -> seat.getStatus() == SeatStatus.AVAILABLE)
                .collect(Collectors.toList());
    }
    
    public Seat getSeatByNumber(String seatNumber) {
        return seats.stream()
                .filter(seat -> seat.getSeatNumber().equals(seatNumber))
                .findFirst()
                .orElse(null);
    }
    
    public void displaySeatingLayout() {
        System.out.println("\n=== Seating Layout for " + name + " ===");
        System.out.println("Legend: [A] Available, [B] Booked, [X] Blocked");
        System.out.println("VIP: Rows A-B | Premium: Rows C-E | Regular: Rows F+");
        System.out.println("SCREEN");
        System.out.println("----------------------------------------");
        
        for (int i = 0; i < rows; i++) {
            char rowLetter = (char) ('A' + i);
            System.out.print(rowLetter + " ");
            
            for (int j = 1; j <= seatsPerRow; j++) {
                String seatNumber = rowLetter + String.valueOf(j);
                Seat seat = getSeatByNumber(seatNumber);
                
                char seatDisplay;
                switch (seat.getStatus()) {
                    case AVAILABLE:
                        seatDisplay = 'A';
                        break;
                    case BOOKED:
                        seatDisplay = 'B';
                        break;
                    case BLOCKED:
                        seatDisplay = 'X';
                        break;
                    default:
                        seatDisplay = '?';
                }
                System.out.print("[" + seatDisplay + "] ");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    // Getters
    public String getHallId() { return hallId; }
    public String getName() { return name; }
    public List<Seat> getSeats() { return seats; }
    public int getTotalSeats() { return totalSeats; }
    public int getRows() { return rows; }
    public int getSeatsPerRow() { return seatsPerRow; }
}

// Show
class Show {
    private String showId;
    private Movie movie;
    private CinemaHall hall;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Map<String, Double> seatPrices;
    
    public Show(String showId, Movie movie, CinemaHall hall, LocalDateTime startTime) {
        this.showId = showId;
        this.movie = movie;
        this.hall = hall;
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(movie.getDurationMinutes());
        this.seatPrices = new HashMap<>();
        initializePrices();
    }
    
    private void initializePrices() {
        seatPrices.put("REGULAR", 150.0);
        seatPrices.put("PREMIUM", 250.0);
        seatPrices.put("VIP", 400.0);
    }
    
    public List<Seat> getAvailableSeats() {
        return hall.getSeats().stream()
                .filter(seat -> seat.getStatus() == SeatStatus.AVAILABLE)
                .collect(Collectors.toList());
    }
    
    public double getSeatPrice(SeatType seatType) {
        return seatPrices.get(seatType.toString());
    }
    
    // Getters
    public String getShowId() { return showId; }
    public Movie getMovie() { return movie; }
    public CinemaHall getHall() { return hall; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public Map<String, Double> getSeatPrices() { return seatPrices; }
}

// Booking
class Booking {
    private String bookingId;
    private Show show;
    private List<Seat> seats;
    private LocalDateTime bookingTime;
    private double totalAmount;
    private String userEmail;
    private String userName;
    private BookingStatus status;
    private String paymentId;
    
    public Booking(String userEmail, String userName, Show show, List<Seat> seats) {
        this.bookingId = UUID.randomUUID().toString().substring(0, 8);
        this.userEmail = userEmail;
        this.userName = userName;
        this.show = show;
        this.seats = seats;
        this.bookingTime = LocalDateTime.now();
        this.status = BookingStatus.PENDING;
        calculateTotalAmount();
    }
    
    private void calculateTotalAmount() {
        totalAmount = seats.stream()
                .mapToDouble(seat -> show.getSeatPrice(seat.getType()))
                .sum();
    }
    
    public void displayBookingDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        System.out.println("\n=== Booking Details ===");
        System.out.println("Booking ID: " + bookingId);
        System.out.println("User: " + userName + " (" + userEmail + ")");
        System.out.println("Movie: " + show.getMovie().getTitle());
        System.out.println("Hall: " + show.getHall().getName());
        System.out.println("Show Time: " + show.getStartTime().format(formatter));
        System.out.println("Seats: " + seats.stream().map(Seat::getSeatNumber).collect(Collectors.joining(", ")));
        System.out.println("Total Amount: $" + totalAmount);
        System.out.println("Status: " + status);
        if (paymentId != null) {
            System.out.println("Payment ID: " + paymentId);
        }
    }
    
    // Getters and setters
    public String getBookingId() { return bookingId; }
    public Show getShow() { return show; }
    public List<Seat> getSeats() { return seats; }
    public double getTotalAmount() { return totalAmount; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public String getUserEmail() { return userEmail; }
    public String getUserName() { return userName; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    public String getPaymentId() { return paymentId; }
}

// Payment Service
class PaymentService {
    private Map<String, Double> payments;
    
    public PaymentService() {
        this.payments = new HashMap<>();
    }
    
    public String processPayment(double amount, String userEmail) {
        // Simulate payment processing
        String paymentId = "PAY_" + UUID.randomUUID().toString().substring(0, 8);
        payments.put(paymentId, amount);
        
        System.out.println("Payment processed successfully!");
        System.out.println("Payment ID: " + paymentId);
        System.out.println("Amount: $" + amount);
        
        return paymentId;
    }
    
    public boolean verifyPayment(String paymentId, double amount) {
        Double paidAmount = payments.get(paymentId);
        return paidAmount != null && paidAmount.equals(amount);
    }
}

// Movie Theater Management System
class MovieTheater {
    private Map<String, Movie> movies;
    private Map<String, CinemaHall> cinemaHalls;
    private Map<String, Show> shows;
    private Map<String, Booking> bookings;
    private PaymentService paymentService;
    
    public MovieTheater() {
        movies = new HashMap<>();
        cinemaHalls = new HashMap<>();
        shows = new HashMap<>();
        bookings = new HashMap<>();
        paymentService = new PaymentService();
    }
    
    public void addMovie(Movie movie) {
        movies.put(movie.getMovieId(), movie);
        System.out.println("Movie added: " + movie.getTitle() + " (" + movie.getLanguage() + ")");
    }
    
    public void addCinemaHall(CinemaHall hall) {
        cinemaHalls.put(hall.getHallId(), hall);
        System.out.println("Cinema hall added: " + hall.getName() + " (Capacity: " + hall.getTotalSeats() + ")");
    }
    
    public void addShow(Show show) {
        shows.put(show.getShowId(), show);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        System.out.println("Show added: " + show.getMovie().getTitle() + 
                          " in " + show.getHall().getName() + 
                          " at " + show.getStartTime().format(formatter));
    }
    
    public List<Show> searchShows(String movieId, LocalDateTime date) {
        return shows.values().stream()
                .filter(show -> show.getMovie().getMovieId().equals(movieId))
                .filter(show -> show.getStartTime().toLocalDate().equals(date.toLocalDate()))
                .collect(Collectors.toList());
    }
    
    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies.values());
    }
    
    public List<Show> getShowsForMovie(String movieId) {
        return shows.values().stream()
                .filter(show -> show.getMovie().getMovieId().equals(movieId))
                .collect(Collectors.toList());
    }
    
    public Booking bookTickets(String userEmail, String userName, String showId, List<String> seatNumbers) {
        Show show = shows.get(showId);
        if (show == null) {
            System.out.println("Show not found: " + showId);
            return null;
        }
        
        List<Seat> requestedSeats = new ArrayList<>();
        
        // Validate all seats
        for (String seatNumber : seatNumbers) {
            Seat seat = show.getHall().getSeatByNumber(seatNumber);
            
            if (seat == null) {
                System.out.println("Seat not found: " + seatNumber);
                return null;
            }
            
            if (seat.getStatus() != SeatStatus.AVAILABLE) {
                System.out.println("Seat not available: " + seatNumber + " (Status: " + seat.getStatus() + ")");
                return null;
            }
            
            requestedSeats.add(seat);
        }
        
        // Block seats temporarily (5 minutes to complete payment)
        requestedSeats.forEach(seat -> seat.setStatus(SeatStatus.BLOCKED));
        
        Booking booking = new Booking(userEmail, userName, show, requestedSeats);
        bookings.put(booking.getBookingId(), booking);
        
        System.out.println("Seats blocked for 5 minutes. Please complete payment.");
        booking.displayBookingDetails();
        
        return booking;
    }
    
    public boolean confirmBooking(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null || booking.getStatus() != BookingStatus.PENDING) {
            System.out.println("Invalid booking or booking already processed: " + bookingId);
            return false;
        }
        
        // Process payment
        String paymentId = paymentService.processPayment(booking.getTotalAmount(), booking.getUserEmail());
        
        if (paymentId != null) {
            // Confirm seats
            booking.getSeats().forEach(seat -> seat.setStatus(SeatStatus.BOOKED));
            booking.setStatus(BookingStatus.CONFIRMED);
            booking.setPaymentId(paymentId);
            
            System.out.println("Booking confirmed successfully!");
            booking.displayBookingDetails();
            return true;
        } else {
            // Release blocked seats
            booking.getSeats().forEach(seat -> seat.setStatus(SeatStatus.AVAILABLE));
            booking.setStatus(BookingStatus.CANCELLED);
            
            System.out.println("Payment failed. Booking cancelled and seats released.");
            return false;
        }
    }
    
    public boolean cancelBooking(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            System.out.println("Booking not found: " + bookingId);
            return false;
        }
        
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            System.out.println("Booking already cancelled");
            return false;
        }
        
        // Release seats
        booking.getSeats().forEach(seat -> seat.setStatus(SeatStatus.AVAILABLE));
        booking.setStatus(BookingStatus.CANCELLED);
        
        System.out.println("Booking cancelled successfully: " + bookingId);
        return true;
    }
    
    public void displayShowDetails(String showId) {
        Show show = shows.get(showId);
        if (show == null) {
            System.out.println("Show not found: " + showId);
            return;
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        System.out.println("\n=== Show Details ===");
        System.out.println("Show ID: " + show.getShowId());
        System.out.println("Movie: " + show.getMovie().getTitle());
        System.out.println("Hall: " + show.getHall().getName());
        System.out.println("Start Time: " + show.getStartTime().format(formatter));
        System.out.println("End Time: " + show.getEndTime().format(formatter));
        System.out.println("Available Seats: " + show.getAvailableSeats().size() + "/" + show.getHall().getTotalSeats());
        System.out.println("Seat Prices:");
        for (Map.Entry<String, Double> entry : show.getSeatPrices().entrySet()) {
            System.out.println("  " + entry.getKey() + ": $" + entry.getValue());
        }
        
        show.getHall().displaySeatingLayout();
    }
}