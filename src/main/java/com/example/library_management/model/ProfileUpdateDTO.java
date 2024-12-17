package com.example.library_management.model;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;

public class ProfileUpdateDTO {
  private String firstName;
  private String username;
  private String image;
  private String lastName;
  private Set<String> roles = new HashSet<>();
 
  public ProfileUpdateDTO(Account account) {
    this.username = account.getUsername();
    this.firstName = account.getFirstName();
    this.lastName = account.getLastName();
    this.image = account.getImage();
    this.roles = account.getRoles().stream().map(Enum::name).collect(Collectors.toSet());
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

}
