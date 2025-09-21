// File: com/library/reservation/ReservationManager.java
package com.library.reservation;

import com.library.model.Book;
import com.library.model.Patron;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservationManager {
    private static final Logger LOGGER = Logger.getLogger(ReservationManager.class.getName());

    private final Map<String, Deque<Patron>> reservationQueues = new HashMap<>();
    private final List<ReservationListener> listeners = new ArrayList<>();

    public synchronized void reserve(String isbn, Patron patron) {
        reservationQueues.computeIfAbsent(isbn, k -> new ArrayDeque<>()).addLast(patron);
        LOGGER.log(Level.INFO, "Patron {0} reserved ISBN {1}", new Object[]{patron.getName(), isbn});
    }

    public synchronized Optional<Patron> pollNextPatron(String isbn) {
        Deque<Patron> q = reservationQueues.get(isbn);
        if (q == null || q.isEmpty()) return Optional.empty();
        return Optional.ofNullable(q.pollFirst());
    }

    public synchronized boolean hasReservations(String isbn) {
        Deque<Patron> q = reservationQueues.get(isbn);
        return q != null && !q.isEmpty();
    }

    public synchronized void addListener(ReservationListener listener) {
        listeners.add(listener);
    }

    public synchronized void removeListener(ReservationListener listener) {
        listeners.remove(listener);
    }

    public synchronized void notifyBookAvailable(Book book, Patron patron) {
        for (ReservationListener l : listeners) {
            try {
                l.onBookAvailable(book, patron);
            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, "Listener failed: {0}", ex.getMessage());
            }
        }
    }
}
