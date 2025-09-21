# Library Management System

A comprehensive Java-based Library Management System built with modern software engineering principles, featuring design patterns, robust error handling, and intelligent recommendation capabilities.

## 🚀 Features

### Core Functionality
- **Book Management**: Add, remove, update, and search books by title, author, or ISBN
- **Patron Management**: Register and manage library patrons with borrowing history
- **Inventory Control**: Track multiple copies of books with availability status
- **Reservation System**: Queue-based reservation system with automatic notifications
- **Smart Recommendations**: AI-powered book recommendations based on borrowing history
- **Search Capabilities**: Multiple search strategies with null-safe operations

### Advanced Features
- **Design Patterns**: Factory, Strategy, Observer, and Singleton patterns
- **Thread Safety**: Synchronized operations for concurrent access
- **Input Validation**: Comprehensive validation with trimming and null checks
- **Logging**: Structured logging for monitoring and debugging
- **Error Handling**: Robust error handling with graceful fallbacks

## 🏗️ Architecture

### Class Diagram
```mermaid
classDiagram
    %% Core Models
    class Book {
        -int id
        -String isbn
        -String title
        -String author
        -int publicationYear
        -BookStatus status
        +Book(String, String, String, int)
        +getId() int
        +getIsbn() String
        +getTitle() String
        +setTitle(String) void
        +getAuthor() String
        +setAuthor(String) void
        +getPublicationYear() int
        +setPublicationYear(int) void
        +getStatus() BookStatus
        +setStatus(BookStatus) void
        +equals(Object) boolean
        +hashCode() int
        +toString() String
    }

    class BookStatus {
        <<enumeration>>
        AVAILABLE
        BORROWED
        RESERVED
        LOST
    }

    class Patron {
        -int id
        -String name
        -String email
        -List~BorrowRecord~ borrowHistory
        -Set~String~ currentBorrowedIsbns
        +Patron(String, String)
        +getId() int
        +getName() String
        +setName(String) void
        +getEmail() String
        +setEmail(String) void
        +addBorrowRecord(String) void
        +returnBorrowedBook(String) void
        +getBorrowHistory() List~BorrowRecord~
        +getCurrentBorrowedIsbns() Set~String~
        +toString() String
    }

    class BorrowRecord {
        -String isbn
        -LocalDateTime borrowedAt
        -LocalDateTime returnAt
        +BorrowRecord(String, LocalDateTime, LocalDateTime)
        +getIsbn() String
        +getBorrowedAt() LocalDateTime
        +getReturnAt() LocalDateTime
        +setReturnAt(LocalDateTime) void
        +toString() String
    }

    %% Factory Pattern
    class BookFactory {
        <<utility>>
        -BookFactory()
        +create(String, String, String, int) Book
    }

    class PatronFactory {
        <<utility>>
        -PatronFactory()
        +create(String, String) Patron
    }

    %% Strategy Pattern
    class SearchStrategy {
        <<interface>>
        +search(List~Book~, String) List~Book~
    }

    class TitleSearchStrategy {
        +search(List~Book~, String) List~Book~
    }

    class AuthorSearchStrategy {
        +search(List~Book~, String) List~Book~
    }

    class IsbnSearchStrategy {
        +search(List~Book~, String) List~Book~
    }

    %% Observer Pattern
    class ReservationListener {
        <<interface>>
        +onBookAvailable(Book, Patron) void
    }

    class ReservationManager {
        -Map~String, Deque~Patron~~ reservationQueues
        -List~ReservationListener~ listeners
        +reserve(String, Patron) void
        +pollNextPatron(String) Optional~Patron~
        +hasReservations(String) boolean
        +addListener(ReservationListener) void
        +removeListener(ReservationListener) void
        +notifyBookAvailable(Book, Patron) void
    }

    %% Core System
    class Library {
        -ReservationManager reservationManager
        -LibraryInventory inventory
        -Map~Integer, Patron~ patrons
        -RecommendationEngine recommendEngine
        -List~SearchStrategy~ searchStrategies
        +Library()
        +addPatron(String, String) Patron
        +getPatron(int) Optional~Patron~
        +updatePatron(int, String, String) void
        +listPatrons() Collection~Patron~
        +addBook(String, String, String, int, int) void
        +removeBook(String, int) void
        +updateBook(String, String, String, int) void
        +searchTitle(String) List~Book~
        +searchAuthor(String) List~Book~
        +searchIsbn(String) List~Book~
        +searchGeneric(String, String) List~Book~
        +checkout(String, int) boolean
        +returnBook(String, int) boolean
        +reserve(String, int) void
        +recommendForPatron(int, int) List~Book~
        +onBookAvailable(Book, Patron) void
    }

    class LibraryInventory {
        -Map~String, Book~ isbnBookMap
        -Map~String, Integer~ availableCount
        -Map~String, Integer~ borrowedCount
        -ReservationManager reservationManager
        +LibraryInventory(ReservationManager)
        +addBook(Book, int) void
        +removeBook(String, int) void
        +updateBookInfo(String, String, String, int) void
        +getBookByIsbn(String) Optional~Book~
        +getAllBooks() Collection~Book~
        +getAvailableCopies(String) int
        +isAvailable(String) boolean
        +checkoutBook(String, Patron) boolean
        +returnBook(String, Patron) boolean
    }

    class RecommendationEngine {
        +recommend(Patron, Collection~Book~, int) List~Book~
    }

    class Main {
        +main(String[]) void
    }

    %% Relationships
    Book ||--|| BookStatus : has
    Patron ||--o{ BorrowRecord : contains
    BookFactory ..> Book : creates
    PatronFactory ..> Patron : creates
    SearchStrategy <|.. TitleSearchStrategy : implements
    SearchStrategy <|.. AuthorSearchStrategy : implements
    SearchStrategy <|.. IsbnSearchStrategy : implements
    ReservationListener <|.. Library : implements
    Library ||--o{ ReservationManager : uses
    Library ||--o{ LibraryInventory : uses
    Library ||--o{ RecommendationEngine : uses
    Library ||--o{ SearchStrategy : uses
    Library ||--o{ Patron : manages
    LibraryInventory ||--o{ ReservationManager : uses
    LibraryInventory ||--o{ Book : manages
    RecommendationEngine ..> Patron : uses
    RecommendationEngine ..> Book : uses
    Main ..> Library : uses
```

