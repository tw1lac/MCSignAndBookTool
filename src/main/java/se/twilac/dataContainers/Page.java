package se.twilac.dataContainers;

/**
 * Basically StringAsObject
 */
public class Page {
	private String text;
	public Page() {
		this("");
	}
	public Page(String text) {
		this.text = text;
	}

	public Page setText(String text) {
		this.text = text;
		return this;
	}

	public String getText() {
		return text;
	}

	public Page deepCopy() {
		return new Page(text);
	}
	@Override
	public String toString() {
		return text;
	}
}
