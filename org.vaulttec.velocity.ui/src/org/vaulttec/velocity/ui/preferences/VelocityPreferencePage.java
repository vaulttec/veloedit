package org.vaulttec.velocity.ui.preferences;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.vaulttec.velocity.core.IPreferencesConstants;
import org.vaulttec.velocity.core.VelocityCorePlugin;
import org.vaulttec.velocity.ui.VelocityPlugin;

/**
 * Velocity runtime settings, e.g. loop counter name.
 */
public class VelocityPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	private final String PREFIX = "VelocityPreferences.";

	public VelocityPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		setPreferenceStore(new ScopedPreferenceStore(InstanceScope.INSTANCE, VelocityCorePlugin.PLUGIN_ID));
		setDescription(VelocityPlugin.getMessage(PREFIX + "description"));
	}

	@Override
	protected void createFieldEditors() {
		StringFieldEditor counterName = new StringFieldEditor(IPreferencesConstants.VELOCITY_COUNTER_NAME,
				VelocityPlugin.getMessage(PREFIX + "counterName"), getFieldEditorParent());
		counterName.setEmptyStringAllowed(false);
		addField(counterName);

		DirectiveEditor directives = new DirectiveEditor(IPreferencesConstants.VELOCITY_USER_DIRECTIVES,
				VelocityPlugin.getMessage(PREFIX + "userDirectives"), getFieldEditorParent());
		addField(directives);
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	public boolean performOk() {
		boolean value = super.performOk();
		VelocityPlugin.getDefault().savePluginPreferences();
		return value;
	}
}
