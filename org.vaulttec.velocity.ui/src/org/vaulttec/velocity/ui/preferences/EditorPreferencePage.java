/*******************************************************************************
 * Copyright (c) 2003 Torsten Juergeleit.
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
package org.vaulttec.velocity.ui.preferences;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.vaulttec.velocity.ui.IVelocityPreferencesConstants;
import org.vaulttec.velocity.ui.VelocityUIPlugin;

/**
 * Color settings for syntax highlighting.
 */
public class EditorPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	private final String PREFIX = "EditorPreferences.";

	public EditorPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		setPreferenceStore(VelocityUIPlugin.getDefault().getPreferenceStore());
		setDescription(VelocityUIPlugin.getMessage(PREFIX + "description"));
	}

	@Override
	protected void createFieldEditors() {
		addField(new ColorFieldEditor(IVelocityPreferencesConstants.COLOR_DEFAULT,
				VelocityUIPlugin.getMessage(PREFIX + "default"), getFieldEditorParent()));
		addField(new ColorFieldEditor(IVelocityPreferencesConstants.COLOR_COMMENT,
				VelocityUIPlugin.getMessage(PREFIX + "comment"), getFieldEditorParent()));
		addField(new ColorFieldEditor(IVelocityPreferencesConstants.COLOR_DOC_COMMENT,
				VelocityUIPlugin.getMessage(PREFIX + "docComment"), getFieldEditorParent()));
		addField(new ColorFieldEditor(IVelocityPreferencesConstants.COLOR_DIRECTIVE,
				VelocityUIPlugin.getMessage(PREFIX + "directive"), getFieldEditorParent()));
		addField(new ColorFieldEditor(IVelocityPreferencesConstants.COLOR_STRING,
				VelocityUIPlugin.getMessage(PREFIX + "string"), getFieldEditorParent()));
		addField(new ColorFieldEditor(IVelocityPreferencesConstants.COLOR_REFERENCE,
				VelocityUIPlugin.getMessage(PREFIX + "reference"), getFieldEditorParent()));
		addField(new ColorFieldEditor(IVelocityPreferencesConstants.COLOR_STRING_REFERENCE,
				VelocityUIPlugin.getMessage(PREFIX + "stringReference"), getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}
