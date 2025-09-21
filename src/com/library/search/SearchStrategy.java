// File: com/library/search/SearchStrategy.java
package com.library.search;

import com.library.model.Book;

import java.util.List;

public interface SearchStrategy {
    List<Book> search(List<Book> books, String query);
}
