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

import org.eclipse.ui.texteditor.ITextEditorActionConstants;

/**
 * Action IDs for standard actions, for groups in the menu bar, and for actions
 * in context menus of Velocity views.
 */
public interface IVelocityActionConstants extends ITextEditorActionConstants {
	
	/**
	 * Edit menu: name of standard Code Assist global action
	 * (value <code>"ContentAssist"</code>).
	 */
	public static final String CONTENT_ASSIST = "ContentAssist";
	
	/**
	 * Edit menu: name of standard Open Editor global action (value
	 * <code>"GotoDefinition"</code>).
	 */
	public static final String GOTO_DEFINITION = "GotoDefinition";
	
	/**
	 * Source menu: name of standard Comment global action
	 * (value <code>"Comment"</code>).
	 */
	public static final String COMMENT = "Comment";
	
	/**
	 * Source menu: name of standard Uncomment global action
	 * (value <code>"Uncomment"</code>).
	 */
	public static final String UNCOMMENT = "Uncomment";
}
