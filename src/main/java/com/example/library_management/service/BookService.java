package com.example.library_management.service;

import com.example.library_management.model.Author;
import com.example.library_management.model.Book;
import com.example.library_management.model.BookAuthor;
import com.example.library_management.model.BookAuthorId;
import com.example.library_management.repository.AuthorRepository;
import com.example.library_management.repository.BookAuthorRepository;
import com.example.library_management.repository.BookRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class BookService {
  private final BookRepository bookRepository;
  private final AuthorRepository authorRepository;
  private final BookAuthorRepository bookAuthorRepository;

  public BookService(
      BookRepository bookRepository,
      AuthorRepository authorRepository,
      BookAuthorRepository bookAuthorRepository) {
    this.bookRepository = bookRepository;
    this.authorRepository = authorRepository;
    this.bookAuthorRepository = bookAuthorRepository;
  }

  public List<Book> findAllBooks() {
    List<Book> books = bookRepository.findAll();
    return books;
  }

  public void createBook(Book newBook, String authorName) throws IllegalArgumentException {
    /*
     * If there is no authorName, refuse to insert the book.
     * If the authorName is given without null, try to find the author with that name
     */
    if (newBook == null || authorName == null) throw new IllegalArgumentException();

    try {
      Author author = authorRepository.findByName(authorName);

      if (author == null) {
        throw new IllegalArgumentException("Author not found");
      }

      bookRepository.save(newBook);

      BookAuthorId bookAuthorId = new BookAuthorId(newBook.getISBN(), author.getId());
      BookAuthor bookAuthor = new BookAuthor();
      bookAuthor.setId(bookAuthorId);
      bookAuthor.setBook(newBook);
      bookAuthor.setAuthor(author);

      bookAuthorRepository.save(bookAuthor);

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
      book.setSubeject(newBook.getSubject());
      book.setPublisher(newBook.getPublisher());
      book.setNumberOfPage(newBook.getNumberOfPage());
      bookRepository.save(book);
    } else {
      throw new RuntimeException("Can not update the book");
    }
  }

  public Optional<Book> getBook(String ISBN) {
    Optional<Book> book = bookRepository.findById(ISBN);
    if (book.isEmpty()) {
      return Optional.empty();
    }
    return book;
  }
}
