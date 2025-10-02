package se.twilac;

import se.twilac.setting.ActionFunction;
import se.twilac.setting.TextKey;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class PasteHandler {
	private Supplier<List<String>> stringSource;
	private List<int[]> inBetweenKeySequences;
	private final RobotHandler robotHandler;
	private boolean doBeRunning = false;

	private static final PasteHandler pasteHandler = new PasteHandler();

	private PasteHandler() {
		this.robotHandler = new RobotHandler();

		new ActionFunction(TextKey.START_PASTING, this::runPasteNewThread).setKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
		new ActionFunction(TextKey.ABORT_PASTING, this::abortPasting).setKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
	}

	public static PasteHandler get() {
		return pasteHandler;
	}

	public void runPasteNewThread() {
		CompletableFuture.runAsync(() -> runPasteFromSource());
	}

	public PasteHandler runPasteFromSource() {
		if (stringSource != null && !doBeRunning) {
			doBeRunning = true;
			System.out.println("have string source");
			List<String> strings = stringSource.get();
			if (!strings.isEmpty()) {
				System.out.println("got strings to paste!");
				addToClipboard(strings.get(0));
				robotHandler.robotDoKeyPressSequence(RobotHandler.getPaste());
				for (int i = 1; i < strings.size() && doBeRunning; i++) {
					System.out.println("pasting " + i);
					System.out.println("  1 doBeRunning: " + doBeRunning);
					if (inBetweenKeySequences != null) {
						robotHandler.robotDoKeyPressSequences(inBetweenKeySequences);
					}
					System.out.println("  2 doBeRunning: " + doBeRunning);
					addToClipboard(strings.get(i));
					if (doBeRunning) {
						robotHandler.robotDoKeyPressSequence(RobotHandler.getPaste());
					}
					System.out.println("  3 doBeRunning: " + doBeRunning);
				}

			}
		} else {
			System.out.println("no source? ");
		}
		doBeRunning = false;
		return this;
	}

	public PasteHandler abortPasting() {
		System.out.println("abortPasting");
		doBeRunning = false;
		return this;
	}

	public PasteHandler setStringSource(Supplier<List<String>> stringSource, List<int[]> inBetweenKeySequences) {
		System.out.println("setStringSource: " + stringSource + ", inBetweenKeySequences: " + inBetweenKeySequences);
		this.stringSource = stringSource;
		this.inBetweenKeySequences = inBetweenKeySequences;
		return this;
	}

	public PasteHandler addToClipboard(String text) {
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
		return this;
	}
}
