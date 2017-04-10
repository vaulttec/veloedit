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

public interface ITreeNode {
	public static final Object[] NO_CHILDREN = new Object[0];

	String getName();

	Object getParent();

	boolean hasChildren();

	Object[] getChildren();

	String getUniqueID();

	int getStartLine();

	int getEndLine();

	/**
	 * Visitor design pattern.
	 * 
	 * @see ITreeVisitor#visit(ITreeNodeInfo)
	 */
	boolean accept(ITreeVisitor visitor);
}
