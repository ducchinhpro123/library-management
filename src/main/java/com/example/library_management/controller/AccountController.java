package com.example.library_management.controller;

import com.example.library_management.model.Account;
import com.example.library_management.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {

  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @GetMapping("/registration")
  public String registerAccount() {
    return "register";
  }

  @PostMapping("/registration")
  public String registerAccountHandle(Model model, Account account) {
    try {
      accountService.saveAccount(account);
      return "redirect:/login";
    } catch (Exception e) {
      model.addAttribute("message", e.getMessage());
      return "register";
    }
  }

  @GetMapping("/login")
  public String login(@RequestParam(value = "error", required = false) String error, Model model) {
    if (error != null) {
      model.addAttribute("errorMessage", "Invalid username or password.");
    }

    return "login";
  }
}
