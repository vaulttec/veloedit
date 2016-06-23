package org.vaulttec.velocity.ui.editor;

import org.eclipse.jdt.ui.actions.IJavaEditorActionDefinitionIds;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.texteditor.BasicTextEditorActionContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.RetargetTextEditorAction;
import org.vaulttec.velocity.ui.VelocityPlugin;
import org.vaulttec.velocity.ui.editor.actions.IVelocityActionConstants;
import org.vaulttec.velocity.ui.editor.actions.IVelocityActionDefinitionIds;
import org.vaulttec.velocity.ui.editor.actions.TogglePresentationAction;

/**
 * Contributes Velocity actions to the desktop's edit menu and registers the
 * according global action handlers.
 */
public class VelocityEditorActionContributor extends BasicTextEditorActionContributor {
	private static final String PREFIX = "VelocityEditor.";

	private TogglePresentationAction togglePresentation;
	private RetargetTextEditorAction gotoDefinition;
	private RetargetTextEditorAction contentAssist;
	private RetargetTextEditorAction comment;
	private RetargetTextEditorAction uncomment;

	/**
	 * Defines the menu actions and their action handlers.
	 */
	public VelocityEditorActionContributor() {
		createActions();
	}

	protected void createActions() {

		// Define toolbar actions
		togglePresentation = new TogglePresentationAction();

		// Define text editor actions
		gotoDefinition = new RetargetTextEditorAction(VelocityPlugin.getDefault().getResourceBundle(),
				PREFIX + "GotoDefinition.");
		gotoDefinition.setActionDefinitionId(IVelocityActionDefinitionIds.GOTO_DEFINITION);
		contentAssist = new RetargetTextEditorAction(VelocityPlugin.getDefault().getResourceBundle(),
				PREFIX + "ContentAssist.");
		contentAssist.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
		comment = new RetargetTextEditorAction(VelocityPlugin.getDefault().getResourceBundle(), PREFIX + "Comment.");
		comment.setActionDefinitionId(IJavaEditorActionDefinitionIds.COMMENT);
		uncomment = new RetargetTextEditorAction(VelocityPlugin.getDefault().getResourceBundle(),
				PREFIX + "Uncomment.");
		uncomment.setActionDefinitionId(IJavaEditorActionDefinitionIds.UNCOMMENT);
	}

	@Override
	public void setActiveEditor(IEditorPart part) {
		super.setActiveEditor(part);
		doSetActiveEditor(part);
	}

	/**
	 * Sets the active editor to the actions provided by this contributor.
	 * 
	 * @param part
	 *            the editor
	 */
	private void doSetActiveEditor(IEditorPart part) {
		IStatusLineManager manager = getActionBars().getStatusLineManager();
		manager.setMessage(null);
		manager.setErrorMessage(null);

		// Set the underlying action (registered by the according editor) in
		// the action handlers
		ITextEditor editor = null;
		if (part instanceof ITextEditor) {
			editor = (ITextEditor) part;
		}
		togglePresentation.setEditor(editor);
		gotoDefinition.setAction(getAction(editor, IVelocityActionConstants.GOTO_DEFINITION));
		contentAssist.setAction(getAction(editor, IVelocityActionConstants.CONTENT_ASSIST));
		comment.setAction(getAction(editor, IVelocityActionConstants.COMMENT));
		uncomment.setAction(getAction(editor, IVelocityActionConstants.UNCOMMENT));
		// Set global action handlers according to the given editor
		IActionBars actionBars = getActionBars();
		if (actionBars != null) {
			actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(),
					getAction(editor, ITextEditorActionConstants.DELETE));
			actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(),
					getAction(editor, ITextEditorActionConstants.UNDO));
			actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(),
					getAction(editor, ITextEditorActionConstants.REDO));
			actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(),
					getAction(editor, ITextEditorActionConstants.CUT));
			actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(),
					getAction(editor, ITextEditorActionConstants.COPY));
			actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(),
					getAction(editor, ITextEditorActionConstants.PASTE));
			actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(),
					getAction(editor, ITextEditorActionConstants.SELECT_ALL));
			actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(),
					getAction(editor, ITextEditorActionConstants.FIND));
			actionBars.setGlobalActionHandler(IDEActionFactory.BOOKMARK.getId(),
					getAction(editor, IDEActionFactory.BOOKMARK.getId()));
			actionBars.setGlobalActionHandler(IDEActionFactory.ADD_TASK.getId(),
					getAction(editor, IDEActionFactory.ADD_TASK.getId()));

			actionBars.setGlobalActionHandler(ITextEditorActionConstants.GOTO_LINE,
					getAction(editor, ITextEditorActionDefinitionIds.LINE_GOTO));

			actionBars.setGlobalActionHandler(IJavaEditorActionDefinitionIds.COMMENT,
					getAction(editor, IVelocityActionConstants.COMMENT));
			actionBars.setGlobalActionHandler(IJavaEditorActionDefinitionIds.UNCOMMENT,
					getAction(editor, IVelocityActionConstants.UNCOMMENT));

			actionBars.setGlobalActionHandler(IVelocityActionConstants.GOTO_DEFINITION,
					getAction(editor, IVelocityActionConstants.GOTO_DEFINITION));

			actionBars.updateActionBars();
		}
	}

	@Override
	public void contributeToMenu(IMenuManager menuManager) {
		super.contributeToMenu(menuManager);

		// Add actions to desktop's edit menu
		IMenuManager menu = menuManager.findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);
		if (menu != null) {
			menu.add(contentAssist);
			menu.add(comment);
			menu.add(uncomment);
		}

		// Add actions to desktop's navigate menu
		menu = menuManager.findMenuUsingPath(IWorkbenchActionConstants.M_NAVIGATE);
		if (menu != null) {
			menu.appendToGroup(IWorkbenchActionConstants.MB_ADDITIONS, gotoDefinition);
		}
	}

	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(new Separator());
		toolBarManager.add(togglePresentation);
	}

	@Override
	public void dispose() {
		doSetActiveEditor(null);
		super.dispose();
	}

}
