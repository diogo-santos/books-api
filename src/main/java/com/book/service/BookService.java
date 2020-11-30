package com.book.service;

import com.book.service.repo.Book;
import com.book.service.repo.BookView;
import com.book.service.domain.CreateBookDto;
import com.book.service.domain.PageBookDto;
import com.book.service.repo.BookRepository;
import com.book.utils.DateUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Service
public class BookService {

    private final BookRepository repo;

    public BookService(BookRepository repo) {
        this.repo = repo;
    }

    public PageBookDto getAllBooks(final int pageNumber, final int pageSize, final String sortBy) {
        int page = pageNumber < 1 ? 0 : pageNumber - 1;
        int size = pageSize < 1 ? 5 : pageSize;
        String sortField = hasText(sortBy) ? sortBy : "publishedDate";

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortField));
        Page<BookView> pageBooks = repo.findAllBy(pageable);

        return PageBookDto.from(pageBooks);
    }

    public BookView getBookById(final Long id) {
        return repo.findBookById(id);
    }

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

    public void delete(final Long id) {
        repo.deleteById(id);
    }
}
