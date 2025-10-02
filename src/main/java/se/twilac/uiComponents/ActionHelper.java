package se.twilac.uiComponents;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

public class ActionHelper extends AbstractAction {
	private String name;
	private Consumer<ActionEvent> listener;
	private KeyStroke accelerator;
	private char mnemonic;

	public ActionHelper() {

	}
	public ActionHelper(String name) {
		super(name);
		this.name = name;
	}
	public ActionHelper(String name, Icon icon) {
		super(name, icon);
		this.name = name;
	}
	public ActionHelper(String name, Consumer<ActionEvent> listener) {
		super(name);
		this.name = name;
		this.listener = listener;
	}
	public ActionHelper(String name, Icon icon, Consumer<ActionEvent> listener) {
		super(name, icon);
		this.name = name;
		this.listener = listener;
	}

	public ActionHelper setKeyStroke(String keyStroke) {
		return setKeyStroke(KeyStroke.getKeyStroke(keyStroke));
	}
	public ActionHelper setKeyStroke(KeyEvent keyEvent) {
		return setKeyStroke(KeyStroke.getKeyStrokeForEvent(keyEvent));
	}
	public ActionHelper setKeyStroke(KeyStroke keyStroke) {
		accelerator = keyStroke;
		putValue(Action.ACCELERATOR_KEY, keyStroke);
		return this;
	}

	public ActionHelper setMenuItemMnemonic(char keyEvent) {
		mnemonic = keyEvent;
		putValue(Action.MNEMONIC_KEY, keyEvent);
		return this;
	}
	public ActionHelper setListener(Consumer<ActionEvent> listener) {
		this.listener = listener;
		return this;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (listener != null) {
			listener.accept(e);
		}
	}

	@Override
	public boolean accept(Object sender) {
		return super.accept(sender);
	}



	private ActionHelper getAsAction(String name, ActionListener actionListener) {
		return new ActionHelper(name) {
			@Override
			public void actionPerformed(ActionEvent e) {
				actionListener.actionPerformed(e);
			}
		};
	}

	private ActionHelper getAsAction(String name, Runnable runnable) {
		return new ActionHelper(name) {
			@Override
			public void actionPerformed(ActionEvent e) {
				runnable.run();
			}
		};
	}
}
