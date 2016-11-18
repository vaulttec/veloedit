package org.vaulttec.velocity.ui.preferences;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.vaulttec.velocity.core.IPreferencesConstants;
import org.vaulttec.velocity.core.VelocityCorePlugin;
import org.vaulttec.velocity.ui.VelocityUIPlugin;

/**
 * Velocity runtime settings, e.g. loop counter name.
 */
public class VelocityPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	private final String PREFIX = "VelocityPreferences.";

	public VelocityPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		setPreferenceStore(new ScopedPreferenceStore(InstanceScope.INSTANCE, VelocityCorePlugin.PLUGIN_ID));
		setDescription(VelocityUIPlugin.getMessage(PREFIX + "description"));
	}

	@Override
	protected void createFieldEditors() {
		StringFieldEditor counterName = new StringFieldEditor(IPreferencesConstants.VELOCITY_COUNTER_NAME,
				VelocityUIPlugin.getMessage(PREFIX + "counterName"), getFieldEditorParent());
		counterName.setEmptyStringAllowed(false);
		addField(counterName);

		DirectiveEditor directives = new DirectiveEditor(IPreferencesConstants.VELOCITY_USER_DIRECTIVES,
				VelocityUIPlugin.getMessage(PREFIX + "userDirectives"), getFieldEditorParent());
		addField(directives);
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}
