package se.twilac.uiComponents;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;
import java.util.function.Function;

public class TwiListRenderer<E> extends JLabel implements ListCellRenderer<E> {
	private Function<E, String> displayStringFunction;
	private Function<E, Icon> iconFunction;

	public TwiListRenderer() {
		this(null, null);
	}

	public TwiListRenderer(Function<E, String> displayStringFunction) {
		this(displayStringFunction, null);
	}

	public TwiListRenderer(Function<E, String> displayStringFunction, Function<E, Icon> iconFunction) {
		super();
		this.displayStringFunction = displayStringFunction;
		this.iconFunction = iconFunction;
		setOpaque(true);
		setBorder(getNoFocusBorder());
		setName("List.cellRenderer");
	}

	public TwiListRenderer<E> setDisplayStringFunction(Function<E, String> displayStringFunction) {
		this.displayStringFunction = displayStringFunction;
		return this;
	}

	public TwiListRenderer<E> setIconFunction(Function<E, Icon> iconFunction) {
		this.iconFunction = iconFunction;
		return this;
	}

	public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
		setComponentOrientation(list.getComponentOrientation());

		boolean isDropLocation = isDropLocation(index, list.getDropLocation());

		setBackground(getBgColor(list, isSelected, isDropLocation));
		setForeground(getFgColor(list, isSelected, isDropLocation));

		setIcon(getValueIcon(value));
		setText(getValueString(value));

		setEnabled(list.isEnabled());
		setFont(list.getFont());
		Border border = getBorder(isSelected || isDropLocation, cellHasFocus);
		setBorder(border);

		return this;
	}

	protected Icon getValueIcon(E value) {
		if (iconFunction != null) {
			return iconFunction.apply(value);
		} else if (displayStringFunction == null && value instanceof Icon) {
			return (Icon) value;
		}
		return null;
	}

	protected String getValueString(E value) {
		if (displayStringFunction != null) {
			return displayStringFunction.apply(value);
		} else if (iconFunction == null && value instanceof Icon) {
			return "";
		}
		return (value == null) ? "NULL" : value.toString();
	}

	protected Color getBgColor(JList<? extends E> list, boolean isSelected, boolean isDropLocation) {
		if (isDropLocation) {
			Color bg = UIManager.getColor("List.dropCellBackground", this.getLocale());
			return bg == null ? list.getSelectionBackground() : bg;
		}
		return isSelected ? list.getSelectionBackground() : list.getBackground();
	}

	protected Color getFgColor(JList<? extends E> list, boolean isSelected, boolean isDropLocation) {
		if (isDropLocation) {
			Color fg = UIManager.getColor("List.dropCellForeground", this.getLocale());
			return fg == null ? list.getSelectionForeground() : fg;
		}
		return isSelected ? list.getSelectionForeground() : list.getForeground();
	}

	protected static boolean isDropLocation(int index, JList.DropLocation dropLocation) {
		return dropLocation != null && !dropLocation.isInsert() && dropLocation.getIndex() == index;
	}

	protected static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
	protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;

	protected Border getBorder(boolean isSelected, boolean cellHasFocus) {
		Border border = null;
		if (cellHasFocus) {
			if (isSelected) {
				border = UIManager.getBorder("List.focusSelectedCellHighlightBorder", this.getLocale());
			}
			if (border == null) {
				border = UIManager.getBorder("List.focusCellHighlightBorder", this.getLocale());
			}
		} else {
			border = getNoFocusBorder();
		}
		return border;
	}


	protected Border getNoFocusBorder() {
		Border border = UIManager.getBorder("List.cellNoFocusBorder", this.getLocale());
		if (border == null || noFocusBorder != null && noFocusBorder != DEFAULT_NO_FOCUS_BORDER) {
			border = noFocusBorder;
		}
		return border;
	}

	@Override
	public boolean isOpaque() {
		Color back = getBackground();
		Component p = getParent();
		if (p != null) {
			p = p.getParent();
		}
		// p should now be the JList.
		boolean colorMatch = (back != null) && (p != null)
				&& back.equals(p.getBackground())
				&& p.isOpaque();
		return !colorMatch && super.isOpaque();
	}

	@Override
	public void validate() {}
	@Override
	public void invalidate() {}
	@Override
	public void repaint() {}
	@Override
	public void revalidate() {}
	@Override
	public void repaint(long tm, int x, int y, int width, int height) {}

	@Override
	public void repaint(Rectangle r) {}

	@Override
	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		// Strings get interned...
		if (propertyName == "text"
				|| ((isScaleChanged(propertyName, oldValue, newValue)
						|| propertyName == "font"
						|| propertyName == "foreground")
					&& oldValue != newValue
					&& getClientProperty(javax.swing.plaf.basic.BasicHTML.propertyKey) != null)) {

			super.firePropertyChange(propertyName, oldValue, newValue);
		}
	}
	@Override
	public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {}
	@Override
	public void firePropertyChange(String propertyName, char oldValue, char newValue) {}
	@Override
	public void firePropertyChange(String propertyName, short oldValue, short newValue) {}
	@Override
	public void firePropertyChange(String propertyName, int oldValue, int newValue) {}
	@Override
	public void firePropertyChange(String propertyName, long oldValue, long newValue) {}
	@Override
	public void firePropertyChange(String propertyName, float oldValue, float newValue) {}
	@Override
	public void firePropertyChange(String propertyName, double oldValue, double newValue) {}
	@Override
	public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}

	public static boolean isScaleChanged(final String name,
	                                     final Object oldValue,
	                                     final Object newValue) {
		if (oldValue == newValue || !"graphicsConfiguration".equals(name)) {
			return false;
		}
		var newGC = (GraphicsConfiguration) oldValue;
		var oldGC = (GraphicsConfiguration) newValue;
		var newTx = newGC != null ? newGC.getDefaultTransform() : null;
		var oldTx = oldGC != null ? oldGC.getDefaultTransform() : null;
		return !Objects.equals(newTx, oldTx);
	}

}
