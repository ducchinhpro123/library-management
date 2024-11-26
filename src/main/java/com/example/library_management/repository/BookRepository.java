package com.example.library_management.repository;

import com.example.library_management.model.Book;
import com.example.library_management.model.Subject;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, String> {

    @Query("SELECT b from Book b WHERE (:isbn IS NULL OR b.isbn LIKE %:isbn%) AND (:title IS NULL OR b.title LIKE %:title%) AND (:publisher IS NULL OR b.publisher LIKE %:publisher%)")
    List<Book> findBooksByISBNContainingAndTitleContainingAndPublisherContaining(@Param("isbn") String isbn,
            @Param("title") String title, 
            @Param("publisher") String publisher);

    List<Book> findByAuthorsName(@Param("authorName") String authorName);

    @Query("SELECT b from Book b where title = :title")
    List<Book> findByTitle(@Param("title") String title);

//    @Query("SELECT * FROM Book as b join Subject as sj on b.isbn = sj.book_isbn where b.isbn = '9780747532743';")

//    List<Book> findBooksRelatedToSubjects(@Param("subject") Subject subject);
    List<Book> findBooksBySubjectsContainingIgnoreCase(Subject subject);
    // Given the subject name, find all the books that related to the given book and has the same subject
}
