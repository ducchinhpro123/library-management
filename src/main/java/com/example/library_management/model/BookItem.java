package com.example.library_management.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class BookItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private boolean isReferenceOnly;
  private Date borrowed;
  private Date dueDate;
  private double price;
  private BookStatus status;
  @ManyToOne private Book book;

  @OneToMany(mappedBy = "bookItem")
  private List<BookLending> bookLendings = new ArrayList<>();


  public void setId(int id) {
    this.id = id;
  }
  
  public int getId() {
    return this.id;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }

  public boolean isReferenceOnly() {
    return isReferenceOnly;
  }

  public void setReferenceOnly(boolean isReferenceOnly) {
    this.isReferenceOnly = isReferenceOnly;
  }

  public Date getBorrowed() {
    return borrowed;
  }

  public void setBorrowed(Date borrowed) {
    this.borrowed = borrowed;
  }

  public Date getDueDate() {
    return dueDate;
  }

  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public BookStatus getStatus() {
    return status;
  }

  public void setStatus(BookStatus status) {
    this.status = status;
  }
}
