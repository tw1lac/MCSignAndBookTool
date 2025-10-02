package se.twilac.setting;

import net.miginfocom.swing.MigLayout;
import se.twilac.uiComponents.uiFactories.Button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeybindingPrefPanel extends JPanel {
//	private final ProgramPreferences prefs;
	private final Map<TextKey, JButton> buttonMap;

//	public KeybindingPrefPanel(ProgramPreferences prefs) {
	public KeybindingPrefPanel() {
		super(new MigLayout("fill", "[][][]"));
//		this.prefs = prefs;
		buttonMap = new HashMap<>();

		JPanel settingsPanel = new JPanel(new MigLayout("fill, wrap 2", "[left][right]"));
		for (TextKey textKey : KeyBindingPrefs.getActionFunctionMap().keySet()) {
			settingsPanel.add(new JLabel(textKey.toString()));

			KeyStroke keyStroke = KeyBindingPrefs.getKeyStroke(textKey);
			String kbString = keyStroke == null ? TextKey.NONE.toString() : keyStroke.toString();

			JButton button = Button.create(b -> editKeyBinding(textKey, b), kbString);
			buttonMap.put(textKey, button);
			settingsPanel.add(button);
		}

		settingsPanel.setOpaque(false);

		JScrollPane scrollPane = new JScrollPane(settingsPanel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		add(scrollPane, "spanx, growx, wrap");
//		scrollPane.setPreferredSize(ScreenInfo.getSmallWindow());
		scrollPane.setPreferredSize(new Dimension(600, 600));
		scrollPane.setOpaque(false);

		add(Button.create("Save keybindings", e -> saveKeybindings()), "");

		JButton resetButton = Button.create("Reset", e -> setAllKB());
		add(Button.setTooltip(resetButton, "Reset fields to current keybindings."), "");

		JButton fullResetButton = Button.create("Reset Full", e -> setAllKB());
		add(Button.setTooltip(fullResetButton, "Reset fields to presets. Remember to save if you want to apply the reset keybindings!"), "");
	}

	private void setAllKB() {
		for (TextKey textKey : buttonMap.keySet()) {
			KeyStroke keyStroke = KeyBindingPrefs.getKeyStroke(textKey);
			String kbString = keyStroke == null ? TextKey.NONE.toString() : keyStroke.toString();
			buttonMap.get(textKey).setText(kbString);
		}
	}

	private void editKeyBinding(TextKey textKey, JButton button) {
		JPanel panel = new JPanel(new MigLayout());

		KeyStroke keyStroke = KeyBindingPrefs.getKeyStroke(textKey);
		KeySettingField keySettingField = new KeySettingField(keyStroke);

		panel.add(keySettingField);
		panel.add(Button.create(TextKey.EDIT.toString(), e -> keySettingField.onEdit()));

		int change = JOptionPane.showConfirmDialog(this, panel, "Edit KeyBinding for " + textKey.toString(), JOptionPane.OK_CANCEL_OPTION);

		if (change == JOptionPane.OK_OPTION) {
			KeyStroke newKeyStroke = keySettingField.getNewKeyStroke();
			button.setText(newKeyStroke == null ? TextKey.NONE.toString() : newKeyStroke.toString());
			KeyBindingPrefs.setKeyStroke(textKey, newKeyStroke);
		}
	}

	private void saveKeybindings() {
//		this.prefs.setKeyBindings(keyBindingPrefs);
//		ProgramGlobals.getPrefs().setKeyBindings(keyBindingPrefs).saveToFile();
//
//		ProgramGlobals.getUndoHandler().refreshUndo();
//		ProgramGlobals.linkActions(ProgramGlobals.getMainPanel());
	}

	private static class KeySettingField extends JTextField {
		private final KeyStroke keyStroke;
		private KeyEvent event;

		public KeySettingField(KeyStroke keyStroke) {
			super(24);
			this.keyStroke = keyStroke;
			if (keyStroke != null) {
				setText(keyStroke.toString());
			}
			setEditable(false);
			addKeyListener(getKeyAdapter());
		}

		private KeyAdapter getKeyAdapter() {
			return new KeyAdapter() {
				KeyEvent lastPressedEvent;

				@Override
				public void keyPressed(KeyEvent e) {
					lastPressedEvent = e;
					if (event == null) {
						setText(KeyStroke.getKeyStrokeForEvent(e).toString());
					}
				}

				@Override
				public void keyReleased(KeyEvent e) {
					if (event == null) {
						event = lastPressedEvent;
					}
				}
			};
		}


		public KeyEvent getEvent() {
			return event;
		}
		public void onEdit() {
			event = null;
			setText("");
			requestFocus();
		}
		public void onRemove() {
			event = null;
			setText("");
		}
		public void onReset() {
			event = null;
			if (keyStroke != null) {
				setText(keyStroke.toString());
			} else {
				setText("");
			}
		}

		public KeyStroke getNewKeyStroke() {
			if (event != null) {
				return KeyStroke.getKeyStrokeForEvent(event);
			} else {
				return KeyStroke.getKeyStroke("null");
			}
		}
	}
}
