package com.example.library_management.repository;

import com.example.library_management.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jdbc.repository.query.Query;
import com.example.library_management.model.Book;
import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
  // @Query("SELECT * FROM Subject WHERE")
  // public List<Book> findBooksBySubjectName(Subject subject);
}
