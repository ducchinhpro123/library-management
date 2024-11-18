package com.example.library_management.controller;

import com.example.library_management.model.Account;
import com.example.library_management.model.AccountRole;
import com.example.library_management.model.Admin;
import com.example.library_management.model.Librarian;
import com.example.library_management.model.Member;
import com.example.library_management.service.AccountService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AccountController {
    /*Any flag that starts with admin means only for admin */

  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @PostMapping("/update-profile")
  public String updateProfile(@ModelAttribute Account account) {
    // TODO: implement this
    return "redirect:/profile";
  }

  @GetMapping("/profile")
  public String profilePage(Model model, RedirectAttributes redi) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    List<String> roles = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

    // boolean isAdmin = auth.getAuthorities().stream()
    //         .anyMatch(granted -> granted.getAuthority().contains("ROLE_ADMIN"));

    UserDetails user = (UserDetails) auth.getPrincipal();

    /* NOTE: The order of these if statements is important */
    if (roles.contains("ROLE_ADMIN")) {
      Optional<Admin> admin = accountService.findAdminByUsername(user.getUsername());
      if (admin.isPresent()) {
        model.addAttribute("account", admin.get());
        return "profile";
      } else if (admin.isEmpty()) {
        redi.addFlashAttribute("message", "Account not found");
        return "redirect:/404";
      }
    }

    if (roles.contains("ROLE_LIBRARIAN")) {
      Optional<Librarian> lib = accountService.findLibrarianByUsername(user.getUsername());
      if (lib.isPresent()) {
        model.addAttribute("account", lib.get());
        return "profile";
      } else if (lib.isEmpty()) {
        redi.addFlashAttribute("message", "Account not found");
        return "redirect:/404";
      }
    }

    if (roles.contains("ROLE_MEMBER")) {
      Optional<Member> member = accountService.findMemberByUsername(user.getUsername());
      if (member.isPresent()) {
        model.addAttribute("account", member.get());
        return "profile";
      } else if (member.isEmpty()) {
        redi.addFlashAttribute("message", "Account not found");
        return "redirect:/404";
      }
    }

    redi.addFlashAttribute("message", "There is an error while trying to get user's authorities");
    return "redirect:/404";
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
