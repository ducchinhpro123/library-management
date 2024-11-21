package com.example.library_management.controller;

import com.example.library_management.annotation.CheckAdmin;
import com.example.library_management.dto.BookDTO;
import com.example.library_management.model.Author;
import com.example.library_management.model.Book;
import com.example.library_management.model.BookLending;

import com.example.library_management.repository.SubjectRepository;
import com.example.library_management.service.AuthorService;
import com.example.library_management.service.BookAuthorService;
import com.example.library_management.service.BookLendingService;
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

@Controller
public class BookLendingController {
  private final BookLendingService bookLendingService;

  public BookLendingController(BookLendingService bookLendingService) {
    this.bookLendingService = bookLendingService;
  }

  @GetMapping("/book/borrow")
  public String borrowBook() {

    return "";
  }
}
