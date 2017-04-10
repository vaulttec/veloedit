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
package org.vaulttec.velocity.ui.editor.text;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * A Velocity directive aware word detector.
 */
public class DirectiveDetector implements IWordDetector {

	/**
     * Determines if the specified character is permissible as the first
     * character in a Velocity directive.
     * A character may start a Velocity directive if and only if
     * it is one of the following:
     * <ul>
     * <li>a hash (#)
     * </ul>
     *
     * @param aChar  the character to be tested.
     * @return true if the character may start a Velocity directive;
     *          false otherwise.
     * @see java.lang.Character#isLetter(char)
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart
	 */
	public boolean isWordStart(char aChar) {
		return aChar == '#';
	}
	
	/**
     * Determines if the specified character may be part of a Velocity
     * directive as other than the first character.
     * A character may be part of a Velocity directive if and only if
     * it is one of the following:
     * <ul>
     * <li>a letter (a..z, A..Z)
     * <li>a digit (0..9)
     * <li>a hyphen ("-")
     * <li>a connecting punctuation character ("_")
     * </ul>
     * 
     * @param aChar  the character to be tested.
     * @return true if the character may be part of a Velocity directive; 
     *          false otherwise.
     * @see java.lang.Character#isLetterOrDigit(char)
	 * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart
	 */
	public boolean isWordPart(char aChar) {
		return Character.isLetterOrDigit(aChar) || aChar == '-' ||
													aChar == '_';
	}
}
