package com.book.service;

import com.book.service.repo.Book;
import com.book.service.repo.BookView;
import com.book.service.domain.CreateBookDto;
import com.book.service.domain.PageBookDto;
import com.book.service.repo.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.util.StringUtils.hasText;

@Service
public class BookService {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
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
        Book bookToSave = new Book();
        bookToSave.setTitle(createBookDto.getTitle());
        bookToSave.setAuthor(createBookDto.getAuthor());
        bookToSave.setCategory(createBookDto.getCategory());
        bookToSave.setImage(createBookDto.getImage());
        bookToSave.setPublishedDate(convertDate(createBookDto.getPublishedDate()));
        Book book = repo.save(bookToSave);

        return book.getId();
    }

    private LocalDate convertDate(String dateOrYearStr) {
        LocalDate publishedDate = null;
        if (hasText(dateOrYearStr)) {
            try {
                if (dateOrYearStr.length() == 4) {
                    publishedDate = LocalDate.of(Integer.parseInt(dateOrYearStr), 1, 1);
                } else if (dateOrYearStr.length() == 10) {
                    publishedDate = LocalDate.parse(dateOrYearStr, DateTimeFormatter.ofPattern(DATE_FORMAT));
                }
            } catch (Exception e) {
              throw new IllegalArgumentException(String.format("Invalid date %s. It should be a year (yyyy) or date (%s) format", dateOrYearStr, DATE_FORMAT), e);
            }
        }
        return publishedDate;
    }

    public void delete(final Long id) {
        repo.deleteById(id);
    }
}
