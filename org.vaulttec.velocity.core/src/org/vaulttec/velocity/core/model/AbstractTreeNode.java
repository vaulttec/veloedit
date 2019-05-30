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
package org.vaulttec.velocity.core.model;

public abstract class AbstractTreeNode implements ITreeNode {

	private ITreeNode parent;
	private int startLine;
	private int endLine;

	protected AbstractTreeNode(ITreeNode parent, int startLine, int endLine) {
		this.parent = parent;
		this.startLine = startLine;
		this.endLine = endLine;
	}

	/**
	 * @see ITreeNode#getName()
	 */
	public abstract String getName();

	/**
	 * @see ITreeNode#getParent()
	 */
	public Object getParent() {
		return parent;
	}

	/**
	 * @see ITreeNodeInfo#getStartLine()
	 */
	public int getStartLine() {
		return startLine;
	}

	/**
	 * @see ITreeNodeInfo#getEndLine()
	 */
	public int getEndLine() {
		return endLine;
	}

}
