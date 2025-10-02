package se.twilac;

import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		showFrame();
	}

	private static void showFrame() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
		         UnsupportedLookAndFeelException e) {
			throw new RuntimeException(e);
		}
		System.out.println("Hello world!");
		JFrame frame = new JFrame("MC Sign Creator");
		frame.setJMenuBar(new MenuBar(frame));
		MainTabbedPane tabbedPane = new MainTabbedPane();
		frame.setContentPane(tabbedPane);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(750, 400);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.pack();
	}
}