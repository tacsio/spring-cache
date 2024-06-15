package io.tacsio.cache.app.rest;

import io.tacsio.cache.app.Author;
import io.tacsio.cache.app.Book;

import java.util.Set;
import java.util.stream.Collectors;

public record BookRepresenter(Long id, String name, Set<String> authors) {

    public BookRepresenter(Book book) {
        this(book.getId(), book.getName(),
                book.getAuthors().stream().map(Author::getName).collect(Collectors.toSet()));
    }
}
