package se.twilac;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class RobotHandler {
	private final Robot robot;
	private int betweenKeysDelay = 200;
	private int beforeSeqDelay = 500;
	private int afterSeqDelay = 0;

	RobotHandler() {
		try {
			this.robot = new Robot();
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}
	}


	private void robotPaste() {
		int[] paste = new int[] {KeyEvent.VK_CONTROL, KeyEvent.VK_V};
		robotPressUnpress(paste);
	}

	private void robotUp() {
		int[] down = new int[] {KeyEvent.VK_UP};
		robotPressUnpress(down);
	}

	private void robotDown() {
		int[] down = new int[] {KeyEvent.VK_DOWN};
		robotPressUnpress(down);
	}

	private void robotPrevPage() {
		int[] down = new int[] {KeyEvent.VK_PAGE_UP};
		robotPressUnpress(down);
	}

	private void robotNextPage() {
		int[] down = new int[] {KeyEvent.VK_PAGE_DOWN};
		robotPressUnpress(down);
	}




	public static int[] getPaste() {
		return new int[] {KeyEvent.VK_CONTROL, KeyEvent.VK_V};
	}

	public static int[] getCopy() {
		return new int[] {KeyEvent.VK_CONTROL, KeyEvent.VK_C};
	}

	public static int[] getSelectAll() {
		return new int[] {KeyEvent.VK_CONTROL, KeyEvent.VK_A};
	}

	public static int[] getUp() {
		return new int[] {KeyEvent.VK_UP};
	}

	public static int[] getDown() {
		return new int[] {KeyEvent.VK_DOWN};
	}

	public static int[] getPrevPage() {
		return new int[] {KeyEvent.VK_PAGE_UP};
	}

	public static int[] getNextPage() {
		return new int[] {KeyEvent.VK_PAGE_DOWN};
	}





	public void robotDoKeyPressSequences(List<int[]> keySequences) {
		robot.delay(beforeSeqDelay);
		for (int[] keySeq : keySequences) {
			robotPressUnpress(keySeq);
		}
		robot.delay(afterSeqDelay);
	}

	public void robotDoKeyPressSequences(int[]... keySequences) {
		robot.delay(beforeSeqDelay);
		for (int[] keySeq : keySequences) {
			robotPressUnpress(keySeq);
		}
		robot.delay(afterSeqDelay);
	}
	public void robotDoKeyPressSequence(int[] keySeq) {
		robot.delay(beforeSeqDelay);
		robotPressUnpress(keySeq);
		robot.delay(afterSeqDelay);
	}


	private void robotPressUnpress(int... keys) {
		for (int i = 0; i < keys.length; i++) {
			robot.keyPress(keys[i]);
			robot.delay(betweenKeysDelay);
		}
		for (int i = keys.length-1; 0 <= i; i--) {
			robot.keyRelease(keys[i]);
			robot.delay(betweenKeysDelay);
		}
	}
}
