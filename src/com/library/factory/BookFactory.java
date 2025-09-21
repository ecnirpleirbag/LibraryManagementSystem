// File: com/library/factory/BookFactory.java
package com.library.factory;

import com.library.model.Book;

/**
 * Factory pattern for creating Book objects.
 */
public final class BookFactory {
    private BookFactory() {}

    public static Book create(String isbn, String title, String author, int year) {
        // possible validation/normalization here
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN cannot be empty");
        }
        return new Book(isbn.trim(), title == null ? "" : title.trim(), author == null ? "" : author.trim(), year);
    }
}
