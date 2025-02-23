package com.example.library_management.api.v1;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.library_management.model.Book;
import com.example.library_management.repository.SubjectRepository;
import com.example.library_management.service.AuthorService;
import com.example.library_management.service.BookAuthorService;
import com.example.library_management.service.BookService;

@RestController
public class BookApi {
  private final BookService bookService;
  private final BookAuthorService bookAuthorService;
  private final SubjectRepository subjectRepository;
  private final AuthorService authorService;

  public BookApi(
      BookService bookService,
      BookAuthorService bookAuthorService,
      SubjectRepository subjectRepository,
      AuthorService authorService
      ) {
    this.bookService = bookService;
    this.bookAuthorService = bookAuthorService;
    this.subjectRepository = subjectRepository;
    this.authorService = authorService;
  }

  @PostMapping("/create-book")
  public ResponseEntity<String> createBook(@RequestBody Book newBook, @RequestParam String authorName) {
    try {
      bookService.createBook(newBook, authorName);
      return new ResponseEntity<>("Book created successfully.", HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping("/delete-book")
  public ResponseEntity<String> deleteBook(@RequestParam(name = "isbn") String isbn) {
    try {
      bookService.deleteBook(isbn);
      return new ResponseEntity<>("Book deleted successfully.", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping("/edit-book")
  public ResponseEntity<String> updateBook(@RequestBody Book book) {
    try {
      String isbn = book.getISBN();
      bookService.updateBook(isbn, book);
      return new ResponseEntity<>("Book updated successfully.", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/get-book")
  public ResponseEntity<?> getBook(@RequestParam(name = "isbn") String isbn) {
    Optional<Book> book = bookService.getBook(isbn);
    if (book.isPresent()) return new ResponseEntity<>(book.get(), HttpStatus.OK);
    else {
      Map<String, String> message = new HashMap<>();
      message.put("message", "Can not found the book with the isbn: " + isbn);
      return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/get-all-books")
  public ResponseEntity<List<Book>> getAllBooks() {
    List<Book> books = bookService.findAllBooks();
    return new ResponseEntity<>(books, HttpStatus.OK);
  }

}
