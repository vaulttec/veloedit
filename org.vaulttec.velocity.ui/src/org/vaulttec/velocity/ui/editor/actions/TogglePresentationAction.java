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
package org.vaulttec.velocity.ui.editor.actions;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextEditorAction;
import org.vaulttec.velocity.ui.IVelocityPreferencesConstants;
import org.vaulttec.velocity.ui.VelocityUIPlugin;
import org.vaulttec.velocity.ui.VelocityPluginImages;

/**
 * A toolbar action which toggles the presentation model of the connected text
 * editor. The editor shows either the highlight range only or always the whole
 * document.
 * <p>
 * Adopted from <code>org.eclipse.jdt.internal.ui.javaeditor.*</code>
 */
public class TogglePresentationAction extends TextEditorAction {

	/**
	 * Constructs and updates the action.
	 */
	public TogglePresentationAction() {
		super(VelocityUIPlugin.getDefault().getResourceBundle(), "VelocityEditor.TogglePresentation.", null);
		VelocityPluginImages.setToolImageDescriptors(this, "segment_edit.gif");
		setToolTipText(VelocityUIPlugin.getMessage("VelocityEditor.TogglePresentation.tooltip"));
		update();
	}

	/**
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		ITextEditor editor = getTextEditor();
		if (editor != null) {
			IRegion remembered = editor.getHighlightRange();
			editor.resetHighlightRange();

			boolean showAll = !editor.showsHighlightRangeOnly();
			setChecked(showAll);

			editor.showHighlightRangeOnly(showAll);
			if (remembered != null) {
				editor.setHighlightRange(remembered.getOffset(), remembered.getLength(), true);
			}
			IPreferenceStore store = VelocityUIPlugin.getDefault().getPreferenceStore();
			store.setValue(IVelocityPreferencesConstants.EDITOR_SHOW_SEGMENTS, showAll);
		}
	}

	@Override
	public void update() {
		ITextEditor editor = getTextEditor();
		boolean checked = (editor != null && editor.showsHighlightRangeOnly());
		setChecked(checked);
		setEnabled(editor != null);
	}

	@Override
	public void setEditor(ITextEditor anEditor) {
		super.setEditor(anEditor);

		if (anEditor != null) {
			IPreferenceStore store = VelocityUIPlugin.getDefault().getPreferenceStore();
			boolean showSegments = store.getBoolean(IVelocityPreferencesConstants.EDITOR_SHOW_SEGMENTS);
			if (anEditor.showsHighlightRangeOnly() != showSegments) {
				IRegion remembered = anEditor.getHighlightRange();
				anEditor.resetHighlightRange();
				anEditor.showHighlightRangeOnly(showSegments);
				if (remembered != null) {
					anEditor.setHighlightRange(remembered.getOffset(), remembered.getLength(), true);
				}
			}
			update();
		}
	}

}
