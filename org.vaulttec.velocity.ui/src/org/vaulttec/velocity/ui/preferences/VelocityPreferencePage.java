package org.vaulttec.velocity.ui.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.vaulttec.velocity.ui.IPreferencesConstants;
import org.vaulttec.velocity.ui.VelocityPlugin;

/**
 * Velocity runtime settings, e.g. loop counter name.
 */
public class VelocityPreferencePage extends FieldEditorPreferencePage
										 implements IWorkbenchPreferencePage {
	private final String PREFIX = "VelocityPreferences.";

	public VelocityPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		setPreferenceStore(VelocityPlugin.getDefault().getPreferenceStore());
        setDescription(VelocityPlugin.getMessage(PREFIX + "description"));
	}

	/**
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		StringFieldEditor counterName = new StringFieldEditor(
							 IPreferencesConstants.VELOCITY_COUNTER_NAME,
							 VelocityPlugin.getMessage(PREFIX + "counterName"),
							 getFieldEditorParent());
		counterName.setEmptyStringAllowed(false);
		addField(counterName);

		DirectiveEditor directives = new DirectiveEditor(
						  IPreferencesConstants.VELOCITY_USER_DIRECTIVES,
						  VelocityPlugin.getMessage(PREFIX + "userDirectives"),
						  getFieldEditorParent());
		addField(directives);
    }

	/**
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench aWorkbench) {
	}

    /**
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk() {
        boolean value = super.performOk();
        VelocityPlugin.getDefault().savePluginPreferences();
        return value;
    }
}
