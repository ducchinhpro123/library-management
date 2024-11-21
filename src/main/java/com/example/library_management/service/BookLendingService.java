package com.example.library_management.service;

import org.springframework.stereotype.Service;
import com.example.library_management.repository.BookLendingRepository;

@Service
public class BookLendingService {
  private final BookLendingRepository bookLendingRepository;

  public BookLendingService(BookLendingRepository bookLendingRepository) {
    this.bookLendingRepository = bookLendingRepository;
  }
}
