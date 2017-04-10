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

import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.vaulttec.velocity.core.model.ITreeNode;
import org.vaulttec.velocity.ui.editor.VelocityEditor;

public class VelocityOutlineContentProvider implements ITreeContentProvider {
	public static final String VELOCITY_TEMPLATE = "__velocity_template";

	private VelocityEditor fEditor;
	private IPositionUpdater fPositionUpdater;

	public VelocityOutlineContentProvider(VelocityEditor anEditor) {
	    fEditor = anEditor;
        fPositionUpdater = new DefaultPositionUpdater(VELOCITY_TEMPLATE);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer aViewer, Object anOldInput,
							  Object aNewInput) {
		if (anOldInput != aNewInput) {
			if (anOldInput != null) {
				IDocument document = fEditor.getDocumentProvider().getDocument(
																   anOldInput);
				if (document != null) {
					try {
						document.removePositionCategory(VELOCITY_TEMPLATE);
					} catch (BadPositionCategoryException e) {
					}
					document.removePositionUpdater(fPositionUpdater);
				}
			}

			if (aNewInput != null) {
				IDocument document = fEditor.getDocumentProvider().getDocument(
																	aNewInput);
				if (document != null) {
					document.addPositionCategory(VELOCITY_TEMPLATE);
					document.addPositionUpdater(fPositionUpdater);
				}
			}
		}
    }

    /* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
    }
    
    /* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
        return fEditor.getRootElements();
    }
	
    /* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object anElement) {
        return (anElement instanceof ITreeNode) ?
				  ((ITreeNode)anElement).getChildren() : ITreeNode.NO_CHILDREN;
    }

    /* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object anElement) {
		return (anElement instanceof ITreeNode) ?
									((ITreeNode)anElement).getParent() : null;
    }

    /* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object anElement) {
        return (anElement instanceof ITreeNode) ?
								 ((ITreeNode)anElement).hasChildren() : false;
    }
}
