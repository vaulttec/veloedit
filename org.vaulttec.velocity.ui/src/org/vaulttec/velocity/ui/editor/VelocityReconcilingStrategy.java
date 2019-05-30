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
package org.vaulttec.velocity.ui.editor;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.Token;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.swt.widgets.Display;
import org.vaulttec.velocity.core.model.ITreeNode;
import org.vaulttec.velocity.core.model.Template;
import org.vaulttec.velocity.core.parser.NodeVisitor;
import org.vaulttec.velocity.core.parser.VelocityParser;
import org.vaulttec.velocity.ui.VelocityUIPlugin;

/**
 * Reconciler strategy which parses the whole editor's content (a Velocity
 * template) on a document change.
 */
public class VelocityReconcilingStrategy implements IReconcilingStrategy {

	private final VelocityEditor editor;
	private Template template;
	private Template lastTemplate;
	private String error;

	public VelocityReconcilingStrategy(VelocityEditor editor) {
		this.editor = editor;
	}

	public void setDocument(IDocument document) {
		parse();
	}

	public void reconcile(DirtyRegion dirtyRegion, IRegion region) {
		parse();
	}

	public void reconcile(IRegion partition) {
		parse();
	}

	private void parse() {
		editor.deleteAllProblemMarkers();

		String name = editor.getEditorInput().getName();
		Reader reader = new StringReader(editor.getDocument().get());
		Template newTemplate = null;
		try {
			VelocityParser parser = VelocityEditorEnvironment.getParser();
			org.apache.velocity.Template velocityTemplate = parser.createTemplate(name);
			SimpleNode root = parser.parse(reader, velocityTemplate);

			// Create tree model
			NodeVisitor visitor = new NodeVisitor(velocityTemplate);
			root.jjtAccept(visitor, null);
			newTemplate = visitor.getTemplate();
			error = "";
		} catch (ParseException e) {
			if (e.getMessage() != null) {
				error = e.getMessage();
				Token token = e.currentToken;
				if (token != null) {
					editor.addProblemMarker(e.getMessage(), token.next.beginLine);
				}
			} else {
				error = "";
			}
		} catch (Exception e) {
			error = "";
			VelocityUIPlugin.log(e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				VelocityUIPlugin.log(e);
			}
		}

		// Replace saved template with the new parsed one
		synchronized (this) {
			if (newTemplate != null) {
				template = newTemplate;

				// Save last successful parse tree
				lastTemplate = newTemplate;
			} else {
				template = null;
			}
		}

		// Update outline view and display error message in status line
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				editor.updateOutlinePage();
				editor.displayErrorMessage(error);
			}
		});
	}

	/**
	 * Returns root elements of current parse tree.
	 */
	public Object[] getRootElements() {
		return (template != null ? template.getChildren() : ITreeNode.NO_CHILDREN);
	}

	/**
	 * Returns root node of current parse tree.
	 */
	public ITreeNode getRootNode() {
		return template;
	}

	/**
	 * Returns last successful parse tree.
	 */
	public ITreeNode getLastRootNode() {
		return lastTemplate;
	}

}
