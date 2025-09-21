// File: com/library/inventory/LibraryInventory.java
package com.library.inventory;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.Patron;
import com.library.reservation.ReservationManager;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LibraryInventory {
    private static final Logger LOGGER = Logger.getLogger(LibraryInventory.class.getName());

    private final Map<String, Book> isbnBookMap = new HashMap<>();
    private final Map<String, Integer> availableCount = new HashMap<>(); // isbn -> copies available
    private final Map<String, Integer> borrowedCount = new HashMap<>(); // isbn -> copies borrowed

    private final ReservationManager reservationManager;

    public LibraryInventory(ReservationManager reservationManager) {
        this.reservationManager = reservationManager;
    }

    public synchronized void addBook(Book book, int copies) {
        if (book == null || copies <= 0) throw new IllegalArgumentException("Invalid book/copies");
        isbnBookMap.putIfAbsent(book.getIsbn(), book);
        availableCount.merge(book.getIsbn(), copies, Integer::sum);
        LOGGER.log(Level.INFO, "Added {0} copies of {1}", new Object[]{copies, book.getIsbn()});
    }

    public synchronized void removeBook(String isbn, int copies) {
        if (isbn == null || copies <= 0) return;
        Integer avail = availableCount.get(isbn);
        if (avail == null || avail < copies) {
            LOGGER.log(Level.WARNING, "Not enough copies to remove {0}", isbn);
            return;
        }
        availableCount.put(isbn, avail - copies);
        if (availableCount.get(isbn) == 0 && borrowedCount.getOrDefault(isbn, 0) == 0) {
            isbnBookMap.remove(isbn);
            availableCount.remove(isbn);
            borrowedCount.remove(isbn);
            LOGGER.log(Level.INFO, "Removed book entirely from inventory: {0}", isbn);
        } else {
            LOGGER.log(Level.INFO, "Removed {0} copies of {1}", new Object[]{copies, isbn});
        }
    }

    public synchronized void updateBookInfo(String isbn, String title, String author, int year) {
        Book b = isbnBookMap.get(isbn);
        if (b == null) {
            LOGGER.log(Level.WARNING, "Book not found: {0}", isbn);
            return;
        }
        b.setTitle(title);
        b.setAuthor(author);
        if (year > 0) b.setPublicationYear(year);
        LOGGER.log(Level.INFO, "Updated book info for {0}", isbn);
    }

    public synchronized Optional<Book> getBookByIsbn(String isbn) {
        return Optional.ofNullable(isbnBookMap.get(isbn));
    }

    public synchronized Collection<Book> getAllBooks() {
        return Collections.unmodifiableCollection(isbnBookMap.values());
    }

    public synchronized int getAvailableCopies(String isbn) {
        return availableCount.getOrDefault(isbn, 0);
    }

    public synchronized boolean isAvailable(String isbn) {
        return getAvailableCopies(isbn) > 0;
    }

    public synchronized boolean checkoutBook(String isbn, Patron patron) {
        if (!isbnBookMap.containsKey(isbn)) {
            LOGGER.log(Level.WARNING, "Checkout failed - book unknown: {0}", isbn);
            return false;
        }
        if (!isAvailable(isbn)) {
            // allow reservation
            LOGGER.log(Level.INFO, "Book not available for checkout, consider reservation: {0}", isbn);
            return false;
        }
        // reduce available, increase borrowed
        availableCount.put(isbn, availableCount.get(isbn) - 1);
        borrowedCount.merge(isbn, 1, Integer::sum);

        // Only set status to BORROWED if no copies are available
        Book b = isbnBookMap.get(isbn);
        if (availableCount.get(isbn) == 0) {
            b.setStatus(BookStatus.BORROWED);
        }
        patron.addBorrowRecord(isbn);
        LOGGER.log(Level.INFO, "Patron {0} checked out ISBN {1}", new Object[]{patron.getName(), isbn});
        return true;
    }

    public synchronized boolean returnBook(String isbn, Patron patron) {
        if (!isbnBookMap.containsKey(isbn)) {
            LOGGER.log(Level.WARNING, "Return failed - book unknown: {0}", isbn);
            return false;
        }

        int borrowed = borrowedCount.getOrDefault(isbn, 0);
        if (borrowed <= 0) {
            LOGGER.log(Level.WARNING, "Return failed - no borrowed copies recorded for {0}", isbn);
            // still allow adding as available
            availableCount.merge(isbn, 1, Integer::sum);
            return false;
        }

        borrowedCount.put(isbn, borrowed - 1);
        availableCount.merge(isbn, 1, Integer::sum);
        patron.returnBorrowedBook(isbn);

        Book b = isbnBookMap.get(isbn);
        // Only set status to AVAILABLE if there are available copies and no reservations
        if (availableCount.get(isbn) > 0 && !reservationManager.hasReservations(isbn)) {
            b.setStatus(BookStatus.AVAILABLE);
        }
        LOGGER.log(Level.INFO, "Patron {0} returned ISBN {1}", new Object[]{patron.getName(), isbn});

        // If there are reservations, notify next patron and mark reserved
        if (reservationManager.hasReservations(isbn)) {
            Optional<Patron> next = reservationManager.pollNextPatron(isbn);
            if (next.isPresent()) {
                Patron nextPatron = next.get();
                // mark book as RESERVED until nextPatron checks out
                b.setStatus(BookStatus.RESERVED);
                // decrement available since reserved for that patron
                int avail = availableCount.getOrDefault(isbn, 0);
                if (avail > 0) {
                    availableCount.put(isbn, avail - 1);
                }
                reservationManager.notifyBookAvailable(b, nextPatron);
                LOGGER.log(Level.INFO, "Notified patron {0} for reserved book {1}", new Object[]{nextPatron.getName(), isbn});
            }
        }

        return true;
    }
}
