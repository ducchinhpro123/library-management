package com.example.library_management.model;

import jakarta.persistence.*;

@Entity
@Table(name = "book_subjects", schema = "library")
public class BookSubject {
    @EmbeddedId
    private BookSubjectId id;

    @MapsId("bookIsbn")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_isbn", nullable = false)
    private Book bookIsbn;

    @MapsId("subjectId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    public BookSubjectId getId() {
        return id;
    }

    public void setId(BookSubjectId id) {
        this.id = id;
    }

    public Book getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(Book bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

}