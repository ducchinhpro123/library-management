package com.example.library_management.service;

import com.example.library_management.model.Author;
import com.example.library_management.repository.BookAuthorRepository;
import com.example.library_management.repository.BookItemRepository;

import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class BookItemService {
  private final BookItemRepository bookItemRepository;

  public BookItemService(BookItemRepository bookItemRepository) {
    this.bookItemRepository = bookItemRepository;
  }
}
