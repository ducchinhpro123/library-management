package com.example.library_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.library_management.model.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {
  @Query("SELECT u FROM Member u WHERE u.username = :username")
  public Optional<Member> getMemberByUsername(String username);
}
