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
package org.vaulttec.velocity.ui.editor.outline;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.vaulttec.velocity.core.model.ITreeNode;
import org.vaulttec.velocity.ui.editor.VelocityEditor;
import org.vaulttec.velocity.ui.editor.actions.CollapseAllAction;

/**
 * A content outline page which represents the content of an Velocity template
 * file.
 */
public class VelocityOutlinePage extends ContentOutlinePage {
	private VelocityEditor fEditor;
	private Object fInput;
	private String fSelectedNodeID;
	private VelocityOutlineLabelProvider fLabelProvider;
	private boolean fIsDisposed;

	/**
	 * Creates a content outline page using the given editor.
	 */
	public VelocityOutlinePage(VelocityEditor anEditor) {
		fEditor = anEditor;
		fIsDisposed = true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite aParent) {
		super.createControl(aParent);
		
		fLabelProvider = new VelocityOutlineLabelProvider();

		// Init tree viewer
		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new VelocityOutlineContentProvider(fEditor));
		viewer.setLabelProvider(fLabelProvider);
		viewer.addSelectionChangedListener(this);
		if (fInput != null) {
			viewer.setInput(fInput);
		}
		fIsDisposed = false;
		
		// Add collapse all button to viewer's toolbar
		IToolBarManager mgr = getSite().getActionBars().getToolBarManager();
		mgr.add(new CollapseAllAction(viewer));

		// Refresh outline according to initial cursor position
		update();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent anEvent) {
		super.selectionChanged(anEvent);

		ISelection selection = anEvent.getSelection();
		if (!selection.isEmpty()) {
			ITreeNode node = (ITreeNode)
						   ((IStructuredSelection)selection).getFirstElement();
			if (fSelectedNodeID == null || isDifferentBlock(node)) {
				fEditor.highlightNode(node, true);
				fSelectedNodeID = node.getUniqueID();
			} else {
				fEditor.revealNode(node);
			}
		}
	}

	public void selectNode(int aLine, boolean aForceSelect) {
		if (aLine > 0) {
			TreeViewer viewer = getTreeViewer();
			ITreeNode node = fEditor.getNodeByLine(aLine);
			viewer.removeSelectionChangedListener(this);
			if (node == null) {
				if (fSelectedNodeID != null) {
					viewer.setSelection(new StructuredSelection());
					fEditor.resetHighlightRange();
					fSelectedNodeID = null;
				}
			} else {
				if (aForceSelect || isDifferentBlock(node)) {
					viewer.setSelection(new StructuredSelection(node));
					fEditor.highlightNode(node, false);
					fSelectedNodeID = node.getUniqueID();
				}
				viewer.reveal(node);
			}
			viewer.addSelectionChangedListener(this);
		}
	}

	private boolean isDifferentBlock(ITreeNode aNode) {
		return (fSelectedNodeID == null ||
				 !fSelectedNodeID.equals(aNode.getUniqueID()));
	}

	/**
	 * Sets the input of the outline page.
	 */
	public void setInput(Object aInput) {
		fInput = aInput;
		update();
	}

	/**
	 * Updates the outline page.
	 */
	public void update() {
		TreeViewer viewer = getTreeViewer();
		if (viewer != null) {
			Control control= viewer.getControl();
			if (control != null && !control.isDisposed()) {
				viewer.removeSelectionChangedListener(this);
				control.setRedraw(false);
				viewer.setInput(fInput);
//				viewer.expandAll();
				control.setRedraw(true);
				selectNode(fEditor.getCursorLine(), true);
				viewer.addSelectionChangedListener(this);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#dispose()
	 */
	public void dispose() {
	    setInput(null);
	    if (fLabelProvider != null) {
	    	fLabelProvider.dispose();
	    	fLabelProvider = null;
	    }
	    fIsDisposed = true;
	    super.dispose();
	}

	public boolean isDisposed() {
		return fIsDisposed;
	}
}
