package se.twilac;

import se.twilac.dataContainers.Book;
import se.twilac.dataContainers.Sign;
import se.twilac.setting.SaveProfile;
import se.twilac.setting.Translator;
import se.twilac.uiComponents.TwiFileChooser;

import java.util.ArrayList;
import java.util.List;

public class ProgramGlobals {
	private static SaveProfile saveProfile;
	private static TwiFileChooser fileChooser;
	private static final List<Book> bookList = new ArrayList<>();
	private static final List<Sign> signList = new ArrayList<>();
	private static final Translator translator = new Translator();
	private static Book currentBook;
	private static Sign currentSign;

	public static SaveProfile getSaveProfile() {
		if (saveProfile == null) {
			saveProfile = new SaveProfile().readSettings();
		}
		return saveProfile;
	}

	public static TwiFileChooser getFileChooser() {
		if (fileChooser == null) {
			fileChooser = new TwiFileChooser(getSaveProfile());
		}
		return fileChooser;
	}

	public static List<Book> getBookList() {
		return bookList;
	}

	public static List<Sign> getSignList() {
		return signList;
	}


	public static Book getCurrentBook() {
		return currentBook;
	}

	public static void setCurrentBook(Book currentBook) {
		ProgramGlobals.currentBook = currentBook;
	}

	public static Sign getCurrentSign() {
		return currentSign;
	}

	public static void setCurrentSign(Sign currentSign) {
		ProgramGlobals.currentSign = currentSign;
	}
	public static Translator getTranslator() {
		return translator;
	}
}
