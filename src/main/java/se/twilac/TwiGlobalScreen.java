package se.twilac;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseMotionListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelListener;

import java.util.HashSet;
import java.util.Set;

public class TwiGlobalScreen extends GlobalScreen {
	static Set<NativeKeyListener> keyListeners = new HashSet<>();
	static Set<NativeMouseListener> mouseListeners = new HashSet<>();
	static Set<NativeMouseMotionListener> mouseMotionListeners = new HashSet<>();
	static Set<NativeMouseWheelListener> mouseWheelListeners = new HashSet<>();


	public static void addNativeKeyListener(NativeKeyListener listener) {
		if (listener != null && keyListeners.add(listener)) {
			eventListeners.add(NativeKeyListener.class, listener);
		}
	}
	public static void removeNativeKeyListener(NativeKeyListener listener) {
		if (listener != null && keyListeners.remove(listener)) {
			eventListeners.remove(NativeKeyListener.class, listener);
		}
	}


	public static void addNativeMouseListener(NativeMouseListener listener) {
		if (listener != null && mouseListeners.add(listener)) {
			eventListeners.add(NativeMouseListener.class, listener);
		}
	}
	public static void removeNativeMouseListener(NativeMouseListener listener) {
		if (listener != null && mouseListeners.remove(listener)) {
			eventListeners.remove(NativeMouseListener.class, listener);
		}
	}


	public static void addNativeMouseMotionListener(NativeMouseMotionListener listener) {
		if (listener != null && mouseMotionListeners.add(listener)) {
			eventListeners.add(NativeMouseMotionListener.class, listener);
		}
	}
	public static void removeNativeMouseMotionListener(NativeMouseMotionListener listener) {
		if (listener != null && mouseMotionListeners.remove(listener)) {
			eventListeners.remove(NativeMouseMotionListener.class, listener);
		}
	}

	public static void addNativeMouseWheelListener(NativeMouseWheelListener listener) {
		if (listener != null && mouseWheelListeners.add(listener)) {
			eventListeners.add(NativeMouseWheelListener.class, listener);
		}
	}
	public static void removeNativeMouseWheelListener(NativeMouseWheelListener listener) {
		if (listener != null && mouseWheelListeners.remove(listener)) {
			eventListeners.remove(NativeMouseWheelListener.class, listener);
		}
	}


	public static void register() throws NativeHookException {
		registerNativeHook();
	}

	public static void unregister() throws NativeHookException {
		unregisterNativeHook();
		eventExecutor = null;
	}
}
