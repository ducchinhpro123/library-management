package com.example.library_management.service;

import com.example.library_management.model.Account;
import com.example.library_management.model.AccountRole;
import com.example.library_management.model.AccountStatus;
import com.example.library_management.model.Member;
import com.example.library_management.repository.LibrarianRepository;
import com.example.library_management.repository.MemberRepository;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private final MemberRepository memberRepository;
  private final LibrarianRepository librarianRepository;
  private final PasswordEncoder passwordEncoder;

  public AccountService(
      PasswordEncoder passwordEncoder,
      MemberRepository memberRepository,
      LibrarianRepository librarianRepository) {
    this.memberRepository = memberRepository;
    this.librarianRepository = librarianRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public void saveAccount(Account account) throws Exception {
    String username = account.getUsername();
    Optional<Member> memberOption = memberRepository.getMemberByUsername(username);

    if (memberOption.isEmpty()) {
      Member member = new Member();
      member.setUsername(account.getUsername());
      member.addRole(AccountRole.MEMBER);
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
