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

## 🙏 Acknowledgments

- Design patterns implementation inspired by Gang of Four
- Java best practices from "Effective Java" by Joshua Bloch
- Clean code principles from Robert C. Martin

---

⭐ **Star this repository if you found it helpful!**