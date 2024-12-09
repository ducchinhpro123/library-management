package com.example.library_management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Librarian extends Account {
  @Id private int id;

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
