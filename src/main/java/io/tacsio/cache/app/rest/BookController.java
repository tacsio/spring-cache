package io.tacsio.cache.app.rest;

import com.github.javafaker.Faker;
import io.tacsio.cache.app.BookRepository;
import io.tacsio.cache.app.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final BookRepository bookRepository;
    private final Faker faker;

    public BookController(BookService bookService, BookRepository bookRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.faker = new Faker();
    }

    @GetMapping
    public ResponseEntity<List<BookRepresenter>> index() {
        var books = bookService.all();
        return ResponseEntity.ok(books);

    }

    @GetMapping("/{id}")
    public ResponseEntity<BookRepresenter> get(@PathVariable Long id) {
        var book = bookService.get(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(book);
    }

    @GetMapping("/new")
    public ResponseEntity<BookRepresenter> create() {
        var newBook = bookService.create();
        return ResponseEntity.ok(newBook);
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<BookRepresenter> edit(@PathVariable Long id) {
        var editedBook = bookService.edit(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(editedBook);
    }
}