package com.example.library_management.model;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Book {

  @Id private String ISBN;
  private String title;

  @ManyToMany
  @JoinTable(
      name = "book_subjects",
      joinColumns = @JoinColumn(name = "book_isbn"),
      inverseJoinColumns = @JoinColumn(name = "subject_id"))
  private Set<Subject> subjects = new LinkedHashSet<>();

  public void saveSubject(Subject subject) {
    this.subjects.add(subject);
    subject.getBooks().add(this);
  }

  public void removeSubject(Integer subjectId) {
    Subject subject = this.subjects.stream().filter(subj -> subj.getId() == subjectId).findFirst().orElse(null);
    if (subject != null) {
      this.subjects.remove(subject);
      subject.getBooks().remove(this);
    }
  }

  private String publisher;
  private String language;
  private String imageUrl;
  private int numberOfPage;

  @Lob
  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @OneToMany(mappedBy = "book")
  private Set<BookItem> bookItems;

  public Set<BookItem> getBookItems() {
    return bookItems;
  }

  public void setBookItems(Set<BookItem> bookItems) {
    this.bookItems = bookItems;
  }

  @ManyToMany
  @JoinTable(
      name = "book_author",
      joinColumns = @JoinColumn(name = "book_id"),
      inverseJoinColumns = @JoinColumn(name = "author_id"))
  private Set<Author> authors = new LinkedHashSet<>();

  public void saveAuthor(Author author) {
    this.authors.add(author);
    author.getBooks().add(this);
  }

  public void removeAuthor(Integer authorId) {
    Author author = this.authors.stream().filter(auth -> auth.getId() == authorId).findFirst().orElse(null);
    if (author != null) {
      this.authors.remove(author);
      author.getBooks().remove(this);
    }
  }

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

  // public Set<String> getSubjects() {
  //  return subjects;
  // }
  //
  // public void setSubejects(Set<String> subjects) {
  //  this.subjects = subjects;
  // }

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

  public Set<Subject> getSubjects() {
    return subjects;
  }

  public void setSubjects(Set<Subject> subjects) {
    this.subjects = subjects;
  }
}
