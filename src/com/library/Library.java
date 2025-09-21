// File: com/library/Library.java
package com.library;

import com.library.factory.BookFactory;
import com.library.factory.PatronFactory;
import com.library.inventory.LibraryInventory;
import com.library.model.Book;
import com.library.model.Patron;
import com.library.recommend.RecommendationEngine;
import com.library.reservation.ReservationListener;
import com.library.reservation.ReservationManager;
import com.library.search.IsbnSearchStrategy;
import com.library.search.SearchStrategy;
import com.library.search.TitleSearchStrategy;
import com.library.search.AuthorSearchStrategy;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Library implements ReservationListener {
    private static final Logger LOGGER = Logger.getLogger(Library.class.getName());

    private final ReservationManager reservationManager = new ReservationManager();
    private final LibraryInventory inventory = new LibraryInventory(reservationManager);
    private final Map<Integer, Patron> patrons = new HashMap<>();
    private final RecommendationEngine recommendEngine = new RecommendationEngine();

    private final List<SearchStrategy> searchStrategies = List.of(
            new TitleSearchStrategy(),
            new AuthorSearchStrategy(),
            new IsbnSearchStrategy()
    );

    public Library() {
        reservationManager.addListener(this);
    }

    // -- Patron management --
    public Patron addPatron(String name, String email) {
        Patron p = PatronFactory.create(name, email);
        patrons.put(p.getId(), p);
        LOGGER.log(Level.INFO, "Added patron {0}", p.getName());
        return p;
    }

    public Optional<Patron> getPatron(int id) {
        return Optional.ofNullable(patrons.get(id));
    }

    public void updatePatron(int id, String name, String email) {
        Patron p = patrons.get(id);
        if (p == null) return;
        p.setName(name);
        p.setEmail(email);
        LOGGER.log(Level.INFO, "Updated patron {0}", id);
    }

    public Collection<Patron> listPatrons() {
        return Collections.unmodifiableCollection(patrons.values());
    }

    // -- Book management --
    public void addBook(String isbn, String title, String author, int year, int copies) {
        Book b = BookFactory.create(isbn, title, author, year);
        inventory.addBook(b, copies);
    }

    public void removeBook(String isbn, int copies) {
        inventory.removeBook(isbn, copies);
    }

    public void updateBook(String isbn, String title, String author, int year) {
        inventory.updateBookInfo(isbn, title, author, year);
    }

    // -- Search (uses Strategy pattern) --
    public List<Book> searchTitle(String title) {
        return new TitleSearchStrategy().search(new ArrayList<>(inventory.getAllBooks()), title);
    }

    public List<Book> searchAuthor(String author) {
        return new AuthorSearchStrategy().search(new ArrayList<>(inventory.getAllBooks()), author);
    }

    public List<Book> searchIsbn(String isbn) {
        return new IsbnSearchStrategy().search(new ArrayList<>(inventory.getAllBooks()), isbn);
    }

    public List<Book> searchGeneric(String strategyName, String query) {
        for (SearchStrategy s : searchStrategies) {
            if (s.getClass().getSimpleName().equalsIgnoreCase(strategyName)) {
                return s.search(new ArrayList<>(inventory.getAllBooks()), query);
            }
        }
        // fallback: title search
        return new TitleSearchStrategy().search(new ArrayList<>(inventory.getAllBooks()), query);
    }

    // -- Lending --
    public boolean checkout(String isbn, int patronId) {
        Patron p = patrons.get(patronId);
        if (p == null) {
            LOGGER.log(Level.WARNING, "Checkout failed - unknown patron {0}", patronId);
            return false;
        }
        boolean ok = inventory.checkoutBook(isbn, p);
        if (!ok) {
            // if not available, auto-reserve
            LOGGER.log(Level.INFO, "Auto-reserving {0} for patron {1}", new Object[]{isbn, p.getName()});
            reservationManager.reserve(isbn, p);
        }
        return ok;
    }

    public boolean returnBook(String isbn, int patronId) {
        Patron p = patrons.get(patronId);
        if (p == null) {
            LOGGER.log(Level.WARNING, "Return failed - unknown patron {0}", patronId);
            return false;
        }
        return inventory.returnBook(isbn, p);
    }

    // -- Reservation API --
    public void reserve(String isbn, int patronId) {
        Patron p = patrons.get(patronId);
        if (p == null) {
            LOGGER.log(Level.WARNING, "Reserve failed - unknown patron {0}", patronId);
            return;
        }
        reservationManager.reserve(isbn, p);
    }

    // -- Recommendation API (simple) --
    public List<Book> recommendForPatron(int patronId, int limit) {
        Patron p = patrons.get(patronId);
        if (p == null) return Collections.emptyList();
        return recommendEngine.recommend(p, inventory.getAllBooks(), limit);
    }

    // -- ReservationListener implementation (Observer pattern) --
    @Override
    public void onBookAvailable(com.library.model.Book book, com.library.model.Patron patron) {
        // In real system: send email/SMS push. Here we log notification.
        LOGGER.log(Level.INFO, "NOTIFICATION: Book available - {0} for patron {1} ({2})",
                new Object[]{book.getTitle(), patron.getName(), patron.getEmail()});
    }
}
