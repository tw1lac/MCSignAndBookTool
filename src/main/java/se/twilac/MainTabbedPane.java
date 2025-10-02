package se.twilac;

import se.twilac.setting.ActionFunction;
import se.twilac.setting.KeyBindingPrefs;

import javax.swing.*;
import java.util.List;

public class MainTabbedPane extends JTabbedPane {

	BookTabPanel bookTabPanel;
	SignTabPanel signTabPanel;


	public MainTabbedPane() {
		bookTabPanel = new BookTabPanel();
		signTabPanel = new SignTabPanel();
		addTab("Books", bookTabPanel);
		addTab("Signs", signTabPanel);
		updateHotkeys();

	}

	public void updateHotkeys() {
		GlobalHotkeyListener.clearHotkeys();
		for (ActionFunction actionFunction : KeyBindingPrefs.getActionFunctionMap().values()) {
			GlobalHotkeyListener.addHotkey(actionFunction.getKeyStroke(), actionFunction.getRunnable());
		}
	}

	@Override
	public void setSelectedIndex(int index) {
		if (index != getSelectedIndex()) {
			if (index == 0) {
				ProgramGlobals.setCurrentBook(bookTabPanel.getSelectedBook());
				PasteHandler.get().setStringSource(() -> bookTabPanel.getStrings(), List.of(RobotHandler.getNextPage()));
				CopyLoader.get().setPagesToCopy(2);
			} else if (index == 1) {
				ProgramGlobals.setCurrentBook(null);
				PasteHandler.get().setStringSource(() -> signTabPanel.getStrings(), List.of(RobotHandler.getDown()));
				CopyLoader.get().setPagesToCopy(0);
			}
		}
		super.setSelectedIndex(index);
	}
}
