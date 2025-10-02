package se.twilac.dataContainers;

import java.io.File;

public class Sign {
	private String name;
	private File file;
	private String[] texts;
	private boolean isHanging;

	public Sign(String name) {
		this.name = name;
		texts = new String[] {"", "", "", ""};
	}

	public Sign setName(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}

	public Sign setFile(File file) {
		this.file = file;
		return this;
	}

	public File getFile() {
		return file;
	}

	public String[] getTexts() {
		return texts;
	}

	public Sign setTexts(String[] texts) {
		this.texts = texts;
		return this;
	}

	public Sign setText(int row, String text) {
		if (0 <= row && row < 4) {
			texts[row] = text;
		}
		return this;
	}

	public Sign setText(String text) {
		String[] strings = text.split("\n");
		for (int i = 0; i < strings.length && i < 4; i++) {
			texts[i] = strings[i];
		}
		return this;
	}

	public boolean isHanging() {
		return isHanging;
	}

	public Sign setHanging(boolean hanging) {
		isHanging = hanging;
		return this;
	}
}
