package com.example.library_management.controller;

import com.example.library_management.model.Author;
import com.example.library_management.service.AuthorService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthorController {
  private final AuthorService authorService;

  public AuthorController(AuthorService authorService) {
    this.authorService = authorService;
  }

  @PostMapping("/create-author")
  public ResponseEntity<String> createAuthor(@RequestBody Author newAuthor) {
    try {
      authorService.createAuthor(newAuthor);
      return new ResponseEntity<>("Author created successfully.", HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping("/delete-author")
  public ResponseEntity<String> deleteAuthor(@RequestParam(name = "id") Integer id) {
    try {
      authorService.deleteAuthor(id);
      return new ResponseEntity<>("Author deleted successfully.", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping("/edit-author")
  public ResponseEntity<String> updateAuthor(@RequestBody Author author) {
    try {
      Integer id = author.getId();
      authorService.updateAuthor(id, author);
      return new ResponseEntity<>("Author updated successfully.", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/get-author")
  public ResponseEntity<?> getAuthor(@RequestParam(name = "id") Integer id) {
    Optional<Author> author = authorService.getAuthor(id);
    if (author.isPresent()) return new ResponseEntity<>(author.get(), HttpStatus.OK);
    else {
      Map<String, String> message = new HashMap<>();
      message.put("message", "Can not found the author with the id: " + id.toString());
      return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
  }
}
