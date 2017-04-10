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
package org.vaulttec.velocity.core;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

public class VelocityCorePreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IEclipsePreferences node = DefaultScope.INSTANCE.getNode(VelocityCorePlugin.PLUGIN_ID);
		node.put(IPreferencesConstants.VELOCITY_COUNTER_NAME, "velocityCount");
		node.put(IPreferencesConstants.VELOCITY_USER_DIRECTIVES, "");
		node.put(IPreferencesConstants.LIBRARY_PATH, "");
		node.put(IPreferencesConstants.LIBRARY_LIST, "");
	}

}
