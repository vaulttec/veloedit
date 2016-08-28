package org.vaulttec.velocity.ui.preferences;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.vaulttec.velocity.ui.IVelocityPreferencesConstants;
import org.vaulttec.velocity.ui.VelocityUIPlugin;

/**
 * Color settings for syntax highliting.
 */
public class EditorPreferencePage extends FieldEditorPreferencePage
										 implements IWorkbenchPreferencePage {
	private final String PREFIX = "EditorPreferences.";

	public EditorPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		setPreferenceStore(VelocityUIPlugin.getDefault().getPreferenceStore());
        setDescription(VelocityUIPlugin.getMessage(PREFIX + "description"));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		addField(new ColorFieldEditor(IVelocityPreferencesConstants.COLOR_DEFAULT,
								 VelocityUIPlugin.getMessage(PREFIX + "default"),
								 getFieldEditorParent()));
		addField(new ColorFieldEditor(IVelocityPreferencesConstants.COLOR_COMMENT,
								 VelocityUIPlugin.getMessage(PREFIX + "comment"),
								 getFieldEditorParent()));
		addField(new ColorFieldEditor(IVelocityPreferencesConstants.COLOR_DOC_COMMENT,
							  VelocityUIPlugin.getMessage(PREFIX + "docComment"),
							  getFieldEditorParent()));
		addField(new ColorFieldEditor(IVelocityPreferencesConstants.COLOR_DIRECTIVE,
							   VelocityUIPlugin.getMessage(PREFIX + "directive"),
							   getFieldEditorParent()));
		addField(new ColorFieldEditor(IVelocityPreferencesConstants.COLOR_STRING,
								  VelocityUIPlugin.getMessage(PREFIX + "string"),
								  getFieldEditorParent()));
		addField(new ColorFieldEditor(IVelocityPreferencesConstants.COLOR_REFERENCE,
							   VelocityUIPlugin.getMessage(PREFIX + "reference"),
							   getFieldEditorParent()));
		addField(new ColorFieldEditor(
        				 IVelocityPreferencesConstants.COLOR_STRING_REFERENCE,
						 VelocityUIPlugin.getMessage(PREFIX + "stringReference"),
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
        VelocityUIPlugin.getDefault().savePluginPreferences();
        return value;
    }
}
