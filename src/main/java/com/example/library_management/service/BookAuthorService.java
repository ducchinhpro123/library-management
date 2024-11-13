package com.example.library_management.service;

import com.example.library_management.model.Author;
import com.example.library_management.repository.BookAuthorRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class BookAuthorService {
  private final BookAuthorRepository bookAuthorRepository;

  public BookAuthorService(BookAuthorRepository bookAuthorRepository) {
    this.bookAuthorRepository = bookAuthorRepository;
  }

  public Optional<Author> findAuthorByBookIsbn(String isbn) {
      return bookAuthorRepository.findAuthorByBookISBN(isbn);
  }
}
