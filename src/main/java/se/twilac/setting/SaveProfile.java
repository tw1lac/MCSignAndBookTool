package se.twilac.setting;

import java.io.File;
import java.util.Collection;
import java.util.Objects;

public class SaveProfile {
	private static final String files_fn = "MCSignAndBookTool_file_paths.txt";
	private static final String prefs_fn = "MCSignAndBookTool_settings.txt";
	private String lastDirectory;
	private FileListTracker bookLibDirs;
	private FileListTracker recent;

	private FileListTracker favoriteDirectories;

	private Boolean keyListenerActive = true;


	public SaveProfile readSettings() {
		File settingsDirPath = getSettingsDirPath();
		File file = new File(settingsDirPath, files_fn);
		SettingsLoader.setFromFile(this, file);
		return this;
	}

	public void writeSettings() {
		File profileDir = getSettingsDirPath();
		if (profileDir.canWrite() || profileDir.mkdirs()) {
			SettingsLoader.saveToFile(this, new File(profileDir, files_fn));
		}
	}

	public static File getSettingsDirPath() {
		if (System.getProperty("os.name").toLowerCase().contains("win")) {
			return new File(System.getProperty("user.home") + "\\AppData\\Roaming\\MCSignAndBookTool");
		} else {
			return new File(System.getProperty("user.home") + "/.mcsignandbooktool");
		}
	}


	public String getPath() {
		return lastDirectory;
	}

	public void setPath(String path) {
		setPath(path, true);
	}
	public void setPath(String path, boolean save) {
		if (!Objects.equals(lastDirectory, path)) {
			lastDirectory = path;
			writeSettings(save);
		}
	}

	public void clearRecent() {
		clearRecent(true);
	}
	public void clearRecent(boolean save) {
		getRecent().clear();
		writeSettings(save);
	}

	public FileListTracker getRecent() {
		if (recent == null) {
			recent = new FileListTracker();
		}
		return recent;
	}

	public void addRecent(String fp) {
		addRecent(fp, true);
	}
	public void addRecent(String fp, boolean save) {
		getRecent().remove(fp);
		getRecent().add(fp);
		if (15 < recent.size()) {
			recent.removeFirst();
		}
		writeSettings(save);
	}

	public void addRecentSetPath(File file) {
		addRecentSetPath(file, true);
	}
	public void addRecentSetPath(File file, boolean save) {
		lastDirectory = file.getParent();
		addRecent(file.getPath(), save);
	}

	public void removeFromRecent(String fp) {
		removeFromRecent(fp, true);
	}
	public void removeFromRecent(String fp, boolean save) {
		if (getRecent().remove(fp)) {
			writeSettings(save);
		}
	}

	public FileListTracker getFavorites() {
		if (favoriteDirectories == null) {
			favoriteDirectories = new FileListTracker();
		}
		return favoriteDirectories;
	}

	public boolean addFavorite(File fp) {
		return addFavorite(fp, true);
	}
	public boolean addFavorite(File fp, boolean save) {
		if (getFavorites().add(fp)) {
			getFavorites().sort();
			writeSettings(save);
			return true;
		}
		return false;
	}

	public boolean removeFromFavorite(File fp) {
		return removeFromFavorite(fp, true);
	}
	public boolean removeFromFavorite(File fp, boolean save) {
		if (getFavorites().remove(fp)) {
			writeSettings(save);
			return true;
		}
		return false;
	}



	public FileListTracker getBookLibDirs() {
		if (bookLibDirs == null) {
			bookLibDirs = new FileListTracker();
		}
		return bookLibDirs;
	}

	public boolean addBookLibDir(File fp) {
		return addBookLibDir(fp, true);
	}
	public boolean addBookLibDir(File fp, boolean save) {
		if (getBookLibDirs().add(fp)) {
			getBookLibDirs().sort();
			writeSettings(save);
			return true;
		}
		return false;
	}

	public boolean removeFromBookLibDir(File fp) {
		return removeFromBookLibDir(fp, true);
	}
	public boolean removeFromBookLibDir(File fp, boolean save) {
		if (getBookLibDirs().remove(fp)) {
			writeSettings(save);
			return true;
		}
		return false;
	}

	private void writeSettings(boolean save) {
		if (save) {
			writeSettings();
		}
	}

	public Collection<File> getBookLibC() {
		return getBookLibDirs().getFiles();
	}

	public Collection<File> getFavoritesC() {
		return getFavorites().getFiles();
	}


	public boolean isKeyListenerActive() {
		return keyListenerActive;
	}

	public SaveProfile setKeyListenerActive(boolean keyListenerActive) {
		this.keyListenerActive = keyListenerActive;
		return this;
	}
}
