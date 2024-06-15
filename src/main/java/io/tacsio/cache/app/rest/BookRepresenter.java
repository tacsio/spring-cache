package io.tacsio.cache.app.rest;

import io.tacsio.cache.app.model.Author;
import io.tacsio.cache.app.model.Book;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

public record BookRepresenter(Long id, String name, Set<String> authors) implements Serializable {

    public BookRepresenter(Book book) {
        this(book.getId(), book.getName(),
                book.getAuthors().stream().map(Author::getName).collect(Collectors.toSet()));
    }
}
