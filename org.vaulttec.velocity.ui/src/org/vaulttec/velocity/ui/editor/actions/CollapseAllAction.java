package org.vaulttec.velocity.ui.editor.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.vaulttec.velocity.ui.VelocityPlugin;
import org.vaulttec.velocity.ui.VelocityPluginImages;

public class CollapseAllAction extends Action {
	private AbstractTreeViewer fViewer;

	public CollapseAllAction(AbstractTreeViewer aViewer) {
		fViewer = aViewer;
        setText(VelocityPlugin.getMessage(
									"VelocityEditor.CollapseAllAction.label"));
        setToolTipText(VelocityPlugin.getMessage(
								  "VelocityEditor.CollapseAllAction.tooltip"));
        VelocityPluginImages.setLocalImageDescriptors(this, "collapseall.gif");
	}

	/**
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		fViewer.collapseAll();
	}
}
