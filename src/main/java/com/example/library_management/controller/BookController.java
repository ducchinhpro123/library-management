package com.example.library_management.controller;

import com.example.library_management.annotation.CheckAdmin;
import com.example.library_management.dto.BookDTO;
import com.example.library_management.model.Author;
import com.example.library_management.model.Book;
// import com.example.library_management.model.BookLending;
import com.example.library_management.model.Subject;

import com.example.library_management.repository.SubjectRepository;
import com.example.library_management.service.AuthorService;
import com.example.library_management.service.BookAuthorService;
import com.example.library_management.service.BookService;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

// @RestController("/api")
@Controller
public class BookController {
  private final BookService bookService;
  private final BookAuthorService bookAuthorService;
  private final SubjectRepository subjectRepository;
  private final AuthorService authorService;

  public BookController(
      BookService bookService,
      BookAuthorService bookAuthorService,
      SubjectRepository subjectRepository,
      AuthorService authorService) {
    this.bookService = bookService;
    this.bookAuthorService = bookAuthorService;
    this.subjectRepository = subjectRepository;
    this.authorService = authorService;
  }

  /**
   * NOTE: About the CheckAdmin annotation: it is denoted that only admin have
   * this permission to perform this action.
  */

  @CheckAdmin
  @GetMapping("/table")
   * this permission to perform this action
   * 'CheckLibrarian' annotation: Admin and Librarian will be able to access this page
   * In other word: CheckAdmin retricts everyone except admin
   *                CheckLibrarian retricts everyone except librarian and admin
   * otherwise, return to 404 page
   */

  public String tablePage(Model model) {
    List<Book> books = bookService.findAllBooks();

    model.addAttribute("books", books);
    return "table";
  }

  @CheckAdmin
  @GetMapping("/new-book")
  public String newBookPage(Model model) {
    model.addAttribute("book", new BookDTO());
    model.addAttribute("subjects", subjectRepository.findAll());
    return "new_book";
  }

  @CheckAdmin
  @PostMapping("/new-book")
  public String newBook(@ModelAttribute BookDTO bookDTO, Model model) {
    try {
      bookService.saveBookWithoutAuthor(bookDTO);
      model.addAttribute("message", "Book created successfully.");
      return "redirect:/table";
    } catch (IllegalArgumentException e) {
      model.addAttribute("message", "Invalid input: " + e.getMessage());
      return "redirect:/new-book";
    } catch (Exception e) {
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

    return dto;
  }

  @CheckAdmin
  @PostMapping("/update-book")
  public String postUpdateBook(@ModelAttribute BookDTO dto, RedirectAttributes redirect) {
    try {
      bookService.update(dto);
      redirect.addFlashAttribute("message", "Updated successful");
      return "redirect:/table";
    } catch (Exception e) {
      // WARNING: Don't do this in production
      redirect.addFlashAttribute("message", e.getMessage());
      return "redirect:/table";
    }
  }

  @CheckAdmin
  @GetMapping("/update-book/{isbn}")
  public String updateBook(@PathVariable("isbn") String isbn, RedirectAttributes redirect, Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    boolean isAdmin = auth.getAuthorities().stream().anyMatch(granted -> granted.getAuthority().equals("ROLE_ADMIN"));
    if (isAdmin) {
      try {
        Optional<Book> book = bookService.getBook(isbn);
        if (book.isPresent()) {

          BookDTO dto = convertBookToDTO(book.get());
          String pat = System.getProperty("user.dir") + "/src/main/resources/static/images/";
          Path path = Paths.get(pat + book.get().getImageUrl());

          if (Files.exists(path)) {
            dto.setImageUrl(book.get().getImageUrl());
          }

          model.addAttribute("subjects", subjectRepository.findAll());
          model.addAttribute("book", dto);
          return "update_book";
        } else {
          return "redirect:/";
        }
      } catch (Exception e) {
        return "redirect:/";
      }
    } else {
        return "redirect:/403";
    }
  }

  @CheckAdmin
  @GetMapping("/delete-book/{isbn}")
  public String deleteBook(@PathVariable("isbn") String isbn, RedirectAttributes redirect) {
    try {
      bookService.removeBookByIsbn(isbn);
      redirect.addFlashAttribute(
          "message", "Book with ISBN " + isbn + " was successfully deleted.");
      return "redirect:/table";
    } catch (Exception e) {
      redirect.addFlashAttribute("message", "Can not delete the book with isbn." + e.getMessage());
      return "redirect:/404";
    }
  }

  
  // private List<Book> getBookRelatedSubjects(Book book) {
  //   List<Book> books = new LinkedList<>();

  //   return books;
  // }

  @GetMapping("/book/{isbn}") //
  public String viewDetailBook(@PathVariable("isbn") String isbn, Model model, RedirectAttributes redirect) {
    Optional<Book> book = bookService.getBook(isbn);
    // th:if="${book}"
    if (book.isPresent()) {
      Optional<Author> author = bookAuthorService.findAuthorByBookIsbn(isbn);
      if (author.isPresent()) {
        model.addAttribute("author", author.get());
      } else {
        model.addAttribute("author", null);
      }
      model.addAttribute("book", book.get());
      if (book.get().getSubjects().size() > 1 || !book.get().getSubjects().isEmpty()) {
        Set<Subject> subjects = book.get().getSubjects();

        Set<Book> books = new HashSet<>();

        for (Subject subject : subjects) {
          List<Book> subjectRelatedBooks = bookService.getBookRelatedSubjects(subject);
          if (!subjectRelatedBooks.isEmpty()) {
            for (Book b : subjectRelatedBooks) {
              if (b.equals(book.get()) || books.contains(b)) {
                continue;
              }
              books.add(b);
            }
          }
        }

        model.addAttribute("books", books);
      }
      // getBookRelatedSubjects(book.get());

    } else {
      redirect.addFlashAttribute("message", "Can not found any book with that isbn: " + isbn);
      return "redirect:/404";
    }

    return "book_detail";
  }

}
