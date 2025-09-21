// File: com/library/search/TitleSearchStrategy.java
package com.library.search;

import com.library.model.Book;

import java.util.ArrayList;
import java.util.List;

public class TitleSearchStrategy implements SearchStrategy {
    @Override
    public List<Book> search(List<Book> books, String query) {
        if (query == null) return List.of();
        String q = query.toLowerCase().trim();
        List<Book> res = new ArrayList<>();
        for (Book b : books) {
            String title = b.getTitle();
            if (title != null && title.toLowerCase().contains(q)) {
                res.add(b);
            }
        }
        return res;
    }
}
