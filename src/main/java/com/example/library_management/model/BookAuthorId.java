package com.example.library_management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import org.hibernate.Hibernate;

@Embeddable
public class BookAuthorId implements java.io.Serializable {
  private static final long serialVersionUID = -4121595847516892210L;

  @Size(max = 255)
  @NotNull
  @Column(name = "book_id", nullable = false)
  private String bookId;

  public BookAuthorId(@Size(max = 255) @NotNull String bookId, @NotNull Integer authorId) {
    this.bookId = bookId;
    this.authorId = authorId;
  }

  @NotNull
  @Column(name = "author_id", nullable = false)
  private Integer authorId;

  public BookAuthorId() {}

  public String getBookId() {
    return bookId;
  }

  public void setBookId(String bookId) {
    this.bookId = bookId;
  }

  public Integer getAuthorId() {
    return authorId;
  }

  public void setAuthorId(Integer authorId) {
    this.authorId = authorId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    BookAuthorId entity = (BookAuthorId) o;
    return Objects.equals(this.authorId, entity.authorId)
        && Objects.equals(this.bookId, entity.bookId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authorId, bookId);
  }
}
