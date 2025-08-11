// =====================================================
// LIBRARY MANAGEMENT SYSTEM - FIXED VERSION
// =====================================================

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Main Class - Must be first and public
public class LibraryManagementSystem {
    public static void main(String[] args) {
        System.out.println("=== LIBRARY MANAGEMENT SYSTEM DEMO ===");
        System.out.println("======================================\n");
        
        // Create library
        Library library = new Library("City Central Library", "Main Street, Downtown");
        
        // Add books
        System.out.println("1. Adding books to library...");
        BookItem book1 = new BookItem("978-0132350884", "Clean Code", "Robert Martin", "Programming", 464, "BC001");
        BookItem book2 = new BookItem("978-0201633610", "Design Patterns", "Gang of Four", "Programming", 395, "BC002");
        BookItem book3 = new BookItem("978-0134685991", "Effective Java", "Joshua Bloch", "Programming", 412, "BC003");
        BookItem book4 = new BookItem("978-0262033848", "Introduction to Algorithms", "Thomas Cormen", "Algorithms", 1312, "BC004");
        
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
        library.addBook(book4);
        
        // Add members
        System.out.println("\n2. Adding library members...");
        LibraryMember member1 = new LibraryMember("M001", "John Doe", "123 Oak Street", "555-1234");
        LibraryMember member2 = new LibraryMember("M002", "Jane Smith", "456 Pine Avenue", "555-5678");
        
        library.addMember(member1);
        library.addMember(member2);
        
        // Display initial stats
        library.displayLibraryStats();
        
        // Search for books
        System.out.println("\n3. Searching for books...");
        library.searchBooks("programming", "subject");
        library.searchBooks("effective java", "title");
        
        // Checkout books
        System.out.println("\n4. Checking out books...");
        library.checkoutBook("M001", "BC001");
        library.checkoutBook("M001", "BC002");
        library.checkoutBook("M002", "BC003");
        library.checkoutBook("M001", "BC001"); // Try to checkout already checked out book
        
        // Display member info
        library.displayMemberInfo("M001");
        
        // Return books
        System.out.println("\n5. Returning books...");
        library.returnBook("M001", "BC001");
        library.returnBook("M002", "BC003");
        
        // Try to checkout the maximum number of books
        System.out.println("\n6. Testing borrowing limits...");
        library.checkoutBook("M001", "BC001");
        library.checkoutBook("M001", "BC003");
        library.checkoutBook("M001", "BC004");
        // M001 now has 4 books (BC002 + BC001 + BC003 + BC004)
        
        // Display final stats
        library.displayLibraryStats();
        library.displayMemberInfo("M001");
        
        System.out.println("\n=== LIBRARY MANAGEMENT DEMO COMPLETED ===");
    }
}

// Book class
class Book {
    private String isbn;
    private String title;
    private String author;
    private String subject;
    private LocalDateTime publishedDate;
    private int numberOfPages;
    private boolean isAvailable;
    
    public Book(String isbn, String title, String author, String subject, int numberOfPages) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.subject = subject;
        this.numberOfPages = numberOfPages;
        this.publishedDate = LocalDateTime.now();
        this.isAvailable = true;
    }
    
    // Getters and setters
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getSubject() { return subject; }
    public int getNumberOfPages() { return numberOfPages; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { this.isAvailable = available; }
}

// BookItem represents a copy of a book
class BookItem extends Book {
    private String barcode;
    private String rackNumber;
    private LocalDateTime borrowedDate;
    private LocalDateTime dueDate;
    
    public BookItem(String isbn, String title, String author, String subject, int numberOfPages, String barcode) {
        super(isbn, title, author, subject, numberOfPages);
        this.barcode = barcode;
    }
    
    public void checkout(String memberId) {
        this.setAvailable(false);
        this.borrowedDate = LocalDateTime.now();
        this.dueDate = LocalDateTime.now().plusDays(14); // 2 weeks loan period
    }
    
