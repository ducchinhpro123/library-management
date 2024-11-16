package com.example.library_management.controller;

import com.example.library_management.model.Account;
import com.example.library_management.model.AccountRole;
import com.example.library_management.service.AccountService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {
    /*Any flag that starts with admin means only for admin */

  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @PostMapping("/admin/registration")
  public String registerForAdmin(@ModelAttribute Account account, Model model) throws Exception {
    try {
      accountService.saveAccountWithPermission(account);
      model.addAttribute("message", "Account created successfully.");
      return "register_admin";
    } catch(Exception e) {
      model.addAttribute("message", e.getMessage());
      return "register_admin";
    }
  }

  @GetMapping("/admin/registration")
  public String registerForAdmin(Model model) {
    List<AccountRole> roles = Arrays.asList(AccountRole.values());
    Account account = new Account();

    model.addAttribute("roles", roles);
    model.addAttribute("account", account);

    return "register_admin";
  }

  @GetMapping("/registration")
  public String registerAccount() {
    return "register";
  }

  // For normal user
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
