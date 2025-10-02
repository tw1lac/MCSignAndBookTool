package se.twilac.setting;

import se.twilac.TimeLogger;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.TreeMap;

public class SettingsLoader {

	public static <T> boolean saveToFile(T instance, File file) {
		System.out.println("saveToFile: " + file);
		return writeToFile(toSaveString(instance), file);
	}

	public static <T> T setFromFile(T instance, File file) {

		TimeLogger timeLogger = new TimeLogger().start();
		String string = readStringFrom(file);
		timeLogger.log("readStringFrom");
		T instance1 = fromString(instance, string);
		timeLogger.log("instanceCreated!");
		timeLogger.print();
		return instance1;
	}

	public static <T> T setFromFile111(T instance, File file) {
		return fromString(instance, readStringFrom(file));
	}

	private static boolean writeToFile(String string, File file) {
		System.out.println("Writing: " + file.getPath());
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			outputStream.write(string.getBytes());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static <T> String toSaveString(T instance) {
		Field[] declaredFields = instance.getClass().getDeclaredFields();

		StringBuilder sb = new StringBuilder();
		for (Field field : declaredFields) {
			if (notTransientNorStatic(field)) {
				try {
					String name = field.getName();
					field.setAccessible(true);
					Object o = field.get(instance);

					if (o instanceof Collection<?> collection) {
						sb.append(name).append(" = ").append("[\n");
						for (Object e : collection) {
							if (e instanceof String || e instanceof File) {
								sb.append("\t\"").append(e).append("\",\n");
							} else {
								sb.append("\t").append(e).append(",\n");
							}
						}
						sb.append("];\n");
					} else if (o != null) {
						if (o instanceof String || o instanceof File) {
							sb.append(name).append(" = \"").append(o).append("\";\n");
						} else {
							sb.append(name).append(" = ").append(o).append(";\n");
						}
					}
					field.setAccessible(false);

				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return sb.toString();
	}

	private static String readStringFrom(File file) {
		TimeLogger timeLogger = new TimeLogger();
		timeLogger.start();
		if (file != null && file.exists()) {
			System.out.println("reading file: " + file);
			try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
				StringBuilder sb = new StringBuilder();
				r.lines().forEach(l -> sb.append(l).append("\n"));
				timeLogger.log("Strings read");
				timeLogger.print();
				return sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("Could not find file: \"" + file + "\"");
		}
		return "";
	}
	public static <T> T fromString(Class<T> targetClass, String s) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
//		T instance = targetClass.newInstance();
		T instance = targetClass.getConstructor().newInstance();
		return fromString(instance, s);
	}
	public static <T> T fromString(T instance, String s) {
		TimeLogger timeLogger = new TimeLogger();
		timeLogger.start();
		Field[] declaredFields = instance.getClass().getDeclaredFields();
		timeLogger.log("Fields fetched!");
		TreeMap<Integer, Field> offsetToField = new TreeMap<>();
		for (Field field : declaredFields) {
			if (notTransientNorStatic(field)) {
				String name = field.getName();
				int indexOf = s.indexOf(name + " = ");
				if (indexOf != -1) {
					offsetToField.put(indexOf, field);
				}
			}
		}
		timeLogger.log("Fields filtered!");

		for (Integer offs : offsetToField.keySet()) {
			Field field = offsetToField.get(offs);
			String name = field.getName();
			String fieldString = s.strip().split(name + " = ")[1];
			Integer nOffs = offsetToField.higherKey(offs);
			if (nOffs != null) {
				String nextName = offsetToField.get(nOffs).getName();
				fieldString = fieldString.strip().split(nextName + " = ")[0];
			}

			parseField(instance, field, fieldString);
		}
		timeLogger.log("Fields parsed!");
		timeLogger.print();
		return instance;
	}
	public static <T> T fromString111(T instance, String s) {
		Field[] declaredFields = instance.getClass().getDeclaredFields();
		TreeMap<Integer, Field> offsetToField = new TreeMap<>();
		for (Field field : declaredFields) {
			if (notTransientNorStatic(field)) {
				String name = field.getName();
				int indexOf = s.indexOf(name + " = ");
				if (indexOf != -1) {
					offsetToField.put(indexOf, field);
				}
			}
		}

		for (Integer offs : offsetToField.keySet()) {
			Field field = offsetToField.get(offs);
			String name = field.getName();
			String fieldString = s.strip().split(name + " = ")[1];
			Integer nOffs = offsetToField.higherKey(offs);
			if (nOffs != null) {
				String nextName = offsetToField.get(nOffs).getName();
				fieldString = fieldString.strip().split(nextName + " = ")[0];
			}

			parseField(instance, field, fieldString);
		}
		return instance;
	}

	private static <T> void parseField(T instance, Field field, String strip) {
		String s1 = strip.replaceAll("(^\")|(\"?;\\s*$)", "");
//		System.out.println("" + field.getName() + " - " + field.getType() + " [" + s1 +"]");

		field.setAccessible(true);
		try {
			if (field.getType().getSuperclass() == Enum.class) {
				field.set(instance, Enum.valueOf((Class<Enum>) field.getType(), s1));

			} else if (field.getType() == Float.class) {
				field.set(instance, Float.parseFloat(s1));
			} else if (field.getType() == Integer.class) {
				field.set(instance, Integer.parseInt(s1));
			} else if (field.getType() == Boolean.class) {
				field.set(instance, Boolean.parseBoolean(s1));
			} else if (field.getType() == String.class) {
				System.out.println("String: " + field.getName() + " [" + s1 +"] from [" + strip + "]");
				field.set(instance, s1);
			} else if (field.getType() == File.class) {
				field.set(instance, new File(s1.strip()));
			} else if (field.getType() == FileListTracker.class) {
				field.set(instance, new FileListTracker().fromString(s1));
//			} else if (field.getType() == DataSourceTracker.class) {
//				field.set(instance, new DataSourceTracker().fromString(s1));
//			} else if (field.getType() == Set.class) {
//				System.out.println("\tSet! " + field.getType());
//				String[] split = s1.split(",\n");
//
////								field.set(instance, Color.getColor(s1));
////								field.set(instance, Color.decode(s1));
//			} else if (field.getType() == List.class) {
//				System.out.println("\tList! " + field.getType());
////								field.set(instance, Color.getColor(s1));
////								field.set(instance, Color.decode(s1));
//			} else if (field.getType() == Color.class) {
////								field.set(instance, Color.getColor(s1));
////								field.set(instance, Color.decode(s1));
//			} else {
//				System.out.println("\tUNKNOWN TYPE, super type: " + field.getType().getSuperclass());
//				System.out.println("\t  getNestHost: " + field.getType().getNestHost());
//				System.out.println("\t  componentType: " + field.getType().componentType());
//				for (Class<?> interf : field.getType().getInterfaces()) {
//					System.out.println("\t\t" + interf.getSimpleName());
//				}

			}
//							field.set(instance, s1);

		} catch (Exception e) {
			System.out.println("Failed to parse [" + field.getName() + ", " + field.getType() + "]");
			e.printStackTrace();
		}
		field.setAccessible(false);
	}

	private static boolean notTransientNorStatic(Field field) {
		int modifiers = field.getModifiers();
		return !Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers);
	}
}
