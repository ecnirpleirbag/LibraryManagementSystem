// File: com/library/search/IsbnSearchStrategy.java
package com.library.search;

import com.library.model.Book;

import java.util.ArrayList;
import java.util.List;

public class IsbnSearchStrategy implements SearchStrategy {
    @Override
    public List<Book> search(List<Book> books, String query) {
        if (query == null) return List.of();
        String q = query.trim();
        List<Book> res = new ArrayList<>();
        for (Book b : books) {
            if (b.getIsbn().equalsIgnoreCase(q)) res.add(b);
        }
        return res;
    }
}
