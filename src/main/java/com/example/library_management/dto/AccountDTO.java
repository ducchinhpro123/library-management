package com.example.library_management.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

import com.example.library_management.model.AccountRole;
import com.example.library_management.model.AccountStatus;
import com.example.library_management.model.Admin;
import com.example.library_management.model.Librarian;
import com.example.library_management.model.Member;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class AccountDTO {
  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private String image;
  private MultipartFile imageFile;

  public Member getMember() {
    Member mem = new Member();
    mem.setUsername(this.username);
    mem.setFirstName(this.username);
    mem.setLastName(this.username);
    // mem.setUsername(this.username);
    // mem.setUsername(this.username);
    // mem.setUsername(this.username);

    return mem;
  }

  public AccountDTO(Librarian account) {
    this.username = account.getUsername();
    this.password = account.getPassword();
    this.firstName = account.getFirstName();
    this.lastName = account.getLastName();
    this.image = account.getImage();
  }

  public AccountDTO(Member account) {
    this.username = account.getUsername();
    this.password = account.getPassword();
    this.firstName = account.getFirstName();
    this.lastName = account.getLastName();
    this.image = account.getImage();
  }

  public AccountDTO(Admin account) {
    this.username = account.getUsername();
    this.password = account.getPassword();
    this.firstName = account.getFirstName();
    this.lastName = account.getLastName();
    this.image = account.getImage();
  }

  @Override
  public String toString() {
    return this.username + " " + this.firstName + " " + this.lastName;
  }

  private Set<AccountRole> roles = new HashSet<>();

  public AccountDTO() {
    this.roles = new HashSet<>();
  }

  private AccountStatus status;

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

  public MultipartFile getImageFile() {
    return imageFile;
  }
  
  public void setImageFile(MultipartFile imageFile) {
      this.imageFile = imageFile;
  }
}
