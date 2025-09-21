// File: com/library/Main.java
package com.library;

import com.library.model.Book;
import com.library.model.Patron;

import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {

    static {
        Logger root = Logger.getLogger("");
        root.setLevel(Level.INFO);
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.INFO);
        root.addHandler(ch);
    }

    public static void main(String[] args) {
        Library lib = new Library();

        // Add patrons
        Patron alice = lib.addPatron("Alice", "alice@example.com");
        Patron bob = lib.addPatron("Bob", "bob@example.com");

        // Add books
        lib.addBook("ISBN-001", "Effective Java", "Joshua Bloch", 2018, 2);
        lib.addBook("ISBN-002", "Clean Code", "Robert C. Martin", 2008, 1);
        lib.addBook("ISBN-003", "Design Patterns", "GoF", 1994, 1);

        // Search
        List<Book> found = lib.searchTitle("clean");
        System.out.println("Search results for 'clean': " + found);

        // Checkout and return
        boolean checked = lib.checkout("ISBN-002", bob.getId()); // Bob checks out Clean Code
        System.out.println("Bob checked out Clean Code? " + checked);

        // Alice tries to check out same book -> gets reserved
        boolean aliceChecked = lib.checkout("ISBN-002", alice.getId());
        System.out.println("Alice checked out Clean Code? " + aliceChecked + " (should be false and reserved)");

        // Bob returns -> should notify Alice and mark reserved for her
        lib.returnBook("ISBN-002", bob.getId());

        // Recommendation example
        List<Book> rec = lib.recommendForPatron(alice.getId(), 3);
        System.out.println("Recommendations for Alice: " + rec);
    }
}
