package se.twilac.uiComponents;

import javax.swing.*;
import java.awt.*;

public class TwiScrollPane extends JScrollPane {
	private Component view;
	public TwiScrollPane(Component view) {
		super(view);
		this.view = view;
		getVerticalScrollBar().setUnitIncrement(16);
	}

	public TwiScrollPane setVIncrement(int increment) {
		getVerticalScrollBar().setUnitIncrement(increment);
		return this;
	}

	public TwiScrollPane setHIncrement(int increment) {
		getHorizontalScrollBar().setUnitIncrement(increment);
		return this;
	}

	public TwiScrollPane setVP(JViewport viewport) {
		super.setViewport(viewport);
		return this;
	}

	public TwiScrollPane setVpView(Component view) {
		this.view = view;
		super.setViewportView(view);
		return this;
	}

	public JScrollBar getVScrollBar() {
		return super.getVerticalScrollBar();
	}

	public JScrollBar getHScrollBar() {
		return super.getHorizontalScrollBar();
	}

	public TwiScrollPane setVScrollBar(JScrollBar verticalScrollBar) {
		super.setVerticalScrollBar(verticalScrollBar);
		return this;
	}

	public TwiScrollPane setHScrollBar(JScrollBar horizontalScrollBar) {
		super.setHorizontalScrollBar(horizontalScrollBar);
		return this;
	}

	public TwiScrollPane setVSBPol(int policy) {
		super.setVerticalScrollBarPolicy(policy);
		return this;
	}

	public TwiScrollPane setHSBPol(int policy) {
		super.setHorizontalScrollBarPolicy(policy);
		return this;
	}
}
