package com.example.library_management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.Date;
import java.util.List;

@Entity
public class Member extends Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private Date dateOfMemberShip;
  private int totalBooksCheckedOut;

  @OneToMany(mappedBy = "member")
  private List<BookLending> bookLendings;

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
