package com.example.library_management.controller;

import com.example.library_management.model.Book;
import com.example.library_management.service.BookService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

  private final BookService bookService;

  public MainController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping("/")
  public String homePage(Model model) {
    List<Book> books = bookService.findAllBooks();
    model.addAttribute("books", books);
    model.addAttribute("activeHome", "active");
    return "index";
  }


  @GetMapping("/blank")
  public String blankPage() {
    return "blank";
  }

  @GetMapping("/404")
  public String notFoundPage() {
    return "404";
  }

  @GetMapping("/access-denied")
  public String acecssDenied() {
    return "403";
  }

}
