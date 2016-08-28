package org.vaulttec.velocity.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Provider for SWT color objects used in the Velocity UI.
 */
public class VelocityColorManager implements ISharedTextColors, IVelocityColorConstants {

	protected final Map<RGB, Color> colorTable = new HashMap<>(10);
	private static VelocityColorManager colorManager;

	public static VelocityColorManager getDefault() {
		if (colorManager == null) {
			colorManager = new VelocityColorManager();
		}
		return colorManager;
	}

	@Override
	public Color getColor(RGB rgb) {
		Color color = colorTable.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			colorTable.put(rgb, color);
		}
		return color;
	}

	@Override
	public void dispose() {
		for (Color color : colorTable.values()) {
			color.dispose();
		}
	}

}
