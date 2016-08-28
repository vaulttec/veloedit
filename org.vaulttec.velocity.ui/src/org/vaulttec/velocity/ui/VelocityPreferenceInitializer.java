package org.vaulttec.velocity.ui;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;

public class VelocityPreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = VelocityUIPlugin.getDefault().getPreferenceStore();

		store.setDefault(IVelocityPreferencesConstants.EDITOR_SHOW_SEGMENTS, false);

		/**
		 * Default editor colors
		 */
		PreferenceConverter.setDefault(store, IVelocityPreferencesConstants.COLOR_DEFAULT,
				IVelocityColorConstants.RGB_DEFAULT);
		PreferenceConverter.setDefault(store, IVelocityPreferencesConstants.COLOR_COMMENT,
				IVelocityColorConstants.RGB_COMMENT);
		PreferenceConverter.setDefault(store, IVelocityPreferencesConstants.COLOR_DOC_COMMENT,
				IVelocityColorConstants.RGB_DOC_COMMENT);
		PreferenceConverter.setDefault(store, IVelocityPreferencesConstants.COLOR_DIRECTIVE,
				IVelocityColorConstants.RGB_DIRECTIVE);
		PreferenceConverter.setDefault(store, IVelocityPreferencesConstants.COLOR_STRING,
				IVelocityColorConstants.RGB_STRING);
		PreferenceConverter.setDefault(store, IVelocityPreferencesConstants.COLOR_REFERENCE,
				IVelocityColorConstants.RGB_REFERENCE);
		PreferenceConverter.setDefault(store, IVelocityPreferencesConstants.COLOR_STRING_REFERENCE,
				IVelocityColorConstants.RGB_STRING_REFERENCE);
	}

}
