// File: com/library/model/Book.java
package com.library.model;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Book {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    private final int id;
    private final String isbn;
    private String title;
    private String author;
    private int publicationYear;

    private BookStatus status;

    public Book(String isbn, String title, String author, int publicationYear) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.status = BookStatus.AVAILABLE;
    }

    public int getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title != null && !title.trim().isEmpty()) {
            this.title = title.trim();
        }
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if (author != null && !author.trim().isEmpty()) {
            this.author = author.trim();
        }
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        if (publicationYear > 0) this.publicationYear = publicationYear;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;
        return isbn.equals(book.isbn); // ISBN uniquely identifies a book edition
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publicationYear=" + publicationYear +
                ", status=" + status +
                '}';
    }
}
