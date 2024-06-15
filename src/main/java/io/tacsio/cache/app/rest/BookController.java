package io.tacsio.cache.app.rest;

import com.github.javafaker.Faker;
import io.tacsio.cache.app.Author;
import io.tacsio.cache.app.Book;
import io.tacsio.cache.app.BookRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookRepository bookRepository;
    private final Faker faker;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        this.faker = new Faker();
    }

    @Cacheable("books")
    @GetMapping
    public ResponseEntity<List<BookRepresenter>> index() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);

        var books = bookRepository.findAll().stream()
                .map(BookRepresenter::new)
                .toList();

        return ResponseEntity.ok(books);
    }

    @Cacheable("books")
    @GetMapping("/{id}")
    public ResponseEntity<BookRepresenter> get(@PathVariable Long id) throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);

        var book = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(new BookRepresenter(book));
    }

    @CacheEvict("books")
    @GetMapping("/new")
    @Transactional
    public ResponseEntity<BookRepresenter> create() {
        var fakeBook = faker.book();
        var author = new Author(fakeBook.author());
        var book = new Book(fakeBook.title(), Set.of(author));

        bookRepository.save(book);

        return ResponseEntity.ok(new BookRepresenter(book));
    }

    @CacheEvict("books")
    @GetMapping("/edit/{id}")
    @Transactional
    public ResponseEntity<BookRepresenter> edit(@PathVariable Long id) {
        var editBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        editBook.setName(faker.book().title());

        return ResponseEntity.ok(new BookRepresenter(editBook));
    }
}