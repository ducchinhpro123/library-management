package com.example.library_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "subjects", schema = "library")
public class Subject {
  @Id
  @Column(name = "id", nullable = false)
  private Integer id;

  @Size(max = 200)
  @Column(name = "name", length = 200)
  private String name;

  @ManyToMany(mappedBy = "subjects")
  private Set<Book> books = new LinkedHashSet<>();

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

  public Set<Book> getBooks() {
    return books;
  }

  public void setBooks(Set<Book> books) {
    this.books = books;
  }
}
