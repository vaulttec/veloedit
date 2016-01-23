package org.vaulttec.velocity.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Provider for colors used in the Velocity UI.
 */
public class VelocityColorProvider implements IColorConstants {

	// Default colors
	private static final RGB RGB_DEFAULT = new RGB(0, 0, 0);
	private static final RGB RGB_COMMENT = new RGB(63, 127, 95);
	private static final RGB RGB_DOC_COMMENT = new RGB(63, 150, 192);
	private static final RGB RGB_DIRECTIVE = new RGB(127, 0, 85);
	private static final RGB RGB_STRING = new RGB(42, 0, 255);
	private static final RGB RGB_REFERENCE = new RGB(220, 0, 0);
	private static final RGB RGB_STRING_REFERENCE = new RGB(250, 10, 240);

	protected final Map<String, Color> colorTable = new HashMap<>(10);

	/**
	 * Set default colors in given preference node.
	 */
	public static void initializeDefaults(IEclipsePreferences node) {
		node.put(IPreferencesConstants.COLOR_DEFAULT, StringConverter.asString(RGB_DEFAULT));
		node.put(IPreferencesConstants.COLOR_COMMENT, StringConverter.asString(RGB_COMMENT));
		node.put(IPreferencesConstants.COLOR_DOC_COMMENT, StringConverter.asString(RGB_DOC_COMMENT));
		node.put(IPreferencesConstants.COLOR_DIRECTIVE, StringConverter.asString(RGB_DIRECTIVE));
		node.put(IPreferencesConstants.COLOR_STRING, StringConverter.asString(RGB_STRING));
		node.put(IPreferencesConstants.COLOR_REFERENCE, StringConverter.asString(RGB_REFERENCE));
		node.put(IPreferencesConstants.COLOR_STRING_REFERENCE, StringConverter.asString(RGB_STRING_REFERENCE));
	}

	/**
	 * Returns specified color that is stored in the color table. If color not
	 * found in color table then a new instance is created from according
	 * preferences value and stored in color table.
	 */
	public Color getColor(String name) {
		Color color = colorTable.get(name);
		if (color == null) {
			IPreferenceStore store = VelocityPlugin.getDefault().getPreferenceStore();
			RGB rgb = PreferenceConverter.getColor(store, IPreferencesConstants.PREFIX_COLOR + name);
			if (rgb != null) {
				color = new Color(Display.getCurrent(), rgb);
			} else {
				color = Display.getCurrent().getSystemColor(SWT.COLOR_LIST_FOREGROUND);
				VelocityPlugin.logErrorMessage("Undefined color '" + name + "'");
			}
			colorTable.put(name, color);
		}
		return color;
	}

	/**
	 * Release all of the color resources held onto by the color provider.
	 */
	public void dispose() {
		Iterator<Color> colors = colorTable.values().iterator();
		while (colors.hasNext()) {
			colors.next().dispose();
		}
	}
}
