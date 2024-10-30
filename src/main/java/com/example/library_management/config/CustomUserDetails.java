package com.example.library_management.config;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import com.example.library_management.model.Account;
import com.example.library_management.model.Librarian;
import com.example.library_management.model.Member;
import com.example.library_management.repository.LibrarianRepository;
import com.example.library_management.repository.MemberRepository;

@Service
public class CustomUserDetails implements UserDetailsService {

  private final LibrarianRepository librarianRepository;
  private final MemberRepository memberRepository;

  public CustomUserDetails(LibrarianRepository librarianRepository, MemberRepository memberRepository) {
    this.librarianRepository = librarianRepository;
    this.memberRepository = memberRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Librarian> librarian = librarianRepository.getLibrarianByUsername(username);
    if (librarian.isPresent()) {
      return User.builder()
          .username(librarian.get().getUsername())
          .password(librarian.get().getPassword())
          .roles(getRoles(librarian.get()))
          .build();
    }

    Optional<Member> member = memberRepository.getMemberByUsername(username);
    if (member.isPresent()) {
      return User.builder()
          .username(member.get().getUsername())
          .password(member.get().getPassword())
          .roles(getRoles(member.get()))
          .build();
    }
   throw new UsernameNotFoundException("Username are given but not found");
  }

  private String[] getRoles(Account account) {
    if (account.getRole() == null) {
      return new String[] { "MEMBER" };
    }

    return account.getRole().split(" ");
  }

}
