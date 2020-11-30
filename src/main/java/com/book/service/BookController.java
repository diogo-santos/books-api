package com.book.service;

import com.book.service.domain.CreateBookDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping
public class BookController {
    private final Logger logger = LoggerFactory.getLogger(BookController.class);

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @PostMapping("/books")
    @ResponseStatus(CREATED)
    public void createBook(@RequestBody @Validated CreateBookDto createBookDto) {
        logger.info("In createBook with {}", createBookDto);
        Long id = this.service.create(createBookDto);
        logger.info("Out createBook with {}", id);
    }

    @DeleteMapping("/books/{id}")
    @ResponseStatus(OK)
    public void deleteBook(@PathVariable final Long id) {
        logger.info("In deleteBook with id {}", id);
        this.service.delete(id);
        logger.info("Out deleteBook with id {}", id);
    }
}