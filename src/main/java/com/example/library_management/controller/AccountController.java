package com.example.library_management.controller;

import com.example.library_management.dto.AccountDTO;
import com.example.library_management.model.Account;
import com.example.library_management.model.AccountRole;
import com.example.library_management.model.Admin;
import com.example.library_management.model.Librarian;
import com.example.library_management.model.Member;
import com.example.library_management.service.AccountService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.security.auth.login.AccountNotFoundException;

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

  @PostMapping("/update-avatar")
  public String changeImageAvatar(@ModelAttribute AccountDTO account, RedirectAttributes re) throws Exception {

    // public boolean updateAvatar(AccountDTO account, AccountRole role) {
    Set<AccountRole> roles = account.getRoles();
    AccountRole role = null;
    if (roles.contains(AccountRole.ADMIN)) {
      role = AccountRole.ADMIN;
    } else if (roles.contains(AccountRole.LIBRARIAN)) {
      role = AccountRole.LIBRARIAN;
    } else if (roles.contains(AccountRole.MEMBER)) {
      role = AccountRole.MEMBER;
    }

    try {
      assert role != null;
      accountService.updateAvatar(account, role);
    } catch(AccountNotFoundException e) {
      re.addFlashAttribute("message", e);
      return "redirect:/404";
    } catch(Exception e) {
      re.addFlashAttribute("message", e);
      return "redirect:/404";
    }
    re.addFlashAttribute("message", "Updated image successfully.");
    return "redirect:/profile";
  }

  @PostMapping("/update-profile")
  public String updateProfile(@ModelAttribute Account account, RedirectAttributes re) throws Exception {
    Set<AccountRole> roles = account.getRoles();

    try {
      if (roles.contains(AccountRole.MEMBER)) {
        if (accountService.updateAccountMember(account)) {
          re.addFlashAttribute("message", "Updated successfully");
        }  else {
          re.addFlashAttribute("message", "Failed to update");
        }
      }

      if (roles.contains(AccountRole.LIBRARIAN)) { 
        if (accountService.updateaccountLibrarian(account)) {
          re.addFlashAttribute("message", "Updated successfully");
        } else {
          re.addFlashAttribute("message", "Failed to update");
        }
      }

      if (roles.contains(AccountRole.ADMIN)) { 
        if (accountService.updateAccountAdmin(account)) {
          re.addFlashAttribute("message", "Updated successfully");
        } else {
          re.addFlashAttribute("message", "Failed to update");
        }
      }

      re.addFlashAttribute("message", "Your account has expired. Please contact to admin of this system to re-active it.");

    } catch (AccountNotFoundException e) {
      re.addFlashAttribute("message", "Account not found");
    } catch (Exception e) {
      System.err.println(e);
      re.addFlashAttribute("message", "There is an error while trying to update Account");
    }

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
        AccountDTO dto = new AccountDTO(admin.get());
        dto.setRoles(admin.get().getRoles());
        // dto.addRole(AccountRole.ADMIN);

        model.addAttribute("account", admin.get());
        model.addAttribute("dto", dto);

        return "profile";
      } else if (admin.isEmpty()) {
        redi.addFlashAttribute("message", "Account not found");
        return "redirect:/404";
      }
    }

    if (roles.contains("ROLE_LIBRARIAN")) {
      Optional<Librarian> lib = accountService.findLibrarianByUsername(user.getUsername());
      if (lib.isPresent()) {
        AccountDTO dto = new AccountDTO(lib.get());
        dto.setRoles(lib.get().getRoles());

        model.addAttribute("account", lib.get());
        model.addAttribute("dto", dto);

        return "profile";
      } else if (lib.isEmpty()) {
        redi.addFlashAttribute("message", "Account not found");
        return "redirect:/404";
      }
    }

    if (roles.contains("ROLE_MEMBER")) {
      Optional<Member> member = accountService.findMemberByUsername(user.getUsername());
      if (member.isPresent()) {
        AccountDTO dto = new AccountDTO(member.get());
        dto.setRoles(member.get().getRoles());

        model.addAttribute("account", member.get());
        model.addAttribute("dto", dto);

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
  public String registerForAdmin(@ModelAttribute Account account, RedirectAttributes re) throws Exception {
    List<AccountRole> roles = Arrays.asList(AccountRole.values());
    try {
      accountService.saveAccountWithPermission(account);
      re.addFlashAttribute("message", "Account created successfully.");
      re.addFlashAttribute("roles", roles);
      return "register_admin";
    } catch(Exception e) {
      re.addFlashAttribute("message", e.getMessage());
      return "redirect:/admin/registration";
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
