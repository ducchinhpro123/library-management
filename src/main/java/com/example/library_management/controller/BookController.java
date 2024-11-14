package com.example.library_management.controller;

import com.example.library_management.dto.BookDTO;
import com.example.library_management.model.Author;
import com.example.library_management.model.Book;
import com.example.library_management.model.Subject;
import com.example.library_management.repository.SubjectRepository;
import com.example.library_management.service.AuthorService;
import com.example.library_management.service.BookAuthorService;
import com.example.library_management.service.BookService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// @RestController("/api")
@Controller
public class BookController {
  private final BookService bookService;
  private final BookAuthorService bookAuthorService;
  private final SubjectRepository subjectRepository;
  private final AuthorService authorService;

  public BookController(BookService bookService, BookAuthorService bookAuthorService, SubjectRepository subjectRepository, AuthorService authorService) {
    this.bookService = bookService;
    this.bookAuthorService = bookAuthorService;
      this.subjectRepository = subjectRepository;
    this.authorService = authorService;
  }

  /* The table page summary books */
  @GetMapping("/table")
  public String tablePage(Model model) {
    List<Book> books = bookService.findAllBooks();
    model.addAttribute("books", books);
    return "table";
  }

  @GetMapping("/new-book")
  public String newBookPage(Model model) {
    model.addAttribute("book", new BookDTO());
    model.addAttribute("subjects", subjectRepository.findAll());
    return "new_book";
  }

  @PostMapping("/new-book")
  public String newBook(@ModelAttribute BookDTO bookDTO, Model model) {
    try {
      bookService.saveBookWithoutAuthor(bookDTO);
      model.addAttribute("message", "Book created successfully.");
      return "redirect:/table";
    } catch(IllegalArgumentException e) {
        model.addAttribute("message", "Invalid input: " + e.getMessage());
        return "redirect:/new-book";
    }
    catch (Exception e) {
      e.printStackTrace();
      model.addAttribute("message", e.getMessage());
      return "redirect:/new-book";
    }
  }

  private BookDTO convertBookToDTO(Book book) {
    BookDTO dto = new BookDTO();
    dto.setISBN(book.getISBN());
    dto.setTitle(book.getTitle());
    dto.setPublisher(book.getPublisher());
    dto.setLanguage(book.getLanguage());
    dto.setNumberOfPage(book.getNumberOfPage());
    dto.setDescription(book.getDescription());


  @GetMapping("/book/{isbn}") // 
  public String viewDetailBook(@PathVariable("isbn") String isbn, Model model) {
    Optional<Book> book = bookService.getBook(isbn);
    // th:if="${book}"
    if (book.isPresent()) {
      //System.out.println(book.get().getTitle());
      Optional<Author> author = bookAuthorService.findAuthorByBookIsbn(isbn);
      if (author.isPresent()) {
        model.addAttribute("author", author.get());
      } else {
        model.addAttribute("author", null);
      }
      model.addAttribute("book", book.get());
    } else {
      // th:if="${message}"
      model.addAttribute("message", "Can not found any book with that isbn: " + isbn);
    }

    return "book_detail";
  }

  @PostMapping("/create-book")
  public ResponseEntity<String> createBook(
      @RequestBody Book newBook, @RequestParam String authorName) {
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
}
