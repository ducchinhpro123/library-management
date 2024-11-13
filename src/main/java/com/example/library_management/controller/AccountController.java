package com.example.library_management.controller;

import com.example.library_management.model.Account;
import com.example.library_management.model.AccountRole;
import com.example.library_management.model.AccountStatus;
import com.example.library_management.model.Member;
import com.example.library_management.repository.LibrarianRepository;
import com.example.library_management.repository.MemberRepository;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {

  private final PasswordEncoder passwordEncoder;
  private final LibrarianRepository librarianRepository;
  private final MemberRepository memberRepository;

  public AccountController(
      LibrarianRepository librarianRepository,
      PasswordEncoder passwordEncoder,
      MemberRepository memberRepository) {
    this.librarianRepository = librarianRepository;
    this.memberRepository = memberRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @GetMapping("/registration")
  public String registerAccount() {
    return "register";
  }

  @PostMapping("/registration")
  public String registerAccountHandle(Model model, Account account) {
    String username = account.getUsername();
    Optional<Member> memberOption = memberRepository.getMemberByUsername(username);

    if (memberOption.isEmpty()) {
      Member member = new Member();
      member.setUsername(account.getUsername());
      member.addRole(AccountRole.MEMBER);
      member.setPassword(passwordEncoder.encode(account.getPassword()));
      member.setStatus(AccountStatus.ACTIVE);

      memberRepository.save(member);

      return "redirect:/login";

    } else {
      model.addAttribute(
          "userNameAlreadyExist", "username already taken, please choose another username");
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
