package com.example.library_management.service;

import com.example.library_management.model.Book;
import com.example.library_management.repository.BookRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class BookService {
  private final BookRepository bookRepository;

  public BookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  public void createBook(Book newBook) throws IllegalArgumentException {
    if (newBook == null) return;

    try {
      bookRepository.save(newBook);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public void deleteBook(String ISBN) throws IllegalArgumentException {
    try {
      bookRepository.deleteById(ISBN);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public void updateBook(String ISBN, Book newBook) throws IllegalArgumentException {
    Optional<Book> bookOptional = bookRepository.findById(ISBN);
    if (bookOptional.isPresent()) {
      Book book = bookOptional.get();
      book.setTitle(newBook.getTitle());
      book.setISBN(newBook.getISBN());
      book.setLanguage(newBook.getLanguage());
      book.setSubeject(newBook.getSubeject());
      book.setPublisher(newBook.getPublisher());
      book.setNumberOfPage(newBook.getNumberOfPage());
      bookRepository.save(book);
    } else {
      throw new RuntimeException("Can not update the book");
    }
  }

  public Optional<Book> getBook(String ISBN) throws IllegalArgumentException {
    Optional<Book> book = bookRepository.findById(ISBN);
    if (book.isEmpty()) {
      throw new RuntimeException("Can not find the book with that isbn");
    }
    return book;
  }
}
