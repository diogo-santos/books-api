package com.book.service.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
    Page<BookView> findAllBy(Pageable paging);
    BookView findBookById(Long id);
    List<BookView> findBookByTitle(String title);
}