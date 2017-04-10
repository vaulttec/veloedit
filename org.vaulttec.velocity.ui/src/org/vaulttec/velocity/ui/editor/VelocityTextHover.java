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

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;

public class VelocityTextHover implements ITextHover {

	private VelocityEditor fEditor;

	public VelocityTextHover(VelocityEditor anEditor) {
		fEditor = anEditor;
	}

	public String getHoverInfo(ITextViewer aTextViewer, IRegion aRegion) {
		return fEditor.getDefinitionLine(aRegion);
	}
	
	public IRegion getHoverRegion(ITextViewer aTextViewer, int anOffset) {
		return new Region(anOffset, 0);
	}
}
