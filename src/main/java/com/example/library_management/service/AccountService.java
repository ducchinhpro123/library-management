package com.example.library_management.service;

import com.example.library_management.repository.LibrarianRepository;
import com.example.library_management.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private final MemberRepository memberRepository;
  private final LibrarianRepository librarianRepository;

  public AccountService(
      MemberRepository memberRepository, LibrarianRepository librarianRepository) {
    this.memberRepository = memberRepository;
    this.librarianRepository = librarianRepository;
  }
}
