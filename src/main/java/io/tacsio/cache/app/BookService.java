package io.tacsio.cache.app;

import com.github.javafaker.Faker;
import io.tacsio.cache.app.rest.BookRepresenter;
import io.tacsio.cache.app.model.Author;
import io.tacsio.cache.app.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BookService {
    private final Logger log = LoggerFactory.getLogger(BookService.class);
    private final Faker faker = new Faker();

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Cacheable("books")
    public List<BookRepresenter> all() {
        log.info("call all");

        return bookRepository.findAll().stream()
                .map(BookRepresenter::new)
                .toList();
    }

    @Cacheable("books")
    public Optional<BookRepresenter> get(Long id) {
        log.info("call get {}", id);

        var book = bookRepository.findById(id);
        return book.map(BookRepresenter::new);

    }

    @Transactional
    @CacheEvict("books")
    public BookRepresenter create() {
        log.info("call create");

        var fakeBook = faker.book();

        var author = new Author(fakeBook.author());
        var newBook = new Book(fakeBook.title(), Set.of(author));

        bookRepository.save(newBook);
        return new BookRepresenter(newBook);
    }

    @Transactional
    @CacheEvict("books")
    public Optional<BookRepresenter> edit(Long id) {
        log.info("call edit {}", id);

        var optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) return Optional.empty();

        var editBook = optionalBook.get();
        editBook.setName(faker.book().title());

        return Optional.of(new BookRepresenter(editBook));
    }
}
