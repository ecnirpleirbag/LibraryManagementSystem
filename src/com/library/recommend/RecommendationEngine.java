// File: com/library/recommend/RecommendationEngine.java
package com.library.recommend;

import com.library.model.Book;
import com.library.model.Patron;

import java.util.*;
import java.util.logging.Logger;


public class RecommendationEngine {
    private static final Logger LOGGER = Logger.getLogger(RecommendationEngine.class.getName());

    public List<Book> recommend(Patron patron, Collection<Book> catalog, int limit) {
        // Create a map of ISBN to Book for quick lookup
        Map<String, Book> isbnToBook = new HashMap<>();
        for (Book book : catalog) {
            isbnToBook.put(book.getIsbn(), book);
        }
        
        // Count authors from borrow history
        Map<String, Integer> authorCount = new HashMap<>();
        for (Patron.BorrowRecord br : patron.getBorrowHistory()) {
            Book book = isbnToBook.get(br.getIsbn());
            if (book != null && book.getAuthor() != null) {
                authorCount.merge(book.getAuthor(), 1, Integer::sum);
            }
        }

        // If patron has borrow history, recommend books by preferred authors
        if (!authorCount.isEmpty()) {
            List<Book> recommendations = new ArrayList<>();
            for (Book book : catalog) {
                if (book.getStatus() == com.library.model.BookStatus.AVAILABLE && 
                    book.getAuthor() != null && 
                    authorCount.containsKey(book.getAuthor())) {
                    recommendations.add(book);
                }
            }
            if (!recommendations.isEmpty()) {
                recommendations.sort((a, b) -> {
                    int countA = authorCount.getOrDefault(a.getAuthor(), 0);
                    int countB = authorCount.getOrDefault(b.getAuthor(), 0);
                    return Integer.compare(countB, countA); // Sort by preference count descending
                });
                return recommendations.subList(0, Math.min(limit, recommendations.size()));
            }
        }

        // Fallback: recommend latest available books (by publication year)
        List<Book> available = new ArrayList<>();
        for (Book b : catalog) {
            if (b.getStatus() == com.library.model.BookStatus.AVAILABLE) {
                available.add(b);
            }
        }
        available.sort((a, b) -> Integer.compare(b.getPublicationYear(), a.getPublicationYear()));
        return available.subList(0, Math.min(limit, available.size()));
    }
}
