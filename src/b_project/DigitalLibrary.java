package b_project;

import java.sql.*;
import java.util.*;

class BookNode {
    Book book;
    BookNode left, right;

    public BookNode(Book book) {
        this.book = book;
        this.left = this.right = null;
    }
}

class Book {
    private String bookId;
    private String title;
    private String author;
    private String genre;
    private String availability;

    public Book(String bookId, String title, String author, String genre, String availability) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.availability = availability;
    }

    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
    
    @Override
    public String toString() {
        return "Book ID: " + bookId + ", Title: " + title + ", Author: " + author + ", Genre: " + genre + ", Availability: " + availability;
    }
}

class Library {
    private Connection connection;
    private BookNode root;

    public Library() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "bhawani");
            System.out.println("Database connected.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBook(String bookId, String title, String author, String genre, String availability) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO books VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, bookId);
            stmt.setString(2, title);
            stmt.setString(3, author);
            stmt.setString(4, genre);
            stmt.setString(5, availability);
            stmt.executeUpdate();
            System.out.println("Book added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewBooks() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM books");
            while (rs.next()) {
                System.out.println("Book ID: " + rs.getString(1) + ", Title: " + rs.getString(2) + ", Author: " + rs.getString(3) + ", Genre: " + rs.getString(4) + ", Availability: " + rs.getString(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchBook(String searchTerm) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM books WHERE bookId = ? OR title = ?");
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Book ID: " + rs.getString(1) + ", Title: " + rs.getString(2) + ", Author: " + rs.getString(3) + ", Genre: " + rs.getString(4) + ", Availability: " + rs.getString(5));
            } else {
                System.out.println("Book not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBook(String bookId) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM books WHERE bookId = ?");
            stmt.setString(1, bookId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book deleted successfully.");
            } else {
                System.out.println("Book not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

public class DigitalLibrary {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();
        
        while (true) {
            System.out.println("\nDigital Library Management System");
            System.out.println("1. Add Book");
            System.out.println("2. View All Books");
            System.out.println("3. Search Book");
            System.out.println("4. Delete Book");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    System.out.print("Enter Book ID: ");
                    String bookId = scanner.nextLine();
                    System.out.print("Enter Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter Author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter Genre: ");
                    String genre = scanner.nextLine();
                    System.out.print("Enter Availability: ");
                    String availability = scanner.nextLine();
                    library.addBook(bookId, title, author, genre, availability);
                    break;
                case 2:
                    library.viewBooks();
                    break;
                case 3:
                    System.out.print("Enter Book ID or Title to search: ");
                    String search = scanner.nextLine();
                    library.searchBook(search);
                    break;
                case 4:
                    System.out.print("Enter Book ID to delete: ");
                    String deleteId = scanner.nextLine();
                    library.deleteBook(deleteId);
                    break;
                case 5:
                    System.out.println("Exiting system...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

