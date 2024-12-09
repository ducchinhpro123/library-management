package com.example.library_management.service;

import com.example.library_management.dto.BookDTO;
import com.example.library_management.model.*;
import com.example.library_management.repository.AuthorRepository;
import com.example.library_management.repository.BookAuthorRepository;
import com.example.library_management.repository.BookRepository;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.LinkedList;
import java.util.HashSet;

import org.springframework.dao.EmptyResultDataAccessException;
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

  public List<Book> filterIsbnTitlePublisher(String isbn, String title, String publisher) {
    try {
      List<Book> books = bookRepository.findBooksByISBNContainingAndTitleContainingAndPublisherContaining(isbn, title, publisher);

      return books;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public List<Book> findBookByTitle(String kw) {
    try {
      List<Book> books = bookRepository.findByTitle(kw);
      return books;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    
  }

  private String saveImage(MultipartFile image) {
    String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
    try {
      String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/";
      Path uploadPath = Paths.get(uploadDir);
      if (!Files.exists(uploadPath)) {
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

  private boolean removeImage(String fileName) {
    try {
      String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/";
      Path pat = Paths.get(uploadDir + fileName);
      if (Files.isDirectory(pat)) {
        throw new RuntimeException("Cannot delete a directory: " + fileName);
      }
      if (Files.notExists(pat)) {
        return false;
      } else {
        Files.delete(pat);
        return true;
      }
    } catch (IOException e) {
      System.err.println("Failed to delete file: " + fileName + " - " + e.getMessage());
      return false;
    }
  }

  public Set<Subject> subjects(Book book) {
    Set<Subject> subjects = new HashSet<>();

    return subjects;
  }
  
  public Set<Book> getBookRelatedSubjects(Set<Subject> subjects, String id) {
      return bookRepository.findBooksBySubjectsInAndISBNNot(subjects, id);
      // return bookRepository.findBooksBySubjectsContainingIgnoreCase(subject);
  }


//  Cannot invoke "String.isEmpty()" because the return value of "com.example.library_management.model.Book.getImageUrl()" is null
//  java.nio.file.DirectoryNotEmptyException: /home/voducchinh/Github/library-management/src/main/resources/static/images

  @Transactional
  public void update(BookDTO dto) throws Exception {
    String isbn = dto.getISBN();
    if (!bookRepository.existsById(isbn)) {
      try {
        saveBookWithoutAuthor(dto);
      } catch (Exception e) {
        throw new Exception("There is an error while saving a book " + e.getMessage());
      }
    } else {
      Optional<Book> bookOp = bookRepository.findById(isbn);
      if (bookOp.isPresent()) {
        Book book = bookOp.get();
        if (!dto.getImage().isEmpty()) {
            // Remove the old image of book.getImageUrl()
            if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
                if (removeImage(book.getImageUrl())) {
                  System.out.println("Deleted image: " + book.getImageUrl());
                } else {
                    System.out.println("Failed to delete image: " + book.getImageUrl() + ". Will try to override it.");
                }
            }
          // Saving new image of dto.getImageUrl
          String newFileName = saveImage(dto.getImage());
          book.setImageUrl(newFileName);

        } 
        // else {
        //   String fileName = StringUtils.cleanPath(Objects.requireNonNull(dto.getImage().getOriginalFilename()));
        //   System.out.println(fileName);
        //   book.setImageUrl(fileName);
        // }
        book.setISBN(dto.getISBN());
        book.setTitle(dto.getTitle());
        for (Subject subject : dto.getSubjects()) {
          book.saveSubject(subject);
        }
        book.setLanguage(dto.getLanguage());
        book.setNumberOfPage(dto.getNumberOfPage());
        bookRepository.save(book);

      } else {
          throw new Exception("There is an error occur while trying to find the book in the database");
      }
    }
  }

  public void removeBookByIsbn(String isbn) {
    try {
      if (bookRepository.existsById(isbn)) {
        bookRepository.deleteById(isbn);
      } else {
        throw new RuntimeException("Book with ISBN " + isbn + " does not exist.");
      }
    } catch (EmptyResultDataAccessException e) {
      throw new RuntimeException("No book found with ISBN " + isbn, e);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @Transactional
  public void saveBookWithoutAuthor(BookDTO bookDTO) {
    try {
      Book book = new Book();
      book.setISBN(bookDTO.getISBN());
      book.setTitle(bookDTO.getTitle());
      book.setPublisher(bookDTO.getPublisher());
      book.setLanguage(bookDTO.getLanguage());
      book.setNumberOfPage(bookDTO.getNumberOfPage());
      book.setDescription(bookDTO.getDescription());

      for (Subject subject : bookDTO.getSubjects()) {
        book.saveSubject(subject);
      }

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
      //      book.getSubjects().clear();
      //      for (Subject subject : newBook.getSubjects()) {
      //        subject.setBook(book);
      //        book.getSubjects().add(subject);
      //      }

      //      book.setSubjects(newBook.getSubjects());
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
