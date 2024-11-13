package com.example.library_management.service;

import com.example.library_management.dto.BookDTO;
import com.example.library_management.model.*;
import com.example.library_management.repository.AuthorRepository;
import com.example.library_management.repository.BookAuthorRepository;
import com.example.library_management.repository.BookRepository;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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
    return bookRepository.findAll();
  }

  private String saveImage(MultipartFile image) {
    String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
    try {
      String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/";
      Path uploadPath = Paths.get(uploadDir);
      if (Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
      }
      try (InputStream inputStream = image.getInputStream()) {
        Files.copy(
            inputStream, Paths.get(uploadDir + fileName), StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return fileName;
  }

  public void saveBookWithoutAuthor(BookDTO bookDTO) {
    try {
      Book book = new Book();
      book.setISBN(bookDTO.getISBN());
      book.setTitle(bookDTO.getTitle());
      book.setPublisher(bookDTO.getPublisher());
      book.setLanguage(bookDTO.getLanguage());
      book.setNumberOfPage(bookDTO.getNumberOfPage());
      book.setSubjects(bookDTO.getSubjects());

      try {
        String fileName = saveImage(bookDTO.getImage());
        book.setImageUrl(fileName);
      } catch (Exception e) {
        throw new RuntimeException(e.getMessage());
      }

      bookRepository.save(book);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
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
      //  ------- WARNING --------
      book.getSubjects().clear();
      for (Subject subject : newBook.getSubjects()) {
        subject.setBook(book);
        book.getSubjects().add(subject);
      }

      book.setSubjects(newBook.getSubjects());
      //   -----------------------

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
