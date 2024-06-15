package io.tacsio.cache;

import io.tacsio.cache.app.Author;
import io.tacsio.cache.app.Book;
import io.tacsio.cache.app.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.util.Set;

@EnableCaching
@SpringBootApplication
public class CacheApplication {

    @Bean
    CommandLineRunner runner(BookRepository repository) {
        return args -> {
            var author1 = new Author("Laurentiu Spilca");
            var book1 = new Book("Troubleshooting Java: Read, debug, and optimize JVM applications",
                    Set.of(author1));

            var author2 = new Author("Mark Richards");
            var author3 = new Author("Neal Ford");

            var book2 = new Book("Fundamentals of Software Architecture: An Engineering Approach",
                    Set.of(author2, author3));

            repository.save(book1);
            repository.save(book2);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(CacheApplication.class, args);
    }

}
