package se.twilac;

import se.twilac.bookUI.BookPage;
import se.twilac.bookUI.BookPanel;
import se.twilac.setting.ActionFunction;
import se.twilac.setting.TextKey;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CopyLoader {
	private final RobotHandler robotHandler;
	private int pagesToCopy;
	private BookPanel bookPanel;


	private static final CopyLoader copyLoader = new CopyLoader();

	private CopyLoader() {
		robotHandler = new RobotHandler();
		new ActionFunction(TextKey.COPY_FROM_GAME, this::copyBook).setKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
	}

	public CopyLoader setBookPanel(BookPanel bookPanel) {
		this.bookPanel = bookPanel;
		return this;
	}

	public static CopyLoader get() {
		return copyLoader;
	}

	public CopyLoader setPagesToCopy(int pagesToCopy) {
		this.pagesToCopy = pagesToCopy;
		return this;
	}

	public void copyBook(int pagesToCopy) {
		List<String> pages = new ArrayList<>();
		if (0 < pagesToCopy) {
			System.out.println("Page 0");
			robotHandler.robotDoKeyPressSequences(RobotHandler.getSelectAll(), RobotHandler.getCopy());
			somethingTransferable();
			for (int i = 1; i < pagesToCopy; i++) {
				robotHandler.robotDoKeyPressSequence(RobotHandler.getNextPage());
				System.out.println("Page " + i);
				robotHandler.robotDoKeyPressSequences(RobotHandler.getSelectAll(), RobotHandler.getCopy());
				somethingTransferable();
			}
		}
	}

	public void copyBook() {
		List<String> pages = new ArrayList<>();
		System.out.println("Copy Book! " + pagesToCopy + ", " + bookPanel);
		if (0 < pagesToCopy && bookPanel != null) {
			System.out.println("Page 0");
			robotHandler.robotDoKeyPressSequences(RobotHandler.getSelectAll(), RobotHandler.getCopy());
			BookPage lastPage = bookPanel.addPageText(somethingTransferable2());
			System.out.println("  page 0 done!");
			for (int i = 1; i < pagesToCopy; i++) {
				System.out.println("Page " + i);
				bookPanel.addPageAfter(lastPage);
				robotHandler.robotDoKeyPressSequence(RobotHandler.getNextPage());
				robotHandler.robotDoKeyPressSequences(RobotHandler.getSelectAll(), RobotHandler.getCopy());
				lastPage = bookPanel.addPageText(somethingTransferable2());
				System.out.println("  page " + i + " done!");
			}
		}
		System.out.println(" Colying done!");
	}

	public void copyBook11() {
		List<String> pages = new ArrayList<>();
		if (0 < pagesToCopy) {
			System.out.println("Page 0");
			robotHandler.robotDoKeyPressSequences(RobotHandler.getSelectAll(), RobotHandler.getCopy());
			somethingTransferable();
			for (int i = 1; i < pagesToCopy; i++) {
				robotHandler.robotDoKeyPressSequence(RobotHandler.getNextPage());
				System.out.println("Page " + i);
				robotHandler.robotDoKeyPressSequences(RobotHandler.getSelectAll(), RobotHandler.getCopy());
				somethingTransferable();
			}
		}
	}

	private static void somethingTransferable() {
		Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		if (contents != null) {
			try {
				Object transferData = contents.getTransferData(DataFlavor.stringFlavor);
				System.out.println(transferData);
			} catch (UnsupportedFlavorException | IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static String somethingTransferable2() {
		Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		if (contents != null) {
			try {
				Object transferData = contents.getTransferData(DataFlavor.stringFlavor);
				System.out.println(transferData);
				return transferData.toString();
			} catch (UnsupportedFlavorException | IOException e) {
				throw new RuntimeException(e);
			}
		}
		return "";
	}
}
