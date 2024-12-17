package com.example.library_management.service;

import com.example.library_management.dto.AccountDTO;
import com.example.library_management.model.*;
import com.example.library_management.repository.AdminRepository;
import com.example.library_management.repository.LibrarianRepository;
import com.example.library_management.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.util.StringUtils;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

  public AccountWithDTO getAccountByUsernameAndRole(String username, String primaryRole) {
    return switch (primaryRole) {
      case "ROLE_ADMIN" -> getAdminAccount(username);
      case "ROLE_LIBRARIAN" -> getLibrarianAccount(username);
      case "ROLE_MEMBER" -> getMemberAccount(username);
      default -> throw new IllegalStateException("Invalid role: " + primaryRole);
    };
  }

  private AccountWithDTO getLibrarianAccount(String username) {
    Librarian librarian = librarianRepository.getLibrarianByUsername(username)
      .orElseThrow(() -> new EntityNotFoundException("Librarian not found"));

    AccountDTO dto = new AccountDTO(librarian);
    dto.setRoles(librarian.getRoles());

    return new AccountWithDTO(librarian, dto);
  }

  private AccountWithDTO getMemberAccount(String username) {
    Member member = memberRepository.getMemberByUsername(username)
      .orElseThrow(() -> new EntityNotFoundException("Member not found"));

    AccountDTO dto = new AccountDTO(member);
    dto.setRoles(member.getRoles());

    return new AccountWithDTO(member, dto);
  }

  private AccountWithDTO getAdminAccount(String username) {
    Admin admin = adminRepository.getAdminByUsername(username)
      .orElseThrow(() -> new EntityNotFoundException("Admin not found"));

    AccountDTO accountDTO = new AccountDTO(admin);
    AccountWithDTO accountWithDTO = new AccountWithDTO(admin, accountDTO);
    return accountWithDTO;
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

  public Optional<Librarian> findLibrarianByUsername(String username) {
    Optional<Librarian> librarian = librarianRepository.getLibrarianByUsername(username);
    if (librarian.isPresent()) {
      return librarian;
    }
    return Optional.empty();
  }

  public Optional<Member> findMemberByUsername(String username) {
    Optional<Member> member = memberRepository.getMemberByUsername(username);
    if (member.isPresent()) {
      return member;
    }
    return Optional.empty();
  }


  public Optional<Admin> findAdminByUsername(String username) {
    Optional<Admin> admin = adminRepository.getAdminByUsername(username);
    if (admin.isPresent()) {
      return admin;
    }
    return Optional.empty();
  }

  private String saveImage(MultipartFile image) {
    String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
    try {
      String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/avatar/";
      Path uploadPath = Paths.get(uploadDir);

      if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
      }
      try (InputStream inputStream = image.getInputStream()) {
        Files.copy(
            inputStream, Paths.get(uploadDir + fileName), StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return fileName;
  }

  private void removeImage(String fileName) {
    try {
      String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/avatar/";
      Path pat = Paths.get(uploadDir + fileName);
      if (Files.isDirectory(pat)) {
        throw new RuntimeException("Cannot delete a directory: " + fileName);
      }
      if (Files.notExists(pat)) {
        throw new RuntimeException("Cannot delete a file that didn't exists: " + fileName);
      } else {
        Files.delete(pat);
      }
    } catch (IOException e) {
      System.err.println("Failed to delete file: " + fileName + " - " + e.getMessage());
    }
  }

  public boolean updateAvatar(AccountDTO account, AccountRole role) throws Exception {
    switch (role) {
      case ADMIN:  {
                     Admin admin = adminRepository.getAdminByUsername(account.getUsername())
                       .orElseThrow(() -> new AccountNotFoundException("Account not found"));
                     try {

                       if (admin.getImage() != account.getImage()) removeImage(admin.getImage());

                       String newFileName = saveImage(account.getImageFile());
                       admin.setImage(newFileName);
                       adminRepository.save(admin);
                       return true;
                     } catch (Exception e) {
                       throw new Exception(e);
                     }
      }
      case LIBRARIAN: {
                     Librarian lib  = librarianRepository.getLibrarianByUsername(account.getUsername())
                       .orElseThrow(() -> new AccountNotFoundException("Account not found"));
                     try {
                       String newFileName = saveImage(account.getImageFile());
                       lib.setImage(newFileName);
                       return true;
                     } catch (Exception e) {
                       throw new Exception(e);
                     }
      }
      case MEMBER: {
                     Member mem  = memberRepository.getMemberByUsername(account.getUsername())
                       .orElseThrow(() -> new AccountNotFoundException("Account not found"));
                     try {
                       String newFileName = saveImage(account.getImageFile());
                       mem.setImage(newFileName);
                       return true;
                     } catch (Exception e) {
                       throw new Exception(e);
                     }

      }
      default: throw new AccountNotFoundException("Account not found");
    }
  }

  public boolean updateaccountLibrarian(Account account) throws Exception {
    String username = account.getUsername();
    Librarian lib = librarianRepository.getLibrarianByUsername(username)
      .orElseThrow(() -> new AccountNotFoundException("Account not found"));

    try {
      lib.setFirstName(account.getFirstName());
      lib.setLastName(account.getLastName());
      librarianRepository.save(lib);
      return true;
    } catch(Exception e) {
      throw new RuntimeException(e);
    }
  }

  public boolean updateAccountMember(Account account) throws Exception {
    String username = account.getUsername();
    Member member = memberRepository.getMemberByUsername(username)
      .orElseThrow(() -> new AccountNotFoundException("Account not found"));

    try {
      member.setFirstName(account.getFirstName());
      member.setLastName(account.getLastName());
      memberRepository.save(member);
      return true;
    } catch(Exception e) {
      throw new RuntimeException(e);
    }
  }

  public boolean updateAccountAdmin(Account account) throws Exception {
    String username = account.getUsername();
    Admin admin = adminRepository.getAdminByUsername(username)
      .orElseThrow(() -> new AccountNotFoundException("Account not found"));

    try {
      admin.setFirstName(account.getFirstName());
      admin.setLastName(account.getLastName());
      adminRepository.save(admin);
      return true;
    } catch(Exception e) {
      throw new RuntimeException(e);
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
