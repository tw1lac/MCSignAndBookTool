package se.twilac;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class GlobalHotkeyListener {
	private static NativeKeyListener keyListener;
	private static final Map<KeyStroke, Runnable> keyStrokeFunctionMap = new HashMap<>();

	public static void addHotkey(KeyStroke keyStroke, Runnable runnable) {
		keyStrokeFunctionMap.put(keyStroke, runnable);
	}
	public static void removeHotkey(KeyStroke keyStroke) {
		keyStrokeFunctionMap.remove(keyStroke);
	}
	public static void clearHotkeys() {
		keyStrokeFunctionMap.clear();
	}

	public static void setActive(boolean active) {
		if (active) {
			activate();
		} else {
			inActivate();
		}
		ProgramGlobals.getSaveProfile().setKeyListenerActive(active);
	}

	public static void activate() {
		try {
			TwiGlobalScreen.register();
		} catch (Exception e) {
			e.printStackTrace();
		}
		TwiGlobalScreen.addNativeKeyListener(getKeyListener());
	}

	public static void inActivate() {
		if (keyListener != null) {
			TwiGlobalScreen.removeNativeKeyListener(keyListener);
			keyListener = null;
		}
		try {
			TwiGlobalScreen.unregister();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static NativeKeyListener getKeyListener() {
		if (keyListener == null) {
			keyListener = new NativeKeyListener() {
				@Override
				public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
					KeyStroke keyStroke = KeyStroke.getKeyStroke(nativeEvent.getRawCode(), nativeEvent.getModifiers());
					Runnable runnable = keyStrokeFunctionMap.get(keyStroke);
					if (runnable != null) {
						runnable.run();
					}
				}
			};
		}
		return keyListener;
	}

}
