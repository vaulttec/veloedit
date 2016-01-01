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
