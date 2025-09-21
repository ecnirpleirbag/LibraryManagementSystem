// File: com/library/model/Patron.java
package com.library.model;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Patron {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    private final int id;
    private String name;
    private String email;
    private final List<BorrowRecord> borrowHistory = new ArrayList<>();
    private final Set<String> currentBorrowedIsbns = new HashSet<>();

    public Patron(String name, String email) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && !email.trim().isEmpty()) {
            this.email = email.trim();
        }
    }

    public void addBorrowRecord(String isbn) {
        BorrowRecord r = new BorrowRecord(isbn, LocalDateTime.now(), null);
        borrowHistory.add(r);
        currentBorrowedIsbns.add(isbn);
    }

    public void returnBorrowedBook(String isbn) {
        // mark last borrow record with null return as returned now
        for (int i = borrowHistory.size() - 1; i >= 0; i--) {
            BorrowRecord r = borrowHistory.get(i);
            if (r.getIsbn().equals(isbn) && r.getReturnAt() == null) {
                r.setReturnAt(LocalDateTime.now());
                break;
            }
        }
        currentBorrowedIsbns.remove(isbn);
    }

    public List<BorrowRecord> getBorrowHistory() {
        return Collections.unmodifiableList(borrowHistory);
    }

    public Set<String> getCurrentBorrowedIsbns() {
        return Collections.unmodifiableSet(currentBorrowedIsbns);
    }

    @Override
    public String toString() {
        return "Patron{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", borrowed=" + currentBorrowedIsbns +
                '}';
    }

    public static class BorrowRecord {
        private final String isbn;
        private final LocalDateTime borrowedAt;
        private LocalDateTime returnAt;

        public BorrowRecord(String isbn, LocalDateTime borrowedAt, LocalDateTime returnAt) {
            this.isbn = isbn;
            this.borrowedAt = borrowedAt;
            this.returnAt = returnAt;
        }

        public String getIsbn() {
            return isbn;
        }

        public LocalDateTime getBorrowedAt() {
            return borrowedAt;
        }

        public LocalDateTime getReturnAt() {
            return returnAt;
        }

        public void setReturnAt(LocalDateTime returnAt) {
            this.returnAt = returnAt;
        }

        @Override
        public String toString() {
            return "BorrowRecord{" +
                    "isbn='" + isbn + '\'' +
                    ", borrowedAt=" + borrowedAt +
                    ", returnAt=" + returnAt +
                    '}';
        }
    }
}
