package com.example.library_management.service;

import com.example.library_management.model.Author;
import com.example.library_management.repository.AuthorRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
  private final AuthorRepository authorRepository;

  public AuthorService(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  public void createAuthor(Author newAuthor) throws IllegalArgumentException {
    if (newAuthor == null) return;

    try {
      authorRepository.save(newAuthor);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public void deleteAuthor(Integer id) throws IllegalArgumentException {
    try {
      authorRepository.deleteById(id);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public void updateAuthor(Integer id, Author newAuthor) throws IllegalArgumentException {
    Optional<Author> bookOptional = authorRepository.findById(id);
    if (bookOptional.isPresent()) {
      Author author = bookOptional.get();
      author.setName(newAuthor.getName());
      author.setDescription(newAuthor.getDescription());
      authorRepository.save(author);
    } else {
      throw new RuntimeException("Can not update author");
    }
  }

  public Author getAuthor(String authorName) {
    Author author = authorRepository.findByName(authorName);
    if (author == null) {
      return null;
    }
    return author;
  }

  public Optional<Author> getAuthor(Integer id) {
    Optional<Author> author = authorRepository.findById(id);
    if (author.isEmpty()) {
      return Optional.empty();
    }
    return author;
  }
}
