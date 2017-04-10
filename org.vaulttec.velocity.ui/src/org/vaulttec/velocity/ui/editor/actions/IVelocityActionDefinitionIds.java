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

import org.eclipse.jdt.ui.actions.IJavaEditorActionDefinitionIds;

/**
 * Defines the definition IDs for the Velocity editor actions.
 */
public interface IVelocityActionDefinitionIds	
										extends IJavaEditorActionDefinitionIds {
	/**
	 * Action definition ID of the 'Navigate -> Go To Definition' action.
	 */
	public static final String GOTO_DEFINITION =
								"org.vaulttec.velocity.ui.edit.goto.definition";
}