    public void returnBook() {
        this.setAvailable(true);
        this.borrowedDate = null;
        this.dueDate = null;
    }
    
    // Getters
    public String getBarcode() { return barcode; }
    public String getRackNumber() { return rackNumber; }
    public LocalDateTime getBorrowedDate() { return borrowedDate; }
    public LocalDateTime getDueDate() { return dueDate; }
    public boolean isOverdue() {
        return dueDate != null && LocalDateTime.now().isAfter(dueDate);
    }
    
    public void setRackNumber(String rackNumber) { this.rackNumber = rackNumber; }
}

// Library Member
class LibraryMember {
    private String memberId;
    private String name;
    private String address;
    private String phone;
    private LocalDateTime dateOfMembership;
    private int totalBooksCheckedOut;
    private List<BookItem> borrowedBooks;
    
    public LibraryMember(String memberId, String name, String address, String phone) {
        this.memberId = memberId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.dateOfMembership = LocalDateTime.now();
        this.totalBooksCheckedOut = 0;
        this.borrowedBooks = new ArrayList<>();
    }
    
    public boolean canBorrowBook() {
        return borrowedBooks.size() < 5; // Max 5 books per member
    }
    
    public void borrowBook(BookItem book) {
        borrowedBooks.add(book);
        totalBooksCheckedOut++;
    }
    
    public void returnBook(BookItem book) {
        borrowedBooks.remove(book);
    }
    
    // Getters
    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public LocalDateTime getDateOfMembership() { return dateOfMembership; }
    public int getTotalBooksCheckedOut() { return totalBooksCheckedOut; }
    public List<BookItem> getBorrowedBooks() { return borrowedBooks; }
}

// Library Catalog - Search interface
class LibraryCatalog {
    private Map<String, List<BookItem>> titleMap;
    private Map<String, List<BookItem>> authorMap;
    private Map<String, List<BookItem>> subjectMap;
    
    public LibraryCatalog() {
        titleMap = new HashMap<>();
        authorMap = new HashMap<>();
        subjectMap = new HashMap<>();
    }
    
    public void addBook(BookItem book) {
        titleMap.computeIfAbsent(book.getTitle().toLowerCase(), k -> new ArrayList<>()).add(book);
        authorMap.computeIfAbsent(book.getAuthor().toLowerCase(), k -> new ArrayList<>()).add(book);
        subjectMap.computeIfAbsent(book.getSubject().toLowerCase(), k -> new ArrayList<>()).add(book);
    }
    
    public List<BookItem> searchByTitle(String title) {
        return titleMap.getOrDefault(title.toLowerCase(), new ArrayList<>());
    }
    
    public List<BookItem> searchByAuthor(String author) {
        return authorMap.getOrDefault(author.toLowerCase(), new ArrayList<>());
    }
    
    public List<BookItem> searchBySubject(String subject) {
        return subjectMap.getOrDefault(subject.toLowerCase(), new ArrayList<>());
    }
}

// Fine Calculator
class FineCalculator {
    private static final double FINE_PER_DAY = 2.0;
    
    public double calculateFine(BookItem book) {
        if (!book.isOverdue()) {
            return 0.0;
        }
        
        LocalDateTime dueDate = book.getDueDate();
        LocalDateTime currentDate = LocalDateTime.now();
        long overdueDays = java.time.Duration.between(dueDate, currentDate).toDays();
        
        return overdueDays * FINE_PER_DAY;
    }
}

// Main Library System
class Library {
    private String name;
    private String address;
    private LibraryCatalog catalog;
    private Map<String, LibraryMember> members;
    private Map<String, BookItem> books;
    private FineCalculator fineCalculator;
    
    public Library(String name, String address) {
        this.name = name;
        this.address = address;
        this.catalog = new LibraryCatalog();
        this.members = new HashMap<>();
        this.books = new HashMap<>();
        this.fineCalculator = new FineCalculator();
    }
    
