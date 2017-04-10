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

import java.util.ResourceBundle;

import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.Region;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextEditorAction;
import org.vaulttec.velocity.ui.editor.VelocityEditor;

public class GotoDefinitionAction extends TextEditorAction {

	public GotoDefinitionAction(ResourceBundle aBundle, String aPrefix,
								 ITextEditor anEditor) {
		super(aBundle, aPrefix, anEditor);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		VelocityEditor editor = (VelocityEditor)getTextEditor();
		ITextSelection selection = (ITextSelection)
								  editor.getSelectionProvider().getSelection();
		if (!selection.isEmpty()) {
			editor.gotoDefinition(new Region(selection.getOffset(),
											  selection.getLength()));
		}
	}
}
