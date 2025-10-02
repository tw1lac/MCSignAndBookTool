package se.twilac.setting;

import javax.swing.*;
import java.util.EnumMap;
import java.util.Map;

public class KeyBindingPrefs {
	private static final Map<TextKey, ActionFunction> actionFunctionMap = new EnumMap<>(TextKey.class);

	public static void addActionFunction(TextKey textKey, ActionFunction function) {
		actionFunctionMap.put(textKey, function);
	}

	public static KeyStroke getKeyStroke(TextKey key) {
		return actionFunctionMap.get(key).getKeyStroke();
	}

	public static void setKeyStroke(TextKey key, KeyStroke keyStroke) {
		actionFunctionMap.get(key).setKeyStroke(keyStroke);
	}

	public static String mapToString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("\n");
		for (ActionFunction action : actionFunctionMap.values()) {
			stringBuilder.append(action.getName().name())
					.append(" = ")
					.append(action.getKeyStroke())
					.append("\n");

		}
		return stringBuilder.toString();
	}

	public static void parseString(String string) {
		String[] lines = string.split("\n");
		for (String line : lines) {
			String[] s = line.split("=");
//			System.out.println("parsing: \"" + line + "\"");
			if (1 < s.length) {
				try {
					TextKey textKey = TextKey.valueOf(s[0].strip());
					KeyStroke keyStroke = KeyStroke.getKeyStroke(s[1]);
					ActionFunction actionFunction = actionFunctionMap.get(textKey);
					if (actionFunction != null) {
						actionFunction.setKeyStroke(keyStroke);
					}
				} catch (Exception e) {
					System.out.println("failed to parse keybinding \"" + line + "\":");
//					System.out.println(e.getCause().getMessage());
					System.out.println(e.getMessage());
				}
			}
		}
	}


//	public ActionMap getActionMap() {
//		ActionMap actionMap = new ActionMap();
//		for (ActionFunction function : actionFunctionMap.values()) {
//			actionMap.put(function.getName(), function.getAction());
//		}
////		for (ActionMapActions action : ActionMapActions.values()) {
////			actionMap.put(action.getName(), action.getAction());
////		}
//		return actionMap;
//	}

	public static InputMap getInputMap() {
		InputMap inputMap = new InputMap();
		for (ActionFunction function : actionFunctionMap.values()) {
			if (function.getKeyStroke() != null) {
				inputMap.put(function.getKeyStroke(), function.getName());
			}
		}
		return inputMap;
	}

//	public ActionMap addActionMap(JComponent component) {
//		ActionMap actionMap = component.getActionMap();
//		for (ActionFunction function : actionFunctionMap.values()) {
//			actionMap.put(function.getName(), function.getAction());
//		}
//		return actionMap;
//	}

	public static InputMap addInputMap(JComponent component) {
		InputMap inputMap = component.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		for (ActionFunction function : actionFunctionMap.values()) {
			if (function.getKeyStroke() != null) {
				inputMap.put(function.getKeyStroke(), function.getName());
			}
		}
		return inputMap;
	}

	public static Map<TextKey, ActionFunction> getActionFunctionMap() {
		return actionFunctionMap;
	}
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("\n");
		for (ActionFunction action : actionFunctionMap.values()) {
			stringBuilder.append(action.getName().name())
					.append(" = ")
					.append(action.getKeyStroke())
					.append("\n");

		}
		return stringBuilder.toString();
	}
}
