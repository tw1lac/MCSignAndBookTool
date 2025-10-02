package se.twilac.dataContainers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Book {
	private String name;
	private File file;
	private final List<Page> pages;

	public Book() {
		this("Unnamed");
	}
	public Book(String name) {
		this.name = name;
		this.pages  = new ArrayList<>();
	}
	public Book(String name, File file) {
		this.name = name;
		this.file = file;
		this.pages  = new ArrayList<>();
	}
	public Book(File file) {
		this.name = file.getName();
		this.file = file;
		this.pages  = new ArrayList<>();
	}

	public Book setName(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}

	public Book setFile(File file) {
		this.file = file;
		return this;
	}

	public File getFile() {
		return file;
	}

	public String getDisplayName() {
		return name + " (" + pages.size() + " pages)";
	}

	public Book addPage() {
		return addPage("");
	}
	public Book addPage(String page) {
		return addPage(new Page(page));
	}
	public Book addPage(Page page) {
		pages.add(page);
		return this;
	}

	public Book addPage(int i) {
		return addPage(i, "");
	}

	public Book addPage(int i, String page) {
		pages.add(i, new Page(page));
		return this;
	}

	public Book addPage(int i, Page page) {
		System.out.println("add page: " + i);
		pages.add(i, page);
		return this;
	}

	public Page remove(int i) {
		System.out.println("rem: " + i);
		return pages.remove(i);
	}

	public Book removePage(int i) {
		System.out.println("rem page: " + i);
		pages.remove(i);
		return this;
	}

	public Page getPage(int i) {
		return pages.get(i);
	}

	public Book movePage(int i, int move) {
		Page page = pages.remove(i);
		int newI = Math.max(0, Math.min(pages.size(), i + move));
		if (newI <= pages.size()) {
			pages.add(newI, page);
		}
//		if (0 <= newI && newI <= pages.size()) {
//			pages.add(newI, page);
//		}
		return this;
	}

	public List<Page> getPages() {
		return pages;
	}

	public int getNumPages() {
		return pages.size();
	}

	public List<String> getPagesAsStrings() {
		return pages.stream().map(Page::toString).toList();
	}
}
