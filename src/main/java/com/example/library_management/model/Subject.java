package com.example.library_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "subjects", schema = "library")
public class Subject {
  @Id
  @Column(name = "id", nullable = false)
  private Integer id;

  @Size(max = 200)
  @Column(name = "name", length = 200)
  private String name;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @ManyToOne
  @JoinColumn(name = "book_isbn", nullable = true)
  private Book book;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}

