package com.book.service.repo;

import com.book.service.domain.BookView;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
    List<BookView> findBookByTitle(String title);
}