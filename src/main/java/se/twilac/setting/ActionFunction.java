package se.twilac.setting;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class ActionFunction {
	protected KeyStroke keyStroke;
	protected final TextKey name;
	protected final Runnable runnable;

	public ActionFunction(TextKey name, Runnable runnable) {
		this.name = name;
		this.runnable = runnable;
		KeyBindingPrefs.addActionFunction(name, this);
	}
	public ActionFunction setKeyStroke(KeyStroke keyStroke) {
		this.keyStroke = keyStroke;
//		menuItem.setAccelerator(this.keyStroke);
//		action.putValue(Action.ACCELERATOR_KEY, this.keyStroke);
		return this;
	}
	public ActionFunction setKeyStroke(String keyStroke) {
		return setKeyStroke(KeyStroke.getKeyStroke(keyStroke));
	}
	public ActionFunction setKeyStroke(KeyEvent keyEvent) {
		return setKeyStroke(KeyStroke.getKeyStrokeForEvent(keyEvent));
	}

	public TextKey getName() {
		return name;
	}

	public KeyStroke getKeyStroke() {
		return keyStroke;
	}

	public Runnable getRunnable() {
		return runnable;
	}
}
