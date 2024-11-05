package com.example.library_management.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class Account {
  private String username;
  private String password;
  private List<AccountRole> roles;

  public Account() {
    this.roles = new ArrayList<>();
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

  public List<AccountRole> getRoles() {
    return roles;
  }

  public void addRole(AccountRole role) {
    roles.add(role);
  }
}
