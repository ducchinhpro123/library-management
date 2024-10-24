package com.example.library_management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Librarian extends Account {
  @Id private int id;

  @OneToMany(mappedBy = "librarian")
  private List<BookLending> bookLendings;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public boolean addBook() {
    return true;
  }

  public boolean blockMember(Member member) {
    return true;
  }

  public boolean unlockMember() {
    return true;
  }
}
