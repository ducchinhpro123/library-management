package com.example.library_management.service;

import com.example.library_management.model.*;
import com.example.library_management.repository.AdminRepository;
import com.example.library_management.repository.LibrarianRepository;
import com.example.library_management.repository.MemberRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private final MemberRepository memberRepository;
  private final LibrarianRepository librarianRepository;
  private final AdminRepository adminRepository;
  private final PasswordEncoder passwordEncoder;

  public AccountService(
      PasswordEncoder passwordEncoder,
      MemberRepository memberRepository,
      AdminRepository adminRepository,
      LibrarianRepository librarianRepository) {
    this.memberRepository = memberRepository;
    this.passwordEncoder = passwordEncoder;
    this.adminRepository = adminRepository;
    this.librarianRepository = librarianRepository;
  }

  public void saveLibrarian(Account account) throws Exception {
    String username = account.getUsername();
    Optional<Librarian> librarianOpt = librarianRepository.getLibrarianByUsername(username);

    if (librarianOpt.isEmpty()) {
      Librarian lib = new Librarian();

      lib.setUsername(account.getUsername());
      lib.addRole(AccountRole.LIBRARIAN); // Default
      lib.setPassword(passwordEncoder.encode(account.getPassword()));
      lib.setStatus(AccountStatus.ACTIVE);

      try {
        librarianRepository.save(lib);
      } catch (Exception e) {
        throw new Exception(e.getMessage());
      }
    } else {
      throw new Exception("Account with username " + username + " already exists.");
    }
  }

  public void saveAdmin(Account account) throws Exception {
    String username = account.getUsername();
    Optional<Admin> adminOpt = adminRepository.getAdminByUsername(username);

    if (adminOpt.isEmpty()) {
      Admin admin = new Admin();

      admin.setUsername(account.getUsername());
      admin.addRole(AccountRole.ADMIN); // Default
      admin.setPassword(passwordEncoder.encode(account.getPassword()));
      admin.setStatus(AccountStatus.ACTIVE);

      try {
        adminRepository.save(admin);
      } catch (Exception e) {
        throw new Exception(e.getMessage());
      }
    } else {
      throw new Exception("Account with username " + username + " already exists.");
    }
  }

  public void saveAccountWithPermission(Account account) throws Exception {
    Set<AccountRole> roles = account.getRoles();
    if (roles.contains(AccountRole.ADMIN)) {
      saveAdmin(account);
    } else if (roles.contains(AccountRole.MEMBER)) {
      saveAccount(account);
    } else if (roles.contains(AccountRole.LIBRARIAN)){
      saveLibrarian(account);
    } else {
        throw new Exception("Invalid role.");
    }
  }

  public void saveAccount(Account account) throws Exception {
    String username = account.getUsername();
    Optional<Member> memberOption = memberRepository.getMemberByUsername(username);

    if (memberOption.isEmpty()) {
      Member member = new Member();
      member.setUsername(account.getUsername());
      member.addRole(AccountRole.MEMBER); // Default
      member.setPassword(passwordEncoder.encode(account.getPassword()));
      member.setStatus(AccountStatus.ACTIVE);

      try {
        memberRepository.save(member);
      } catch (Exception e) {
        throw new Exception(e.getMessage());
      }
    } else {
      throw new Exception("Account with username " + username + " already exists.");
    }
  }
}
