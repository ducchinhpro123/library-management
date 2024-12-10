package com.example.library_management.config;

import com.example.library_management.model.AccountRole;
import com.example.library_management.model.Admin;
import com.example.library_management.model.Librarian;
import com.example.library_management.model.Member;
import com.example.library_management.model.AccountStatus;

import com.example.library_management.repository.AdminRepository;
import com.example.library_management.repository.LibrarianRepository;
import com.example.library_management.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.library_management.model.Account;

@Service
public class CustomUserDetails implements UserDetailsService {

  private final LibrarianRepository librarianRepository;
  private final MemberRepository memberRepository;
  private final AdminRepository adminRepository;

  public CustomUserDetails(
      LibrarianRepository librarianRepository, 
      AdminRepository adminRepository,
      MemberRepository memberRepository) {
    this.librarianRepository = librarianRepository;
    this.memberRepository = memberRepository;
    this.adminRepository = adminRepository;
  }

  private boolean isAccountPermit(AccountStatus accountStatus) {
    return !(accountStatus == AccountStatus.BLACKLISTED || accountStatus == AccountStatus.CANCELED
        ||accountStatus == AccountStatus.NONE || accountStatus == AccountStatus.CLOSED);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Librarian> librarian = librarianRepository.getLibrarianByUsername(username);
    if (librarian.isPresent()) {
      if (isAccountPermit(librarian.get().getStatus())) {
        return new CustomUserPrincipal(
            librarian.get().getUsername(), 
            librarian.get().getPassword(), 
            librarian.get().getImage(), 
            getRoles(librarian.get())
              .stream()
              .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
              .collect(Collectors.toList())
         );
      }
      throw new DisabledException("Account is not activated: " + librarian.get().getStatus());
    }

    Optional<Member> member = memberRepository.getMemberByUsername(username);
    if (member.isPresent()) {
      if (isAccountPermit(member.get().getStatus())) {
        return new CustomUserPrincipal(
            member.get().getUsername(), 
            member.get().getPassword(), 
            member.get().getImage(), 
            getRoles(member.get())
              .stream()
              .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
              .collect(Collectors.toList())
         );
      } else {
        throw new DisabledException("Account is not activated: " + member.get().getStatus());
      }
    }

    Optional<Admin> admin = adminRepository.getAdminByUsername(username);
    if (admin.isPresent()) {
      if (isAccountPermit(admin.get().getStatus())) {
        return new CustomUserPrincipal(
            admin.get().getUsername(), 
            admin.get().getPassword(), 
            admin.get().getImage(), 
            getRoles(admin.get())
              .stream()
              .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
              .collect(Collectors.toList())
         );

      }
      throw new DisabledException("Account is not activated: " + admin.get().getStatus());
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
