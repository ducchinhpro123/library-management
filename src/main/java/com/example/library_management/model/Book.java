package com.example.library_management.model;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Book {
  @Id private String ISBN;

  private String title;

  @OneToMany(mappedBy="book")
  private Set<Subject> subjects = new LinkedHashSet<>();

  private String publisher;
  private String language;
  private String imageUrl;
  private int numberOfPage;

  @OneToMany(mappedBy = "book")
  private List<BookItem> bookItems;

  public List<BookItem> getBookItems() {
    return bookItems;
  }

  public void setBookItems(List<BookItem> bookItems) {
    this.bookItems = bookItems;
  }

  @ManyToMany
  @JoinTable(
      name = "book_author",
      joinColumns = @JoinColumn(name = "book_id"),
      inverseJoinColumns = @JoinColumn(name = "author_id"))
  private Set<Author> authors = new LinkedHashSet<>();

  public Set<Author> getAuthors() {
    return authors;
  }

  public void setAuthors(Set<Author> authors) {
    this.authors = authors;
  }

  public String getISBN() {
    return ISBN;
  }

  public void setISBN(String iSBN) {
    ISBN = iSBN;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubeject(String subject) {
    this.subject = subject;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public int getNumberOfPage() {
    return numberOfPage;
  }

  public void setNumberOfPage(int numberOfPage) {
    this.numberOfPage = numberOfPage;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}
