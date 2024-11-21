package com.example.library_management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Member extends Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private Date dateOfMemberShip;
  private int totalBooksCheckedOut;

  @OneToMany
  private List<BookLending> bookLendings = new ArrayList<>();

  public void saveBookLending(BookLending bookLending) {
    this.bookLendings.add(bookLending);
  }

  public void removeBookLending(Integer bookLendingId) {
    bookLendings.removeIf(b -> b.getId() == bookLendingId);
    // BookLending bookLending = this.bookLendings.stream().filter(b -> b.getId() == bookLendingId).findFirst().orElse(null);
    // if (bookLending != null) {
    //   this.bookLendings.remove(bookLending);
    // }
  }

  public List<BookLending> getBookLendings() {
    return bookLendings;
  }

  public void setBookLendings(List<BookLending> bookLendings) {
    this.bookLendings = bookLendings;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Date getDateOfMemberShip() {
    return dateOfMemberShip;
  }

  public void setDateOfMemberShip(Date dateOfMemberShip) {
    this.dateOfMemberShip = dateOfMemberShip;
  }

  public int getTotalBooksCheckedOut() {
    return totalBooksCheckedOut;
  }

  public void setTotalBooksCheckedOut(int totalBooksCheckedOut) {
    this.totalBooksCheckedOut = totalBooksCheckedOut;
  }
}
