package se.twilac.signUI;

import net.miginfocom.swing.MigLayout;
import se.twilac.dataContainers.Sign;
import se.twilac.uiComponents.uiFactories.Button;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;

public class SignRender extends JPanel {
	private Sign sign;
	private JPanel signPanel;
	JPanel blankSignInnerPanel;

	public SignRender() {
		super(new MigLayout(""));

		blankSignInnerPanel = getSignInnerPanel(new Sign(""));
		blankSignInnerPanel.setVisible(false);
		signPanel = new JPanel(new MigLayout("fill, wrap 1, ins 0"));
		signPanel.add(blankSignInnerPanel);
		add(signPanel);
//		add(Button.create("runOnFocusLost", e -> pasteHandler.setPasteOnFocusLost("SIGN", texts)), "wrap");
//		add(Button.create("cancelRun", e -> pasteHandler.removeListener()));
		add(Button.create("runOnFocusLost"), "wrap");
		add(Button.create("cancelRun"));
	}
	

	public SignRender setSign(Sign sign) {
		this.sign = sign;
		signPanel.removeAll();
		if (sign != null) {
			JPanel signInnerPanel = getSignInnerPanel(sign);
			signPanel.add(signInnerPanel);
		} else {
			signPanel.add(blankSignInnerPanel);
		}
		repaint();
		return this;
	}

	private JPanel getSignInnerPanel(Sign sign) {
		JPanel signInnerPanel = new JPanel(new MigLayout("fill, wrap 1, ins 0"));
		String[] texts1 = sign.getTexts();
		for (int i = 0; i < texts1.length; i++) {
			signInnerPanel.add(getTextField(texts1[i], sign.isHanging(), i));

		}
		return signInnerPanel;
	}


	public Sign getSign() {
		return sign;
	}

	private JPanel getTextField(String text, boolean isHanging, int i) {
		int colums = isHanging ? 7 : 14;

		JPanel jPanel = new JPanel(new MigLayout("ins 0"));

		JTextField textField = new JTextField(text, colums);
		textField.setHorizontalAlignment(JTextField.CENTER);
		textField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				System.out.println("" + e);
				textWidth(textField, textField.getText());
				sign.setText(i, textField.getText());
//				texts[i] = textField.getText();
			}
		});
		jPanel.add(textField);
//		jPanel.add(Button.create("Copy", e -> pasteHandler.textToPastebin(textField.getText())));
		jPanel.add(Button.create("Copy"));
		return jPanel;
	}

	private JPanel getTextField(boolean isHanging, int i) {
		int colums = isHanging ? 7 : 14;

		JPanel jPanel = new JPanel(new MigLayout("ins 0"));

		JTextField textField = new JTextField(colums);
		textField.setHorizontalAlignment(JTextField.CENTER);
		textField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				System.out.println("" + e);
				textWidth(textField, textField.getText());
				sign.setText(i, textField.getText());
//				texts[i] = textField.getText();
			}
		});
		jPanel.add(textField);
//		jPanel.add(Button.create("Copy", e -> pasteHandler.textToPastebin(textField.getText())));
		jPanel.add(Button.create("Copy"));
		return jPanel;
	}

	private void textWidth(JTextField textField, String s) {
		Font font = textField.getFont();
		FontMetrics fontMetrics = textField.getFontMetrics(font);

//		System.out.println("getLineMetrics: " + fontMetrics.getLineMetrics(s, textField.getGraphics()));
		System.out.println("\"" + s + "\"  " + s.length());
		System.out.println(" stringWidth: " + fontMetrics.stringWidth(s));
//		System.out.println("stringWidth: " + fontMetrics.stringWidth(s));
//		font.getStringBounds(s, null);
	}
}
