// File: com/library/reservation/ReservationListener.java
package com.library.reservation;

import com.library.model.Book;
import com.library.model.Patron;

public interface ReservationListener {
    void onBookAvailable(Book book, Patron patron);
}
