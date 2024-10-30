package com.example.library_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.library_management.model.Book;
import com.example.library_management.service.BookService;

import java.util.List;

@Controller
public class MainController {

  private final BookService bookService;

  public MainController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping("/home")
  public String homePage(Model model ) {
    List<Book> books = bookService.findAllBooks();
    model.addAttribute("books", books);
    return "index";
  }

  //@PostMapping("/create-book")
  //public ResponseEntity<String> createBook(@RequestBody Book newBook, @RequestParam String authorName) {
  //  try {
  //    bookService.createBook(newBook, authorName);
  //    return new ResponseEntity<>("Book created successfully.", HttpStatus.CREATED);
  //  } catch (Exception e) {
  //    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  //  }
  //}
  //
  //@DeleteMapping("/delete-book")
  //public ResponseEntity<String> deleteBook(@RequestParam(name = "isbn") String isbn) {
  //  try {
  //    bookService.deleteBook(isbn);
  //    return new ResponseEntity<>("Book deleted successfully.", HttpStatus.OK);
  //  } catch (Exception e) {
  //    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  //  }
  //}
  //
  //@PutMapping("/edit-book")
  //public ResponseEntity<String> updateBook(@RequestBody Book book) {
  //  try {
  //    String isbn = book.getISBN();
  //    bookService.updateBook(isbn, book);
  //    return new ResponseEntity<>("Book updated successfully.", HttpStatus.OK);
  //  } catch (Exception e) {
  //    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  //  }
  //}
  //
  //@GetMapping("/get-book")
  //public ResponseEntity<?> getBook(@RequestParam(name = "isbn") String isbn) {
  //  Optional<Book> book = bookService.getBook(isbn);
  //  if (book.isPresent()) return new ResponseEntity<>(book.get(), HttpStatus.OK);
  //  else {
  //    Map<String, String> message = new HashMap<>();
  //    message.put("message", "Can not found the book with the isbn: " + isbn);
  //    return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
  //  }
  //}

}
