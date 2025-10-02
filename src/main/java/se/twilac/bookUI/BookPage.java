package se.twilac.bookUI;

import net.miginfocom.swing.MigLayout;
import se.twilac.dataContainers.Page;
import se.twilac.uiComponents.DocumentUndoManager;

import javax.swing.*;
import javax.swing.plaf.TextUI;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class BookPage extends JPanel {
	private String text;
	private Page page;
	private JTextArea textArea;
//	private JTextPane textArea;
	private int nr;

	public BookPage(int nr) {
		super(new MigLayout("fill"));
		this.nr = nr;
		textArea = new JTextArea(14, 20);
//		textArea = new JTextPane();
		textArea.setFont(new Font("Small Fonts", Font.PLAIN, 11));
		textArea.setText("" + nr);
		add(textArea, "growx, growy, wrap");
	}

	public BookPage addUndoListener(DocumentUndoManager undoManager) {
		textArea.getDocument().addUndoableEditListener(undoManager);
		return this;
	}
	public BookPage removeUndoListener(DocumentUndoManager undoManager) {
		textArea.getDocument().removeUndoableEditListener(undoManager);
		return this;
	}

	public BookPage setNr(int nr) {
		this.nr = nr;
		return this;
	}

	public int getNr() {
		return nr;
	}

	public BookPage setText(String text) {
		System.out.println("setText! + " + text);
		this.text = text;
		textArea.setText(text);
		return this;
	}

	public BookPage appendText(String text) {
		System.out.println("appendText! + " + text);
		textArea.append(text);
//		Document document = textArea.getDocument();
//		try {
//			document.insertString(document.getLength(), text, null);
//		} catch (BadLocationException e) {
//			throw new RuntimeException(e);
//		}
		this.text = textArea.getText();
		return this;
	}

	public BookPage setPage(Page page) {
		System.out.println("setPage!");
		this.page = page;
		this.text = page.getText();
		textArea.setText(text);
		return this;
	}

	public String getText() {
		return text;
	}

	public void printInfo() {
		Document document = textArea.getDocument();
		System.out.println("Page: " + nr);
		System.out.println(" " + document);
		System.out.println(" " + document.getClass().getSimpleName());
		if (document instanceof PlainDocument plainDocument) {
			System.out.println("  " + plainDocument.getDefaultRootElement());
		}
		TextUI ui1 = textArea.getUI();
		System.out.println(" UI");
		System.out.println("  " + ui1);
		System.out.println("  " + ui1.getClass().getSimpleName());

	}
}