### Design Patterns Implemented
- **Factory Pattern**: `BookFactory` and `PatronFactory` for object creation
- **Strategy Pattern**: `SearchStrategy` implementations for different search types
- **Observer Pattern**: `ReservationListener` for notification system
- **Singleton Pattern**: Logger instances and ID generators

### Package Structure
```
src/com/library/
├── Library.java                 # Main library controller
├── Main.java                   # Application entry point
├── factory/                    # Factory pattern implementations
│   ├── BookFactory.java
│   └── PatronFactory.java
├── inventory/                  # Inventory management
│   └── LibraryInventory.java
├── model/                      # Data models
│   ├── Book.java
│   ├── BookStatus.java
│   └── Patron.java
├── recommend/                  # Recommendation engine
│   └── RecommendationEngine.java
├── reservation/                # Reservation system
│   ├── ReservationListener.java
│   └── ReservationManager.java
└── search/                     # Search strategies
    ├── SearchStrategy.java
    ├── TitleSearchStrategy.java
    ├── AuthorSearchStrategy.java
    └── IsbnSearchStrategy.java
```

## 🛠️ Technologies Used

- **Java 8+**: Core programming language
- **Design Patterns**: Factory, Strategy, Observer, Singleton
- **Concurrency**: Thread-safe operations with synchronized methods
- **Logging**: Java Util Logging (JUL)
- **Collections**: HashMap, ArrayList, Deque for efficient data management

## 📋 Prerequisites

- Java Development Kit (JDK) 8 or higher
- Git (for cloning the repository)

## 🚀 Getting Started

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/ecnirpleirbag/LibraryManagementSystem.git
   cd LibraryManagementSystem
   ```

2. **Compile the project**
   ```bash
   javac -d . src/com/library/*.java src/com/library/*/*.java
   ```

3. **Run the application**
   ```bash
   java com.library.Main
   ```

### Usage Example

```java
// Create library instance
Library library = new Library();

// Add patrons
Patron alice = library.addPatron("Alice Johnson", "alice@email.com");
Patron bob = library.addPatron("Bob Smith", "bob@email.com");

// Add books
library.addBook("ISBN-001", "Effective Java", "Joshua Bloch", 2018, 2);
library.addBook("ISBN-002", "Clean Code", "Robert C. Martin", 2008, 1);

// Search books
List<Book> results = library.searchTitle("clean");

// Checkout and return books
boolean success = library.checkout("ISBN-002", bob.getId());
library.returnBook("ISBN-002", bob.getId());

// Get recommendations
List<Book> recommendations = library.recommendForPatron(alice.getId(), 3);
```

## 🔍 API Reference

### Library Management
- `addPatron(String name, String email)` - Register a new patron
- `addBook(String isbn, String title, String author, int year, int copies)` - Add book copies
- `removeBook(String isbn, int copies)` - Remove book copies
- `updateBook(String isbn, String title, String author, int year)` - Update book information

### Search Operations
- `searchTitle(String title)` - Search books by title
- `searchAuthor(String author)` - Search books by author
- `searchIsbn(String isbn)` - Search books by ISBN
- `searchGeneric(String strategy, String query)` - Generic search with strategy selection

### Lending Operations
- `checkout(String isbn, int patronId)` - Check out a book
- `returnBook(String isbn, int patronId)` - Return a book
- `reserve(String isbn, int patronId)` - Reserve a book

### Recommendations
- `recommendForPatron(int patronId, int limit)` - Get personalized book recommendations

## 🧪 Testing

The system includes comprehensive error handling and validation:

- **Null Safety**: All search operations handle null values gracefully
- **Input Validation**: Automatic trimming and empty string validation
- **State Management**: Consistent book status management
- **Concurrency**: Thread-safe operations for multi-user environments

## 🔧 Configuration

### Logging Configuration
The system uses Java Util Logging with configurable levels:
- **INFO**: General operational messages
- **WARNING**: Non-critical issues
- **ERROR**: Critical errors

### ID Generation
- **Books**: Auto-incrementing integer IDs
- **Patrons**: Auto-incrementing integer IDs
- **Thread-Safe**: Uses AtomicInteger for concurrent access

## 📊 Performance Features

- **Efficient Search**: O(n) search operations with early termination
- **Memory Management**: Proper object lifecycle management
- **Concurrent Access**: Synchronized methods for thread safety
- **Caching**: In-memory caching for frequently accessed data

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is open source and available under the [MIT License](LICENSE).

## 👨‍💻 Author

**Gabriel Prince**
- GitHub: [@ecnirpleirbag](https://github.com/ecnirpleirbag)
- Email: gabrielprince2k@gmail.com

## 📝 Assignment Submission

This repository contains the complete implementation of a Library Management System for the Java OOP assignment, demonstrating design patterns, SOLID principles, and comprehensive functionality.

## 🙏 Acknowledgments

- Design patterns implementation inspired by Gang of Four
- Java best practices from "Effective Java" by Joshua Bloch
- Clean code principles from Robert C. Martin

---

⭐ **Star this repository if you found it helpful!**