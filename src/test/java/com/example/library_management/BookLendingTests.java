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
import com.example.library_management.repository.BookItemRepository;
import com.example.library_management.repository.BookRepository;
import com.example.library_management.repository.MemberRepository;
import com.example.library_management.service.AccountService;
import com.example.library_management.service.BookItemService;
// import com.example.library_management.repository.BookLendingRepository;
import com.example.library_management.service.BookLendingService;
import com.example.library_management.service.BookService;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BookLendingTests {

	@Mock
	private MemberRepository memberRepository;

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
	public void testMemberBorrowMultipleBooks() {
		Member member = new Member();
		member.setId(1);

		Book book = new Book();
		book.setTitle("To Kill Mockingbird");
		book.setISBN("978-0547928227");

		Book book2 = new Book();
		book.setTitle("Think Big");
		book.setISBN("978-0547928220");

		BookItem bookItem2 = new BookItem();
		bookItem2.setId(2);
		bookItem2.setReferenceOnly(false);
		bookItem2.setBook(book);
		bookItem2.setStatus(BookStatus.LOANED);

		BookItem bookItem = new BookItem();
		bookItem.setId(1);
		bookItem.setReferenceOnly(false);
		bookItem.setBook(book);
		bookItem.setStatus(BookStatus.LOANED);

		when(bookRepository.findById(book.getISBN())).thenReturn(Optional.of(book));
		when(bookItemRepository.findById(bookItem.getId())).thenReturn(Optional.of(bookItem));
		when(bookItemRepository.findById(bookItem2.getId())).thenReturn(Optional.of(bookItem2));
		when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

		Optional<Book> bookOpt = bookService.getBook(book.getISBN());
		Optional<Book> bookOpt2 = bookService.getBook(book2.getISBN());

		Optional<BookItem> bookItemOpt = bookItemRepository.findById(bookItem.getId());
		Optional<BookItem> bookItemOpt2 = bookItemRepository.findById(bookItem2.getId());
		Optional<Member> memberOpt = memberRepository.findById(member.getId());

		BookLending bookLending = new BookLending();
		bookLending.setMember(memberOpt.get());
		bookLending.setBookItem(bookItemOpt.get());

		BookLending bookLending2 = new BookLending();
		bookLending2.setMember(memberOpt.get());
		bookLending2.setBookItem(bookItemOpt2.get());

		assertTrue(bookOpt.isPresent());
		assertTrue(bookItemOpt.isPresent());
		assertFalse(bookItemOpt.get().isReferenceOnly());
		assertTrue(memberOpt.isPresent());

		assertNotNull(bookLending.getMember());
		assertNotNull(bookLending.getBookItem());

		book.saveBookItem(bookItemOpt.get());
		assertNotNull(book.getBookItems());

		assertTrue(book.getBookItems().contains(bookItemOpt.get()));

		member.saveBookLending(bookLending);
		member.saveBookLending(bookLending2);

		assertNotNull(member.getBookLendings());
		assertTrue(member.getBookLendings().size() == 2);

		assertTrue(member.getBookLendings().contains(bookLending));
		assertTrue(member.getBookLendings().contains(bookLending2));

		assertTrue(bookItem.getStatus() == BookStatus.LOANED);
		assertTrue(bookItem2.getStatus() == BookStatus.LOANED);
	}

	@Test
	public void testMemberReturnABook() {
		Member member = new Member();
		member.setId(1);

		Book book = new Book();
		book.setTitle("To Kill Mockingbird");
		book.setISBN("978-0547928227");

		BookItem bookItem = new BookItem();
		bookItem.setId(1);
		bookItem.setReferenceOnly(false);
		bookItem.setBook(book);

		when(bookRepository.findById(book.getISBN())).thenReturn(Optional.of(book));
		when(bookItemRepository.findById(bookItem.getId())).thenReturn(Optional.of(bookItem));
		when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

		Optional<Book> bookOpt = bookService.getBook("978-0547928227");
		Optional<BookItem> bookItemOpt = bookItemRepository.findById(bookItem.getId());
		Optional<Member> memberOpt = memberRepository.findById(member.getId());

		BookLending bookLending = new BookLending();
		bookLending.setMember(memberOpt.get());
		bookLending.setBookItem(bookItemOpt.get());

		assertTrue(bookOpt.isPresent());
		assertTrue(bookItemOpt.isPresent());
		assertFalse(bookItemOpt.get().isReferenceOnly());
		assertTrue(memberOpt.isPresent());

		assertNotNull(bookLending.getMember());
		assertNotNull(bookLending.getBookItem());

		book.saveBookItem(bookItemOpt.get());
		assertNotNull(book.getBookItems());

		assertTrue(book.getBookItems().contains(bookItemOpt.get()));

		member.saveBookLending(bookLending);

		assertNotNull(member.getBookLendings());
		assertTrue(member.getBookLendings().contains(bookLending));

		member.removeBookLending(bookLending.getId());
		assertTrue(!member.getBookLendings().contains(bookLending));
	}

	@Test
	public void testMemberBorrowABook() {
		Member member = new Member();
		member.setId(1);

		Book book = new Book();
		book.setTitle("To Kill Mockingbird");
		book.setISBN("978-0547928227");

		BookItem bookItem = new BookItem();
		bookItem.setId(1);
		bookItem.setReferenceOnly(false);
		bookItem.setBook(book);

		when(bookRepository.findById(book.getISBN())).thenReturn(Optional.of(book));
		when(bookItemRepository.findById(bookItem.getId())).thenReturn(Optional.of(bookItem));
		when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

		Optional<Book> bookOpt = bookService.getBook("978-0547928227");
		Optional<BookItem> bookItemOpt = bookItemRepository.findById(bookItem.getId());
		Optional<Member> memberOpt = memberRepository.findById(member.getId());

		BookLending bookLending = new BookLending();
		bookLending.setMember(memberOpt.get());
		bookLending.setBookItem(bookItemOpt.get());

		assertTrue(bookOpt.isPresent());
		assertTrue(bookItemOpt.isPresent());
		assertFalse(bookItemOpt.get().isReferenceOnly());
		assertTrue(memberOpt.isPresent());

		assertNotNull(bookLending.getMember());
		assertNotNull(bookLending.getBookItem());

		book.saveBookItem(bookItemOpt.get());
		assertNotNull(book.getBookItems());

		assertTrue(book.getBookItems().contains(bookItemOpt.get()));

		member.saveBookLending(bookLending);

		assertNotNull(member.getBookLendings());
		assertTrue(member.getBookLendings().contains(bookLending));
	}

	@Test
	public void testBookIsReferOnlyAndCantBorrow() {
		Member member = new Member();
		member.setId(1);

		Book book = new Book();
		book.setISBN("978-0547928227");

		BookItem bookItem = new BookItem();
		bookItem.setId(1);
		bookItem.setReferenceOnly(true);
		bookItem.setBook(book);


		when(bookRepository.findById(book.getISBN())).thenReturn(Optional.of(book));
		when(bookItemRepository.findById(bookItem.getId())).thenReturn(Optional.of(bookItem));
		when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

		Optional<Book> bookOpt = bookService.getBook("978-0547928227");
		Optional<BookItem> bookItemOpt = bookItemRepository.findById(bookItem.getId());
		Optional<Member> memberOpt = memberRepository.findById(member.getId());

		BookLending bookLending = new BookLending();
		bookLending.setMember(memberOpt.get());
		bookLending.setBookItem(bookItemOpt.get());

		assertTrue(bookOpt.isPresent());
		assertTrue(bookItemOpt.isPresent());
		assertTrue(bookItemOpt.get().isReferenceOnly());
		assertTrue(memberOpt.isPresent());

		assertNotNull(bookLending.getMember());
		assertNull(bookLending.getBookItem());

		// book.saveBookItem(bookItemOpt.get());
		// assertNotNull(book.getBookItems());

		// assertTrue(book.getBookItems().contains(bookItemOpt.get()));

		// member.saveBookLending(bookLending);

		// assertNotNull(member.getBookLendings());
		// assertTrue(member.getBookLendings().contains(bookLending));

		// assertArrayEquals(book.getBookItems().toArray(), new BookItem[]{bookItem});


		// BookLending bookLending = new BookLending();
		// Member member = new Member();
		// BookItem bookItem = new BookItem();

		// bookLending.setMember(member);
		// bookLending.setBookItem(bookItem);

		// bookItem.setBook(book);
		// member.saveBookLending(bookLending);
		// // member.getBookLendings().add(bookLending);

		// assertEquals(book, bookItem.getBook());
		// assertEquals(member, bookLending.getMember());
		// assertTrue(member.getBookLendings().contains(bookLending));

		// member.removeBookLending(bookLending.getId());
		// assertFalse(member.getBookLendings().contains(bookLending));

	}

}
