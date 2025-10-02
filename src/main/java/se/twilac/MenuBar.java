package se.twilac;

import net.miginfocom.swing.MigLayout;
import se.twilac.setting.SaveProfile;
import se.twilac.uiComponents.ActionHelper;
import se.twilac.uiComponents.TwiFileChooser;
import se.twilac.uiComponents.TwiScrollPane;
import se.twilac.uiComponents.uiFactories.Button;
import se.twilac.uiComponents.uiFactories.CheckBox;
import se.twilac.uiComponents.uiFactories.Label;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MenuBar extends JMenuBar {

	public MenuBar(JFrame parent) {
		add(getFileMenu());
		SaveProfile saveProfile = ProgramGlobals.getSaveProfile();
		add(CheckBox.create("Stay on top", parent::setAlwaysOnTop));
		add(CheckBox.create("Key listener active", saveProfile.isKeyListenerActive(), GlobalHotkeyListener::setActive));
		GlobalHotkeyListener.setActive(saveProfile.isKeyListenerActive());
	}

	public static JMenuItem createMenuItem(String itemText, int keyEvent, ActionListener actionListener) {
		JMenuItem menuItem = new JMenuItem(itemText);
		menuItem.setMnemonic(keyEvent);
		menuItem.addActionListener(actionListener);
		return menuItem;
	}

	private JMenu getFileMenu() {
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		file.add("New");
		file.add(new ActionHelper("Save As", e -> WriteBook.saveBook(ProgramGlobals.getCurrentBook())).setKeyStroke("control S"));
		file.add(new ActionHelper("Open", e -> WriteBook.openBook()).setKeyStroke("control O"));
		file.add(new ActionHelper("Edit Lib Folders", e -> editLibFolders()));
		file.add("Set Folder");
		file.add("Reload");
		file.add("Import");
//		file.add("New");
//		file.add("Open");
//		file.add("Set Folder");
//		file.add("Reload");
//		file.add("Import");
		return file;
	}

	private void editLibFolders() {
		SaveProfile saveProfile = ProgramGlobals.getSaveProfile();
		Set<File> bookLibC = new TreeSet<>(saveProfile.getBookLibC());
		JPanel bookLibPanel = getBookLibPanel();
		int opt = JOptionPane.showConfirmDialog(null, new TwiScrollPane(bookLibPanel), "Edit Lib Folders", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (opt == JOptionPane.OK_OPTION) {
			saveProfile.writeSettings();
		} else {
			saveProfile.getBookLibDirs().clear().addAll(bookLibC);
		}
	}

	JPanel getBookLibPanel() {
		JPanel panel = new JPanel(new MigLayout());
		SaveProfile saveProfile = ProgramGlobals.getSaveProfile();
		for (File file : saveProfile.getBookLibC()) {
			panel.add(Label.create(file.getPath()));
			panel.add(Button.create("X", e -> saveProfile.removeFromBookLibDir(file, false)), "wrap");
		}
		panel.add(Button.create("Add folder", e -> {
			TwiFileChooser fileChooser = ProgramGlobals.getFileChooser();
			int fileSelectionMode = fileChooser.getFileSelectionMode();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			File file = fileChooser.openFile(0);
			if (file != null) {
				saveProfile.addBookLibDir(file, false);

				panel.add(Label.create(file.getPath()));
				panel.add(Button.create("X", e2 -> saveProfile.removeFromBookLibDir(file)), "wrap");
				panel.repaint();
				fileChooser.setFileSelectionMode(fileSelectionMode);
			}
		}));
		return panel;
	}

	List<RecentItem> recentItems = new ArrayList<>();
	public JMenu getRecentMenu() {
		JMenu recentMenu = new JMenu("Open Recent");
		recentMenu.setMnemonic(KeyEvent.VK_R);
		recentMenu.getAccessibleContext().setAccessibleDescription("Allows you to access recently opened files.");

		recentMenu.add(new JSeparator());
		recentMenu.add(createMenuItem("Clear", KeyEvent.VK_C, e -> clearRecent()));
		return recentMenu;
	}

	public void updateRecent(JMenu recentMenu) {
		for (RecentItem recentItem : recentItems) {
			recentMenu.remove(recentItem);
		}
		recentItems.clear();

//		List<File> recent = new ArrayList<>(SaveProfile.get().getRecent().getFiles());
//
//		for (int i = 0; i < recent.size(); i++) {
//			File file = recent.get(recent.size() - i - 1);
//			if (recentItems.size() <= i || !recentItems.get(i).samePath(file.getPath())) {
//				RecentItem item = new RecentItem(file);
//				recentItems.add(item);
//				recentMenu.add(item, recentMenu.getItemCount() - 2);
//			}
//		}
	}

	static class RecentItem extends JMenuItem {
		private final File file;
		public RecentItem(File file) {
			super(file.getName());
			this.file = file;
			addActionListener(e -> openFile());
		}

		private void openFile() {
//			new FileDialog().openFile(file);
		}
		public String getFilepath() {
			return file.getPath();
		}
		public boolean samePath(String filepath) {
			return file.getPath().equals(filepath);
		}

		public File getFile() {
			return file;
		}
	}

	public static void clearRecent() {
//		int dialogResult = JOptionPane.showConfirmDialog(ProgramGlobals.getMainPanel(),
//		                                                 "Are you sure you want to clear the Recent history?", "Confirm Clear",
//		                                                 JOptionPane.YES_NO_OPTION);
//		if (dialogResult == JOptionPane.YES_OPTION) {
//			SaveProfile.get().clearRecent();
//			ProgramGlobals.getMenuBar().updateRecent();
//		}
	}

	private Action getAsAction(String name, ActionListener actionListener) {
		return new AbstractAction(name) {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionListener.actionPerformed(e);
			}
		};
	}

	private AbstractAction getAsAction(String name, Runnable runnable) {
		return new AbstractAction(name) {
			@Override
			public void actionPerformed(ActionEvent e) {
				runnable.run();
			}
		};
	}
}
