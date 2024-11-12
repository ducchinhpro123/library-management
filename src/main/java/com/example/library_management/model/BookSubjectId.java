package com.example.library_management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.Hibernate;

import java.util.Objects;

@Embeddable
public class BookSubjectId implements java.io.Serializable {
    private static final long serialVersionUID = -2949929869388281960L;
    @Size(max = 255)
    @NotNull
    @Column(name = "book_isbn", nullable = false)
    private String bookIsbn;

    @NotNull
    @Column(name = "subject_id", nullable = false)
    private Integer subjectId;

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BookSubjectId entity = (BookSubjectId) o;
        return Objects.equals(this.bookIsbn, entity.bookIsbn) &&
                Objects.equals(this.subjectId, entity.subjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookIsbn, subjectId);
    }

}