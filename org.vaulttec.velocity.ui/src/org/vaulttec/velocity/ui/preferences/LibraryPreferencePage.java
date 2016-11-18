package org.vaulttec.velocity.ui.preferences;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.vaulttec.velocity.core.IPreferencesConstants;
import org.vaulttec.velocity.core.VelocityCorePlugin;
import org.vaulttec.velocity.ui.VelocityUIPlugin;

/**
 * Velocimacro library settings.
 */
public class LibraryPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	private final String PREFIX = "LibraryPreferences.";

	public LibraryPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		setPreferenceStore(new ScopedPreferenceStore(InstanceScope.INSTANCE, VelocityCorePlugin.PLUGIN_ID));
		setDescription(VelocityUIPlugin.getMessage(PREFIX + "description"));
	}

	@Override
	protected void createFieldEditors() {
		DirectoryFieldEditor macroPath = new DirectoryFieldEditor(IPreferencesConstants.LIBRARY_PATH,
				VelocityUIPlugin.getMessage(PREFIX + "path"), getFieldEditorParent());
		addField(macroPath);

		LibraryEditor library = new LibraryEditor(IPreferencesConstants.LIBRARY_LIST,
				VelocityUIPlugin.getMessage(PREFIX + "libraryList"), macroPath, getFieldEditorParent());
		addField(library);
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}