    public void addBook(BookItem book) {
        books.put(book.getBarcode(), book);
        catalog.addBook(book);
        System.out.println("Book added: " + book.getTitle() + " (Barcode: " + book.getBarcode() + ")");
    }
    
    public void addMember(LibraryMember member) {
        members.put(member.getMemberId(), member);
        System.out.println("Member added: " + member.getName() + " (ID: " + member.getMemberId() + ")");
    }
    
    public boolean checkoutBook(String memberId, String barcode) {
        LibraryMember member = members.get(memberId);
        BookItem book = books.get(barcode);
        
        if (member == null) {
            System.out.println("Member not found: " + memberId);
            return false;
        }
        
        if (book == null || !book.isAvailable()) {
            System.out.println("Book not available: " + barcode);
            return false;
        }
        
        if (!member.canBorrowBook()) {
            System.out.println("Member has reached borrowing limit (5 books max)");
            return false;
        }
        
        book.checkout(memberId);
        member.borrowBook(book);
        System.out.println("Book '" + book.getTitle() + "' checked out to " + member.getName());
        System.out.println("Due date: " + book.getDueDate().toLocalDate());
        return true;
    }
    
    public boolean returnBook(String memberId, String barcode) {
        LibraryMember member = members.get(memberId);
        BookItem book = books.get(barcode);
        
        if (member == null || book == null) {
            System.out.println("Invalid member ID or book barcode");
            return false;
        }
        
        if (book.isAvailable()) {
            System.out.println("Book is not currently checked out");
            return false;
        }
        
        double fine = fineCalculator.calculateFine(book);
        
        book.returnBook();
        member.returnBook(book);
        
        if (fine > 0) {
            System.out.println("Book '" + book.getTitle() + "' returned late by " + member.getName());
            System.out.println("Fine: $" + fine);
        } else {
            System.out.println("Book '" + book.getTitle() + "' returned successfully by " + member.getName());
        }
        
        return true;
    }
    
    public List<BookItem> searchBooks(String query, String searchType) {
        List<BookItem> results = new ArrayList<>();
        
        switch (searchType.toLowerCase()) {
            case "title":
                results = catalog.searchByTitle(query);
                break;
            case "author":
                results = catalog.searchByAuthor(query);
                break;
            case "subject":
                results = catalog.searchBySubject(query);
                break;
            default:
                System.out.println("Invalid search type. Use: title, author, or subject");
                return results;
        }
        
        System.out.println("Search results for " + searchType + ": '" + query + "'");
        for (BookItem book : results) {
            System.out.println("- " + book.getTitle() + " by " + book.getAuthor() + 
                             " (Available: " + book.isAvailable() + ")");
        }
        
        return results;
    }
    
    public void displayMemberInfo(String memberId) {
        LibraryMember member = members.get(memberId);
        if (member == null) {
            System.out.println("Member not found: " + memberId);
            return;
        }
        
        System.out.println("\n=== Member Information ===");
        System.out.println("Name: " + member.getName());
        System.out.println("ID: " + member.getMemberId());
        System.out.println("Total books checked out: " + member.getTotalBooksCheckedOut());
        System.out.println("Currently borrowed books: " + member.getBorrowedBooks().size());
        
        if (!member.getBorrowedBooks().isEmpty()) {
            System.out.println("Borrowed books:");
            for (BookItem book : member.getBorrowedBooks()) {
                System.out.println("- " + book.getTitle() + " (Due: " + book.getDueDate().toLocalDate() + 
                                 ", Overdue: " + book.isOverdue() + ")");
            }
        }
    }
    
    public void displayLibraryStats() {
        System.out.println("\n=== Library Statistics ===");
        System.out.println("Library: " + name);
        System.out.println("Total books: " + books.size());
        System.out.println("Total members: " + members.size());
        
        long availableBooks = books.values().stream().mapToLong(book -> book.isAvailable() ? 1 : 0).sum();
        System.out.println("Available books: " + availableBooks);
        System.out.println("Checked out books: " + (books.size() - availableBooks));
    }
}