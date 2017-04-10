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
package org.vaulttec.velocity.ui;

/**
 * Defines constants which are used to refer to values in the plugin's
 * preference bundle.
 */
public interface IVelocityPreferencesConstants {
	String PREFIX_COLOR = "color.";

	String EDITOR_SHOW_SEGMENTS = "editor.showSegments";

	String COLOR_DEFAULT = PREFIX_COLOR + "default";
	String COLOR_COMMENT = PREFIX_COLOR + "comment";
	String COLOR_DOC_COMMENT = PREFIX_COLOR + "doc_comment";
	String COLOR_DIRECTIVE = PREFIX_COLOR + "directive";
	String COLOR_STRING = PREFIX_COLOR + "string";
	String COLOR_REFERENCE = PREFIX_COLOR + "reference";
	String COLOR_STRING_REFERENCE = PREFIX_COLOR + "string_reference";
}
