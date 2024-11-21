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
import com.example.library_management.model.Member;
import com.example.library_management.repository.BookRepository;
import com.example.library_management.service.AccountService;
import com.example.library_management.service.BookItemService;
// import com.example.library_management.repository.BookLendingRepository;
import com.example.library_management.service.BookLendingService;
import com.example.library_management.service.BookService;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BookLendingTests {

	@Mock
	private BookRepository bookRepository;

	@InjectMocks
	private AccountService accountService;

	@InjectMocks
	private BookLendingService bookLendingService;

	@InjectMocks
	private BookItemService bookItemService;

	@InjectMocks
	private BookService bookService;

	@Test
	public void testMemberBorrowingBook() {
		Book book = new Book();
		book.setISBN("978-0547928227");

		when(bookRepository.findById(book.getISBN())).thenReturn(Optional.of(book));

		Optional<Book> bookOpt = bookService.getBook("978-0547928227");
		assertTrue(bookOpt.isPresent());

		BookLending bookLending = new BookLending();
		Member member = new Member();
		BookItem bookItem = new BookItem();

		bookLending.setMember(member);
		bookLending.setBookItem(bookItem);

		bookItem.setBook(book);
		member.saveBookLending(bookLending);
		// member.getBookLendings().add(bookLending);

		assertEquals(book, bookItem.getBook());
		assertEquals(member, bookLending.getMember());
		assertTrue(member.getBookLendings().contains(bookLending));

		member.removeBookLending(bookLending.getId());
		assertFalse(member.getBookLendings().contains(bookLending));
	}

//	@Test
//	public void happyFlow() {
//		Optional<Member> member = accountService.findMemberByUsername("chinh");
//		assertNotEquals(member, null);
//
//	}
}
