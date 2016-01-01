package org.vaulttec.velocity.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
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

	protected Map fColorTable = new HashMap(10);

	/**
	 * Set default colors in given preference store.
	 */
	public static void initializeDefaults(IPreferenceStore aStore) {
		PreferenceConverter.setDefault(aStore,
							 IPreferencesConstants.COLOR_DEFAULT, RGB_DEFAULT);
		PreferenceConverter.setDefault(aStore,
							 IPreferencesConstants.COLOR_COMMENT, RGB_COMMENT);
		PreferenceConverter.setDefault(aStore,
					 IPreferencesConstants.COLOR_DOC_COMMENT, RGB_DOC_COMMENT);
		PreferenceConverter.setDefault(aStore,
						 IPreferencesConstants.COLOR_DIRECTIVE, RGB_DIRECTIVE);
		PreferenceConverter.setDefault(aStore,
							   IPreferencesConstants.COLOR_STRING, RGB_STRING);
		PreferenceConverter.setDefault(aStore,
						 IPreferencesConstants.COLOR_REFERENCE, RGB_REFERENCE);
		PreferenceConverter.setDefault(aStore,
								  IPreferencesConstants.COLOR_STRING_REFERENCE,
								  RGB_STRING_REFERENCE);
	}

	/**
	 * Returns specified color that is stored in the color table. If color not
	 * found in color table then a new instance is created from according
	 * preferences value and stored in color table.
	 */
	public Color getColor(String aName) {
		Color color = (Color)fColorTable.get(aName);
		if (color == null) {
			IPreferenceStore store =
							  VelocityPlugin.getDefault().getPreferenceStore();
			RGB rgb = PreferenceConverter.getColor(store,
								   IPreferencesConstants.PREFIX_COLOR + aName);
			if (rgb != null) {
				color = new Color(Display.getCurrent(), rgb);
			} else {
				color = Display.getCurrent().getSystemColor(
													SWT.COLOR_LIST_FOREGROUND);
				VelocityPlugin.logErrorMessage("Undefined color '" +
											   aName + "'");
			}
			fColorTable.put(aName, color);
		}
		return color;
	}

	/**
	 * Release all of the color resources held onto by the color provider.
	 */
	public void dispose() {
		Iterator colors = fColorTable.values().iterator();
		while (colors.hasNext()) {
			 ((Color)colors.next()).dispose();
		}
	}
}
