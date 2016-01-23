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
