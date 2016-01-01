package org.vaulttec.velocity.ui.preferences;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.vaulttec.velocity.ui.IPreferencesConstants;
import org.vaulttec.velocity.ui.VelocityPlugin;

/**
 * Color settings for syntax highliting.
 */
public class EditorPreferencePage extends FieldEditorPreferencePage
										 implements IWorkbenchPreferencePage {
	private final String PREFIX = "EditorPreferences.";

	public EditorPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		setPreferenceStore(VelocityPlugin.getDefault().getPreferenceStore());
        setDescription(VelocityPlugin.getMessage(PREFIX + "description"));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		addField(new ColorFieldEditor(IPreferencesConstants.COLOR_DEFAULT,
								 VelocityPlugin.getMessage(PREFIX + "default"),
								 getFieldEditorParent()));
		addField(new ColorFieldEditor(IPreferencesConstants.COLOR_COMMENT,
								 VelocityPlugin.getMessage(PREFIX + "comment"),
								 getFieldEditorParent()));
		addField(new ColorFieldEditor(IPreferencesConstants.COLOR_DOC_COMMENT,
							  VelocityPlugin.getMessage(PREFIX + "docComment"),
							  getFieldEditorParent()));
		addField(new ColorFieldEditor(IPreferencesConstants.COLOR_DIRECTIVE,
							   VelocityPlugin.getMessage(PREFIX + "directive"),
							   getFieldEditorParent()));
		addField(new ColorFieldEditor(IPreferencesConstants.COLOR_STRING,
								  VelocityPlugin.getMessage(PREFIX + "string"),
								  getFieldEditorParent()));
		addField(new ColorFieldEditor(IPreferencesConstants.COLOR_REFERENCE,
							   VelocityPlugin.getMessage(PREFIX + "reference"),
							   getFieldEditorParent()));
		addField(new ColorFieldEditor(
        				 IPreferencesConstants.COLOR_STRING_REFERENCE,
						 VelocityPlugin.getMessage(PREFIX + "stringReference"),
						 getFieldEditorParent()));
    }

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench aWorkbench) {
	}

    /* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk() {
        boolean value = super.performOk();
        VelocityPlugin.getDefault().savePluginPreferences();
        return value;
    }
}
