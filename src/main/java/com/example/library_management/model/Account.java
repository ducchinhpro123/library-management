package com.example.library_management.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@MappedSuperclass
public class Account {
  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
  @Pattern(regexp = "^[a-zA-Z0-9._-]{3,}$", message = "Username can only contains letters, numbers, dots, underscores and hyphens")
  private String username;

  @NotBlank(message = "Password is required")
  @Size(min = 5, max = 100, message = "Password must be between 5 and 100 characters")
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", 
    message = "Password must contain at least one digit, one lowercase, one uppercase, and one special character")
  private String password;

  @Transient // Indicate that this field does not persistent in the database
  private String passwordConfirm;

  @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
  @Pattern(regexp = "^[a-zA-Z\\s-']+$", message = "First name can only contain letters, spaces, hyphens and apostrophes")
  private String firstName;

  @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
  @Pattern(regexp = "^[a-zA-Z\\s-']+$", message = "Last name can only contain letters, spaces, hyphens and apostrophes")
  private String lastName;

  private String image;

  @OneToMany
  private List<BookLending> bookLendings;

  @Override
  public String toString() {
    return this.username + " " + this.firstName + " " + this.lastName;
  }

  private Set<AccountRole> roles;

  public Account() {
    this.roles = new HashSet<>();
  }

  private AccountStatus status;

  public String getPasswordConfirm() {
    return passwordConfirm;
  }

  public void setPasswordConfirm(String passwordConfirm) {
    this.passwordConfirm = passwordConfirm;
  }


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public AccountStatus getStatus() {
    return status;
  }

  public void setStatus(AccountStatus status) {
    this.status = status;
  }

  public Set<AccountRole> getRoles() {
    return roles;
  }

  public void setRoles(Set<AccountRole> roles) {
    this.roles = roles;
  }

  public void addRole(AccountRole role) {
    roles.add(role);
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
  
  public String getImage() {
    return image;
  }
  
  public void setImage(String image) {
    this.image = image;
  }

  public String getRolesString() {
    return roles.stream().map(AccountRole::name).collect(Collectors.joining(","));
  }

    public List<BookLending> getBookLendings() {
        return bookLendings;
    }

    public void setBookLendings(List<BookLending> bookLendings) {
        this.bookLendings = bookLendings;
    }
}
