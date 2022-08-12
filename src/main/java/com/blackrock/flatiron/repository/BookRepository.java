package com.blackrock.flatiron.repository;
import com.blackrock.flatiron.model.Author;
import com.blackrock.flatiron.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByTitleAndPagesAndAuthor(String title,int pages, Author author);
    Book findByTitleAndPages(String title,int pages);
    List<Book> findByTitle(String title);
}
