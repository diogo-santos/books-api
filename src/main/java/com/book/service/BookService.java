package com.book.service;

import com.book.service.domain.BookView;
import com.book.service.domain.CreateBookDto;
import com.book.service.repo.Book;
import com.book.service.repo.BookRepository;
import com.book.utils.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookService {

    private final BookRepository repo;

    public BookService(BookRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Long create(CreateBookDto createBookDto) {
        List<BookView> books = repo.findBookByTitle(createBookDto.getTitle());
        if (!CollectionUtils.isEmpty(books)) {
            throw new IllegalArgumentException("Title already exists: " + createBookDto.getTitle());
        }

        Book bookToSave = new Book();
        bookToSave.setTitle(createBookDto.getTitle());
        bookToSave.setAuthor(createBookDto.getAuthor());
        bookToSave.setCategory(createBookDto.getCategory());
        bookToSave.setImage(createBookDto.getImage());
        LocalDate publishedDate = DateUtils.convertYearOrDate(createBookDto.getPublishedDate());
        bookToSave.setPublishedDate(publishedDate);
        Book book = repo.save(bookToSave);

        return book.getId();
    }

    @Transactional
    public void delete(final Long id) {
        repo.deleteById(id);
    }
}
