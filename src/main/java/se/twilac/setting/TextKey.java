package se.twilac.setting;

import se.twilac.ProgramGlobals;

public enum TextKey {
	START_PASTING("Start Pasting"),
	ABORT_PASTING("Abort Pasting"),
	COPY_FROM_GAME("Start Copying From Game"),
	NONE("None"),
	EDIT("Edit"),
	;
	final String defaultTranslation;

	TextKey(String s) {
		defaultTranslation = s;
	}

	public String getDefaultTranslation() {
		return defaultTranslation;
	}

	public String getTranslation() {
		return ProgramGlobals.getTranslator().getText(this);
	}

	@Override
	public String toString() {
		return getTranslation();
	}
}
