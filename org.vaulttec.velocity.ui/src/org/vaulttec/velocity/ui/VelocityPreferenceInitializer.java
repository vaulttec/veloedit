package org.vaulttec.velocity.ui;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.resource.StringConverter;

public class VelocityPreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IEclipsePreferences node = DefaultScope.INSTANCE.getNode(VelocityPlugin.PLUGIN_ID);
		node.put(IPreferencesConstants.EDITOR_SHOW_SEGMENTS, StringConverter.asString(false));

		VelocityColorProvider.initializeDefaults(node);
	}

}
