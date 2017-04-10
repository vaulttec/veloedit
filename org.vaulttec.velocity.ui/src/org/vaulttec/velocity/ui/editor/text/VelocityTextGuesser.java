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

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

/**
 * Guesses the start/end and the type of Velocity content (directive or
 * identifier) from a given offset.
 */
public class VelocityTextGuesser {
	public static final int TYPE_INVALID = 0;
	public static final int TYPE_DIRECTIVE = 1;
	public static final int TYPE_VARIABLE = 2;
	private int fType;
	private String fText;
	private int fLine;

	/**
	 * Create an invalid text guesser.
	 */
	public VelocityTextGuesser() {
		fType = TYPE_INVALID;
		fText = "";
		fLine = -1;
	}

	public VelocityTextGuesser(IDocument aDocument, int anOffset,
								boolean aGuessEnd) {
		try {

		 	// Guess start position
			int start = anOffset;
			while (start >= 1 &&
								isWordPart(aDocument.getChar(start - 1))) {
				start--;
			}

			// Guess end position
			int end = anOffset;
			if (aGuessEnd) {
				int len = aDocument.getLength() - 1;
				while (end < len && isWordPart(aDocument.getChar(end))) {
					end++;
				}
			}
			fText = aDocument.get(start, end - start);
			fLine = aDocument.getLineOfOffset(start) + 1;

			// Now guess fType of completion
			if (start >= 1) {

				// Directive or shorthand reference
				char c1 = aDocument.getChar(start - 1);
				if (c1 == '#') {
					fType = TYPE_DIRECTIVE;
				} else if (c1 == '$') {
					fType = TYPE_VARIABLE;
				} else {
					if (start >= 2) {

						// Formal or quiet reference
						char c2 = aDocument.getChar(start - 2);
						if (c2 == '$' && (c1 == '{' || c1 == '!')) {
							fType = TYPE_VARIABLE;
						} else {
							if (start >= 3) {

								// Formal quiet reference 
								char c3 = aDocument.getChar(start - 3);
								if (c3 == '$' && c2 == '!' || c1 == '{') {
									fType = TYPE_VARIABLE;
								}
							}
						}
					}
				}
			}
		} catch (BadLocationException e) {
			fType = TYPE_INVALID;
			fText = "";
			fLine = -1;
		}
	}

	public int getType() {
		return fType;
	}

	public String getText() {
		return fText;
	}

	public int getLine() {
		return fLine;
	}

	/**
     * Determines if the specified character may be part of a Velocity
     * identifier as other than the first character. A character may be part of
     * a Velocity identifier if and only if it is one of the following:
     * <ul>
     * <li>a letter (a..z, A..Z)
     * <li>a digit (0..9)
     * <li>a hyphen ("-")
     * <li>a connecting punctuation character ("_")
     * </ul>
     * 
     * @param aChar  the character to be tested.
     * @return true if the character may be part of a Velocity identifier; 
     *          false otherwise.
     * @see java.lang.Character#isLetterOrDigit(char)
	 */
	private static final boolean isWordPart(char aChar) {
		return Character.isLetterOrDigit(aChar) || aChar == '-' ||
													aChar == '_';
	}

	public String toString() {
		return "type=" + fType + ", text=" + fText + ", line=" + fLine;
	}
}
