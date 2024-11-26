package com.example.library_management;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
// import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.library_management.model.Book;
import com.example.library_management.model.BookItem;
import com.example.library_management.model.BookLending;
import com.example.library_management.model.BookStatus;
import com.example.library_management.model.Member;
import com.example.library_management.model.Subject;
import com.example.library_management.repository.BookItemRepository;
import com.example.library_management.repository.BookRepository;
import com.example.library_management.repository.MemberRepository;
import com.example.library_management.repository.SubjectRepository;
import com.example.library_management.service.AccountService;
import com.example.library_management.service.BookItemService;
// import com.example.library_management.repository.BookLendingRepository;
import com.example.library_management.service.BookLendingService;
import com.example.library_management.service.BookService;

// import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BookSubjectsRelatedTests {

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private SubjectRepository subjectRepository;

	@Mock
	private BookItemRepository bookItemRepository;
	@InjectMocks
	private BookItemService bookItemService;

	@Mock
	private BookRepository bookRepository;
	@InjectMocks
	private BookService bookService;

	@InjectMocks
	private AccountService accountService;

	@InjectMocks
	private BookLendingService bookLendingService;

	@Test
	public void testGetRelatedSubjectsFromABook() {

		Book book = new Book();
		book.setTitle("To Kill Mockingbird");
		book.setISBN("978-0547928227");

		Subject subject = new Subject();
		subject.setName("Science");

		Subject subject1 = new Subject();
		subject.setName("Cartoon");

		// public void saveSubject(Subject subject) {
		//   this.subjects.add(subject);
		//   subject.getBooks().add(this);
		// }

		// public void removeSubject(Integer subjectId) {
		//   Subject subject = this.subjects.stream().filter(subj -> subj.getId() == subjectId).findFirst().orElse(null);
		//   if (subject != null) {
		//     this.subjects.remove(subject);
		//     subject.getBooks().remove(this);
		//   }
		// }

		book.saveSubject(subject);
		book.saveSubject(subject1);

		// when(bookRepository.findById(book.getISBN())).thenReturn(Optional.of(book));
		// when(subjectRepository.findById(subject.getId())).thenReturn(Optional.of(subject));

		// Optional<Book> bookOpt = bookService.getBook(book.getISBN());
		// Optional<Subject> subjectOpt = subjectRepository.findById(subject.getId());

		// assertTrue(bookOpt.isPresent());
		// assertTrue(subjectOpt.isPresent());

		assertTrue(book.getSubjects().contains(subject));
		assertTrue(book.getSubjects().contains(subject1));

		assertEquals(2, book.getSubjects().size());

		assertTrue(subject.getBooks().contains(book));
		// assertTrue(subjectOpt.get())

	}

}
