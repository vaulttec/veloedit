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
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextViewer;

public class VelocityDoubleClickStrategy implements ITextDoubleClickStrategy {
	protected ITextViewer viewer;

	@Override
	public void doubleClicked(ITextViewer viewer) {
		int pos = viewer.getSelectedRange().x;
		if (pos >= 0) {
			this.viewer = viewer;
			if (!selectComment(pos)) {
				selectWord(pos);
			}
		}
	}

	protected boolean selectComment(int caretPos) {
		IDocument doc = viewer.getDocument();
		int startPos, endPos;

		try {
			int pos = caretPos;
			char c = ' ';

			while (pos >= 0) {
				c = doc.getChar(pos);
				if (c == '\\') {
					pos -= 2;
					continue;
				}
				if (c == Character.LINE_SEPARATOR || c == '\"')
					break;
				--pos;
			}

			if (c != '\"')
				return false;

			startPos = pos;

			pos = caretPos;
			int length = doc.getLength();
			c = ' ';

			while (pos < length) {
				c = doc.getChar(pos);
				if (c == Character.LINE_SEPARATOR || c == '\"')
					break;
				++pos;
			}
			if (c != '\"')
				return false;

			endPos = pos;

			int offset = startPos + 1;
			int len = endPos - offset;
			viewer.setSelectedRange(offset, len);
			return true;
		} catch (BadLocationException x) {
		}

		return false;
	}

	protected boolean selectWord(int caretPos) {

		IDocument doc = viewer.getDocument();
		int startPos, endPos;

		try {

			int pos = caretPos;
			char c;

			while (pos >= 0) {
				c = doc.getChar(pos);
				if (!isIdentifierPart(c))
					break;
				--pos;
			}

			startPos = pos;

			pos = caretPos;
			int length = doc.getLength();

			while (pos < length) {
				c = doc.getChar(pos);
				if (!isIdentifierPart(c))
					break;
				++pos;
			}

			endPos = pos;
			selectRange(startPos, endPos);
			return true;

		} catch (BadLocationException x) {
		}

		return false;
	}

	private void selectRange(int startPos, int stopPos) {
		int offset = startPos + 1;
		int length = stopPos - offset;
		viewer.setSelectedRange(offset, length);
	}

	/**
	 * Determines if the specified character may be part of a Velocity
	 * identifier as other than the first character. A character may be part of
	 * a Velocity identifier if and only if it is one of the following:
	 * <ul>
	 * <li>a letter (a..z, A..Z)
	 * <li>a digit (0..9)
	 * <li>a hyphen ("-")
	 * <li>a underscore("_")
	 * </ul>
	 * 
	 * @param c
	 *            the character to be tested.
	 * @return true if the character may be part of a Velocity identifier; false
	 *         otherwise.
	 * @see java.lang.Character#isLetterOrDigit(char)
	 */
	private boolean isIdentifierPart(char c) {
		return (Character.isLetterOrDigit(c) || c == '-' || c == '_');
	}

}