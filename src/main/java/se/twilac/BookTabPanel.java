package se.twilac;

import net.miginfocom.swing.MigLayout;
import se.twilac.bookUI.BookPanel;
import se.twilac.dataContainers.Book;
import se.twilac.uiComponents.SearchListPanel;
import se.twilac.uiComponents.TwiListRenderer;
import se.twilac.uiComponents.uiFactories.Button;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookTabPanel extends JPanel {
	private final List<Book> bookList;
	private final SearchListPanel<Book> bookSearchList;
	private final BookPanel bookPanel;
	public BookTabPanel() {
		super(new MigLayout("fill", "[][grow]", "[grow][grow]"));
		WriteBook.loadBookLibs();
		bookList = ProgramGlobals.getBookList();
		bookSearchList = new SearchListPanel<>("", bookList, this::search).setSelectionConsumer(this::bookSelected);
		bookSearchList.setRenderer(new TwiListRenderer<>(Book::getDisplayName));
		bookPanel = new BookPanel();
		CopyLoader.get().setBookPanel(bookPanel);

		add(bookSearchList, "growy");
		add(bookPanel, "growx, growy, wrap");
		add(Button.create("New Book", e -> addNewBook()));
		addDummyBooks();
	}

	private void bookSelected(Book book) {
		ProgramGlobals.setCurrentBook(book);
		bookPanel.setBook(book);
	}

	public Book getSelectedBook() {
		return bookPanel.getBook();
	}

	public List<String> getStrings() {
		System.out.println("isValid: " + isValid() + ", hasBook: " + (bookPanel.getBook() != null));
		if (isValid() && bookPanel.getBook() != null) {
			return bookPanel.getBook().getPagesAsStrings();
		}
		return new ArrayList<>();
	}

	public List<String> getPasteSource() {
		if (isVisible() && bookPanel.getBook() != null) {
			return bookPanel.getBook().getPagesAsStrings();
		}
		return new ArrayList<>();
	}

	private void addNewBook() {
		Book book = new Book("Unnamed").addPage("");
		bookList.add(book);
		bookPanel.setBook(book);
	}

	private void addDummyBooks() {
		Book book = getDummyBook("TempBook1", 10);
		bookList.add(book);
		Book book2 = getDummyBook("TempBook2", 2);
		bookList.add(book2);
	}

	private Book getDummyBook(String name, int pages) {
		Book book = new Book(name).addPage("P1\nTempText\ntempText").addPage("P2\nTempText\ntempText");
		for (int i = 0; i < pages; i++) {
			book.addPage("P"+ (i+1) + "\nTempText\ntempText");
		}
		return book;
	}

	private boolean search(Book book, String filter) {
		if (book.getName().toUpperCase(Locale.ROOT).contains(filter.toUpperCase(Locale.ROOT))) {
			return true;
		} else {
			return false;
		}
	}
}