/*******************************************************************************
 * Copyright (c) 2016 Torsten Juergeleit.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 * 
 * Contributors:
 *     Torsten Juergeleit - initial API and implementation
 *******************************************************************************/
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
