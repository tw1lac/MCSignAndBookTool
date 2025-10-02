package se.twilac;

import se.twilac.dataContainers.Book;
import se.twilac.dataContainers.Page;
import se.twilac.uiComponents.TwiFileChooser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class WriteBook {
	private static final String utf8Bom = String.valueOf(new char[] {0xEF, 0xBB, 0xBF});

	public static void loadBookLibs() {
		for (File libDir : ProgramGlobals.getSaveProfile().getBookLibC()) {
//			loadFromDir(libDir, new FileFilter() {
//				@Override
//				public boolean accept(File pathname) {
//					return false;
//				}
//			});
			loadFromDir(libDir, (dir, name) -> name.endsWith("txt"));
		}
	}

	public static void loadFromDir(File dir, FileFilter fileFilter) {
		if (dir != null && dir.listFiles() != null) {
			for (File file : dir.listFiles(fileFilter)) {
				if (file != null) {
					Book readBook = getReadBook(file);
					if (readBook != null) {
						readBook.setFile(file);
						ProgramGlobals.getBookList().add(readBook);
					}
				}
			}
		}
	}

	public static void loadFromDir(File dir, FilenameFilter fileFilter) {
		if (dir != null && dir.listFiles() != null) {
			for (File file : dir.listFiles(fileFilter)) {
				if (file != null) {
					Book readBook = getReadBook(file);
					if (readBook != null) {
						readBook.setFile(file);
						ProgramGlobals.getBookList().add(readBook);
					}
				}
			}
		}
	}


	public static void openBook() {
		File file = ProgramGlobals.getFileChooser().openFile(0);
		if (file != null) {
			Book readBook = getReadBook(file);
			if (readBook != null) {
				readBook.setFile(file);
				ProgramGlobals.getBookList().add(readBook);
			}
		}
	}

	protected static Book getReadBook(File file) {
		try (FileInputStream in = new FileInputStream(file)) {
			return readStream2(in);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	protected static Book readStream2(InputStream in) {

		try (BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
			char[] tempChar = new char[3];
			r.mark(4);
			if (r.read(tempChar, 0, 3) != 3 || !utf8Bom.equals(new String(tempChar))) {
				r.reset();
			}
			String pageSeparator = null;
			String bookName = "Unknown";
			r.mark(100);
			while (r.ready()) {
				String s = r.readLine();
//				if (s.startsWith("§>PageSeparator:")) {
				if (s.startsWith("\u00a7>PageSeparator:")) {
//					System.out.println("PageSeparator: " + s);
					pageSeparator = s.split(":", 2)[1].strip();
//					System.out.println("  Separator: " + pageSeparator);
					r.mark(100);
//				} else if (s.startsWith("§>BookName:")) {
				} else if (s.startsWith("\u00a7>BookName:")) {
//					System.out.println("BookName: " + s);
					bookName = s.split(":", 2)[1].strip();
//					System.out.println("  NAme: " + bookName);
					r.mark(100);
				} else {
//					System.out.println("Other: " + s);
					r.reset();
					break;
				}
			}
			Book book = new Book(bookName);
			StringBuilder sb = null;
			while (r.ready()) {
				String s = r.readLine();
				if (pageSeparator != null && s.matches(pageSeparator)) {
//					System.out.println("PageSeparator: " + s);
					if (sb != null) {
//						System.out.println("  Page done!");
						book.addPage(new Page(sb.toString()));
					}
					sb = null;
				} else if (sb == null) {
//					System.out.println("New Page: " + s);
					sb = new StringBuilder();
					sb.append(s);
				} else {
//					System.out.println(" Add Row: " + s);
					sb.append("\n").append(s);
				}

			}
			if (sb != null) {
//				System.out.println("  Page done!");
				book.addPage(new Page(sb.toString()));
			}
			return book;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	protected String getReadFile(File file) {
		try (FileInputStream in = new FileInputStream(file)) {
			return readStream(in);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}
	protected String readStream(InputStream in) {
		StringBuilder stringBuilder = new StringBuilder();

		try (BufferedReader r = new BufferedReader(new InputStreamReader(in))) {
			char[] tempChar = new char[3];
			r.mark(4);
			if (r.read(tempChar, 0, 3) != 3 || !utf8Bom.equals(new String(tempChar))) {
				r.reset();
			}
			r.lines().forEach(l -> stringBuilder.append(l).append("\n"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return stringBuilder.toString();
	}

	public static void saveBook(Book book) {
		if (book != null) {
			TwiFileChooser fileChooser = ProgramGlobals.getFileChooser();
			if (book.getFile() != null) {
				fileChooser.setSelectedFile(book.getFile());
			}
			File file = fileChooser.saveFile(0);

			if (file != null) {
				book.setFile(file);
				writeBook(file, book);
			}
		}
	}

	public static void writeBook(File file, Book book) {
		File tempFile = getTempFile(file);
		try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
			System.out.println("Writing file");
//			outputStream.write(utf8Bom.getBytes(StandardCharsets.UTF_8));
//			outputStream.write((byte)'§');
			outputStream.write((byte)'\u00a7');
			outputStream.write(">PageSeparetor:<Page>\n".getBytes(StandardCharsets.UTF_8));
			outputStream.write((byte)'\u00a7');
//			outputStream.write((byte)'§');
			outputStream.write((">BookName:" + book.getName() + "\n").getBytes(StandardCharsets.UTF_8));

			outputStream.write(book.getPage(0).getText().getBytes(StandardCharsets.UTF_8));
			for (int i = 0; i < book.getNumPages(); i++) {
				outputStream.write("\n<Page>\n".getBytes(StandardCharsets.UTF_8));
				outputStream.write(book.getPage(i).getText().getBytes(StandardCharsets.UTF_8));
			}
//			for (Page page : book.getPages()) {
//				outputStream.write(page.getText().getBytes(StandardCharsets.UTF_8));
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			removeTempFile(file, tempFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}



	private static void removeTempFile(File file, File tempFile) throws IOException {
		if (tempFile != file && tempFile.exists() && 8 < Files.size(tempFile.toPath())) {
			System.out.println("validFile: " + file);
			System.out.println("tempFile:  " + tempFile + ", size: " + Files.size(tempFile.toPath()));
			boolean delete = file.delete();
			boolean b = tempFile.renameTo(file);
			System.out.println(" deleted: " + delete);
			System.out.println(" renamed: " + b);
		}
	}

	private static File getTempFile(File file) {
		if (file.exists()) {
			File tempFile = null;
			for (int i = 0; i < 100; i++) {
				tempFile = new File(file.getPath() + "BOOK_TEMP" + i);
				if (!tempFile.exists()) {
					return tempFile;
				}
			}
			return tempFile;
		} else {
			return file;
		}
	}
}
