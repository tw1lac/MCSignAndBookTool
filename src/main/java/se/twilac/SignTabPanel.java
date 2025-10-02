package se.twilac;

import net.miginfocom.swing.MigLayout;
import se.twilac.dataContainers.Sign;
import se.twilac.signUI.SignRender;
import se.twilac.uiComponents.SearchListPanel;
import se.twilac.uiComponents.TwiListRenderer;
import se.twilac.uiComponents.uiFactories.Button;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SignTabPanel extends JPanel {

	private final List<Sign> signList;
	private final SearchListPanel<Sign> signSearchList;
	private final SignRender signPanel;
	public SignTabPanel() {
		super(new MigLayout("fill", "[][grow]", "[grow][grow]"));
		WriteBook.loadBookLibs();
		signList = ProgramGlobals.getSignList();
		signSearchList = new SearchListPanel<>("", signList, this::search).setSelectionConsumer(this::signSelected);
		signSearchList.setRenderer(new TwiListRenderer<>(Sign::getName));
		signPanel = new SignRender();

		add(signSearchList, "growy");
		add(signPanel, "growx, growy, wrap");
		add(Button.create("New Sign", e -> addNewSign()));
		addDummySigns();
//		((TwiListModel<Sign>)signSearchList.getSearchList().getModel()).se
	}

	private void signSelected(Sign sign) {
		ProgramGlobals.setCurrentSign(sign);
		signPanel.setSign(sign);
		System.out.println("Sign Selected! " + sign.getName());
		repaint();
	}

	public Sign getSelectedSign() {
		return signPanel.getSign();
	}

	public List<String> getStrings() {
		System.out.println("isValid: " + isValid() + ", hasBook: " + (signPanel.getSign() != null));
		if (isValid() && signPanel.getSign() != null) {
			return Arrays.asList(signPanel.getSign().getTexts());
		}
		return new ArrayList<>();
	}

	public String[] getPasteSource() {
		if (isVisible() && signPanel.getSign() != null) {
			return signPanel.getSign().getTexts();
		}
		return new String[] {"", "", "", ""};
	}

	private void addNewSign() {
		Sign sign = new Sign("Unnamed");
		signList.add(sign);
		signPanel.setSign(sign);
	}

	private void addDummySigns() {
		Sign sign = getDummySign("TempSign1", "P1\nTempText\ntempText");
		signList.add(sign);
		Sign sign2 = getDummySign("TempSign2", "P2\nTempText\ntempText");
		signList.add(sign2);
//		bookPanel.setSign(sign);
	}

	private Sign getDummySign(String name, String text) {
		return new Sign(name).setText(text);
	}

	private boolean search(Sign sign, String filter) {
		if (sign.getName().toUpperCase(Locale.ROOT).contains(filter.toUpperCase(Locale.ROOT))) {
			return true;
		} else {
			return false;
		}
	}
}
