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

import java.util.Iterator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.texteditor.MarkerAnnotation;

/**
 * Determines marker for the given line and formates the according message.
 */
public class VelocityAnnotationHover implements IAnnotationHover {

	@Override
	public String getHoverInfo(ISourceViewer viewer, int line) {
		String info = null;
		IMarker marker = getMarkerForLine(viewer, line);
		if (marker != null) {
			String message = marker.getAttribute(IMarker.MESSAGE, (String) null);
			if (message != null && message.trim().length() > 0) {
				info = message.trim();
			}
		}
		return info;
	}

	/**
	 * Returns one marker which includes the ruler's line of activity.
	 */
	protected IMarker getMarkerForLine(ISourceViewer viewer, int line) {
		IMarker marker = null;
		IAnnotationModel model = viewer.getAnnotationModel();
		if (model != null) {
			Iterator<Annotation> e = model.getAnnotationIterator();
			while (e.hasNext()) {
				Annotation a = e.next();
				if (a instanceof MarkerAnnotation) {
					MarkerAnnotation ma = (MarkerAnnotation) a;
					if (compareRulerLine(model.getPosition(ma), viewer.getDocument(), line) != 0) {
						marker = ma.getMarker();
					}
				}
			}
		}
		return marker;
	}

	/**
	 * Returns distance of given line to specified position (1 = same line, 2 =
	 * included in given position, 0 = not related).
	 */
	protected int compareRulerLine(Position aPosition, IDocument document, int line) {
		int distance = 0;
		if (aPosition.getOffset() > -1 && aPosition.getLength() > -1) {
			try {
				int markerLine = document.getLineOfOffset(aPosition.getOffset());
				if (line == markerLine) {
					distance = 1;
				} else if (markerLine <= line
						&& line <= document.getLineOfOffset(aPosition.getOffset() + aPosition.getLength())) {
					distance = 2;
				}
			} catch (BadLocationException e) {
			}
		}
		return distance;
	}

}
