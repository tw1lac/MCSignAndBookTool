package se.twilac.bookUI;

import net.miginfocom.swing.MigLayout;
import se.twilac.dataContainers.Book;
import se.twilac.dataContainers.Page;
import se.twilac.uiComponents.DocumentUndoManager;
import se.twilac.uiComponents.TwiScrollPane;
import se.twilac.uiComponents.uiFactories.Button;
import se.twilac.uiComponents.uiFactories.Label;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookPanel extends JPanel {
	private static Map<Book, DocumentUndoManager> bokSpecUndoMan = new HashMap<>();
	private JPanel emptyPage;
	private JLabel title;
	private JPanel pageList;
	private TwiScrollPane pagesScrollPane;
	private Book book;
	private List<BookPage> pages;

	public BookPanel() {
		super(new MigLayout("wrap 1"));
		emptyPage = getPageWrapper(new BookPage(0));
		emptyPage.setVisible(false);
		title = Label.create("No Book Selected");
		pageList = new JPanel(new MigLayout("wrap 1"));
		add(title);
		pagesScrollPane = new TwiScrollPane(pageList).setVSBPol(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(pagesScrollPane);
		pageList.add(emptyPage);
		pages = new ArrayList<>();
	}

	public BookPanel setBook(Book book) {
		if (this.book != book) {
			DocumentUndoManager oldUndoManager = bokSpecUndoMan.get(this.book);
			pages.forEach(p -> p.removeUndoListener(oldUndoManager));
			this.book = book;
			pages.clear();
			pageList.removeAll();
			if (book != null) {
				title.setText(book.getName());
				List<Page> bookPages = book.getPages();
				for (int i = 0; i < bookPages.size(); i++) {
//					bookPages.get(i);
					pages.add(new BookPage(i).setPage(bookPages.get(i)));
				}
				DocumentUndoManager undoManager = bokSpecUndoMan.computeIfAbsent(book, k -> new DocumentUndoManager());
				for (BookPage page : pages) {
					page.addUndoListener(undoManager);
					pageList.add(getPageWrapper(page));
				}


				ActionMap actionMap = new ActionMap();
				actionMap.put(undoManager.getRedoAction().toString(), undoManager.getRedoAction());
				actionMap.put(undoManager.getUndoAction().toString(), undoManager.getUndoAction());
				this.setActionMap(actionMap);

				InputMap inputMap = new InputMap();
				inputMap.put((KeyStroke) undoManager.getRedoAction().getValue(Action.ACCELERATOR_KEY), undoManager.getRedoAction().toString());
				inputMap.put((KeyStroke) undoManager.getUndoAction().getValue(Action.ACCELERATOR_KEY), undoManager.getUndoAction().toString());
				this.setInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, inputMap);
			} else {
				title.setText("No Book Selected");
				pageList.add(emptyPage);
			}
		}

		return this;
	}

	public Book getBook() {
		return book;
	}

	public BookPanel addPage(String s) {
		System.out.println("Add Page: \"" + s + "\"");
		Page newPage = new Page(s);
		int nr = book.getNumPages();
		book.addPage(nr, newPage);
		BookPage newBookPage = new BookPage(nr).setPage(newPage);
		newBookPage.addUndoListener(bokSpecUndoMan.get(book));
		pages.add(nr, newBookPage);
		for (int i = nr; i < pages.size(); i++) {
			pages.get(i).setNr(i);
		}
		pageList.add(getPageWrapper(newBookPage), nr);
		updatePageListBorders(nr);
		return this;
	}

	public BookPage addPageText(String s) {
		System.out.println("Add Page Text: \"" + s + "\"");
		BookPage bookPage = pages.get(pages.size() - 1);
		bookPage.appendText(s);
		System.out.println("PageText: " + bookPage.getText());
		return bookPage;
	}

	public BookPanel addPageAfter(BookPage page) {
		System.out.println("Add Page After: " + page.getNr());
		addPageBelow(page);
		return this;
	}

	private JPanel getPageWrapper(BookPage page) {
		JPanel wrapper = new JPanel(new MigLayout("ins 0"));
		wrapper.add(page);
		wrapper.add(Button.setTooltip(Button.create("X", e -> removePage(page)), "Delete"));
		wrapper.add(Button.setTooltip(Button.create("\u2B9D", e -> movePage(page, -1)), "Move Up")); // \u2B9D
		wrapper.add(Button.setTooltip(Button.create("\u2B9F", e -> movePage(page, 1)), "Move Down")); // ⮟
		wrapper.add(Button.setTooltip(Button.create("\uFE3D", e -> pageToTop(page)), "Move First")); // ︽
		wrapper.add(Button.setTooltip(Button.create("\uFE3E", e -> pageToBot(page)), "Move Last")); // ︾
		wrapper.add(Button.setTooltip(Button.create("\u25B2+", e -> addPageAbove(page)), "Add Page Above")); // ▲
		wrapper.add(Button.setTooltip(Button.create("\u25BC+", e -> addPageBelow(page)), "Add Page Below")); // ▼
//		wrapper.add(Button.create("\u2BA0", e -> addPageText("\u000C"))); // ⮠
//		wrapper.add(Button.create("\u2BA0", e -> addPageText("\n\u000C\n"))); // ⮠
		wrapper.setBorder(BorderFactory.createTitledBorder("Page " + page.getNr()));

		return wrapper;
	}

	private void removePage(BookPage page) {
		int nr = page.getNr();
		book.removePage(nr);
		pages.remove(nr).removeUndoListener(bokSpecUndoMan.get(book));
		pageList.remove(nr);
		for (int i = nr; i < pages.size(); i++) {
			pages.get(i).setNr(i);
			((JComponent)pageList.getComponent(i)).setBorder(BorderFactory.createTitledBorder("Page " + i));
		}
	}

	private void movePage(BookPage page, int dir) {
		book.movePage(page.getNr(), dir);

		int nr = page.getNr();
		int newNr = Math.max(0, Math.min(pages.size() - 1, nr + dir));
		BookPage newBookPage = pages.remove(nr);
		pages.add(newNr, newBookPage);
		pageList.add(pageList.getComponent(nr), newNr);

		for (int i = 0; i < pages.size(); i++) {
			pages.get(i).setNr(i);
			((JComponent)pageList.getComponent(i)).setBorder(BorderFactory.createTitledBorder("Page " + i));
		}
	}

	private void addPageBelow(BookPage page) {
		int newNr = page.getNr() + 1;
		addNewPageAt(newNr);

	}
	private void addPageAbove(BookPage page) {
		int nr = page.getNr();
		addNewPageAt(nr);

	}

	private void addNewPageAt(int nr) {
//		bokSpecUndoMan.get(book).addEdit(new AddPageAction(book, nr).doRedo());
		Page newPage = new Page();
		book.addPage(nr, newPage);
		BookPage newBookPage = new BookPage(nr).setPage(newPage);
		newBookPage.addUndoListener(bokSpecUndoMan.get(book));
		pages.add(nr, newBookPage);
		for (int i = nr; i < pages.size(); i++) {
			pages.get(i).setNr(i);
		}
		pageList.add(getPageWrapper(newBookPage), nr);
		updatePageListBorders(nr);
	}

	private void pageToTop(BookPage page) {

		int nr = page.getNr();
		int newNr = 0;
		Page moveToTop = book.remove(nr);

		book.addPage(newNr, moveToTop);
		BookPage newBookPage = pages.remove(nr);

		pages.add(newNr, newBookPage);

		for (int i = newNr; i < pages.size(); i++) {
			pages.get(i).setNr(i);
		}

		pageList.add(pageList.getComponent(nr), newNr);
		updatePageListBorders(newNr);
	}
	private void pageToBot(BookPage page) {
		int nr = page.getNr();
		int newNr = pages.size()-1;
		Page moveToBot = book.remove(nr);
		book.addPage(newNr, moveToBot);

		BookPage newBookPage = pages.remove(nr);

		pages.add(newNr, newBookPage);

		for (int i = nr; i < pages.size(); i++) {
			pages.get(i).setNr(i);
		}

		pageList.add(pageList.getComponent(nr), newNr);
		updatePageListBorders(nr);
	}

	private void updatePageListBorders(int nr) {
		for (int i = nr; i < pages.size(); i++) {
			((JComponent)pageList.getComponent(i)).setBorder(BorderFactory.createTitledBorder("Page " + i));
		}
	}

	private void arrows() {
//		String arrow = "\u25B2";
//		String arrow = "\u25BC";
//		String arrow = "\u2B9D";
//		String arrow = "\u2B9F";
//		String arrow = "\uFE3D";
//		String arrow = "\uFE3E";
//		String arrow = "\u21F1";
//		String arrow = "\u21F2";
//		String arrow = "\u290A";
//		String arrow = "\u290B";
//		String arrow = "\u2BED";
//		String arrow = "\u2BEF";
//		String arrow = "\u2B71";
//		String arrow = "\u2B73";
//		String arrow = "\u2A5E";
//		String arrow = "\u2A63";
//		String arrow = "\u2BC5";
//		String arrow = "\u2BC6";
//		String arrow = "+\u2B0F;
//		String arrow = "+\u2B0E;
//		String arrow = "+\u2BAD;
//		String arrow = "+\u2BAF;
//		String arrow = "\u25B2+;
//		String arrow = "\u25BC+;
//		String arrow = "\u2BA8";
//		String arrow = "\u2BA9";
//		String arrow = "\uD83D\uDF81+";
//		String arrow = "\uD83D\uDF83+";
//		String arrow = "\uD83E\uDC1D";
//		String arrow = "\uD83E\uDC1F";
	}
}
