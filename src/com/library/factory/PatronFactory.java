// File: com/library/factory/PatronFactory.java
package com.library.factory;

import com.library.model.Patron;

public final class PatronFactory {
    private PatronFactory() {}

    public static Patron create(String name, String email) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name required");
        }
        return new Patron(name.trim(), email == null ? "" : email.trim());
    }
}
