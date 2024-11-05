package com.example.library_management.config;

import com.example.library_management.model.Account;
import com.example.library_management.model.AccountRole;
import com.example.library_management.model.Librarian;
import com.example.library_management.model.Member;
import com.example.library_management.repository.LibrarianRepository;
import com.example.library_management.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetails implements UserDetailsService {

  private final LibrarianRepository librarianRepository;
  private final MemberRepository memberRepository;

  public CustomUserDetails(
      LibrarianRepository librarianRepository, MemberRepository memberRepository) {
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
          .roles(getRoles(librarian.get()).toArray(new String[0]))
          .build();
    }

    Optional<Member> member = memberRepository.getMemberByUsername(username);
    if (member.isPresent()) {
      return User.builder()
          .username(member.get().getUsername())
          .password(member.get().getPassword())
          .roles(getRoles(member.get()).toArray(new String[0]))
          .build();
    }
    throw new UsernameNotFoundException("Username are given but not found");
  }

  private List<String> getRoles(Account account) {
    if (account.getRoles().isEmpty()) {
      List.of(AccountRole.MEMBER.name());
    }
    return account.getRoles().stream().map(AccountRole::name).collect(Collectors.toList());
  }
}
