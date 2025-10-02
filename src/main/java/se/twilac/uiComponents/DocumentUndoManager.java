package se.twilac.uiComponents;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import java.awt.event.ActionEvent;

public class DocumentUndoManager extends UndoManager {
	private final JButton undoButton;
	private final JButton redoButton;
	private final AbstractAction undoAction;
	private final AbstractAction redoAction;

	public DocumentUndoManager() {
//		KeyBindingPrefs keyBindingPrefs = ProgramGlobals.getKeyBindingPrefs();
		undoAction = createUndoAction();
		undoAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Z"));
		redoAction = createRedoAction();
		redoAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Y"));
		undoButton = new JButton(undoAction);
		redoButton = new JButton(redoAction);
		update();
	}
	public DocumentUndoManager(boolean isTesting) {
		undoAction = createUndoAction();
		undoAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Z"));
		redoAction = createRedoAction();
		redoAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Y"));
		undoButton = new JButton(undoAction);
		redoButton = new JButton(redoAction);
		update();
	}

	public void update() {
		System.out.println("update UndoRedo");
		System.out.println("  can Redo: " + canRedo());
//		redoButton.setEnabled(canRedo());

//		System.out.println("isInProgress: " + isInProgress());
//		System.out.println("canUndo: " + canUndo());
//		System.out.println("editToBeUndone: " + editToBeUndone());
//		System.out.println("edits: " + this.edits);

		boolean canUndo = canUndo();
		System.out.println("  can Undo: " + canUndo);
//		undoButton.setEnabled(canUndo);
		System.out.println(" updated UndoRedo");

	}

	@Override
	public synchronized boolean addEdit(UndoableEdit anEdit) {
		boolean b = super.addEdit(anEdit);
		System.out.println("edit: " + anEdit.getClass());
		System.out.println("edit: " + anEdit);
		if (anEdit instanceof AbstractDocument.DefaultDocumentEvent event) {
			System.out.println("isSignificant: " + event.isSignificant());
			System.out.println("getDocument: " + event.getDocument());
			System.out.println("getOffset: " + event.getOffset());
			System.out.println("DefaultRootE: " + event.getDocument().getDefaultRootElement());
			System.out.println("event: " + event);
//			if (event.getDocument().getDefaultRootElement())

		}
		update();
		return b;
	}

	public JButton getUndoButton() {
		return undoButton;
	}

	public JButton getRedoButton() {
		return redoButton;
	}

	public AbstractAction getUndoAction() {
		return undoAction;
	}
	public AbstractAction getRedoAction() {
		return redoAction;
	}

	private AbstractAction createUndoAction() {
		return new AbstractAction("Undo") {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("preform undo");
				if (canUndo()) {
					undo();
					update();
				}
			}
		};
	}

	private AbstractAction createRedoAction() {
		return new AbstractAction("Redo") {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("preform redo");
				if (canRedo()) {
					redo();
					update();
				}
			}
		};
	}
}
