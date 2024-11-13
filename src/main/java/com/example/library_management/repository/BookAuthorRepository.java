package com.example.library_management.repository;

import com.example.library_management.model.Author;
import com.example.library_management.model.BookAuthor;
import com.example.library_management.model.BookAuthorId;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookAuthorRepository extends JpaRepository<BookAuthor, BookAuthorId> {

  @Query("SELECT a from Author a join BookAuthor ba on a.id = ba.author.id where ba.book.ISBN = :isbn")
  Optional<Author> findAuthorByBookISBN(String isbn);

//  select author.* from author, book_author, book where
//  book_author.author_id = author.id and
//  book_author.book_id   = book.isbn and
//  book.isbn = '9780552161275';
}
