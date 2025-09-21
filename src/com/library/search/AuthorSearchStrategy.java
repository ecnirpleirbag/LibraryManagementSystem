// File: com/library/search/AuthorSearchStrategy.java
package com.library.search;

import com.library.model.Book;

import java.util.ArrayList;
import java.util.List;

public class AuthorSearchStrategy implements SearchStrategy {
    @Override
    public List<Book> search(List<Book> books, String query) {
        if (query == null) return List.of();
        String q = query.toLowerCase().trim();
        List<Book> res = new ArrayList<>();
        for (Book b : books) {
            String author = b.getAuthor();
            if (author != null && author.toLowerCase().contains(q)) {
                res.add(b);
            }
        }
        return res;
    }
}
