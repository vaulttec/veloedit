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
