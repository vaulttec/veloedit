package org.vaulttec.velocity.ui.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.vaulttec.velocity.ui.IColorConstants;
import org.vaulttec.velocity.ui.VelocityColorProvider;
import org.vaulttec.velocity.ui.editor.text.IVelocityPartitions;
import org.vaulttec.velocity.ui.editor.text.NonRuleBasedDamagerRepairer;

public class VelocityConfiguration extends TextSourceViewerConfiguration {

	private VelocityEditor fEditor;

	public VelocityConfiguration(VelocityEditor anEditor) {
		fEditor = anEditor;
	}

	@Override
	public String[] getConfiguredContentTypes(ISourceViewer aSourceViewer) {
		int length = IVelocityPartitions.PARTITIONS.length;
		String[] contentTypes = new String[length + 1];
		contentTypes[0] = IDocument.DEFAULT_CONTENT_TYPE;
		for (int i = 0; i < length; i++) {
			contentTypes[i + 1] = IVelocityPartitions.PARTITIONS[i];
		}
		return contentTypes;
	}

	@Override
	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
		return IVelocityPartitions.VELOCITY_PARTITIONING;
	}

	@Override
	public ITextHover getTextHover(ISourceViewer aSourceViewer, String aContentType) {
		ITextHover hover;
		if (aContentType.equals(IDocument.DEFAULT_CONTENT_TYPE)
				|| aContentType.equals(IVelocityPartitions.PARSED_STRING)) {
			hover = new VelocityTextHover(fEditor);
		} else {
			hover = null;
		}
		return hover;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getAnnotationHover(org.eclipse.jface.text.source.ISourceViewer)
	 */
	public IAnnotationHover getAnnotationHover(ISourceViewer aSourceViewer) {
		return new VelocityAnnotationHover();
	}

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer aSourceViewer) {
		ContentAssistant assistant = new ContentAssistant();
		assistant.setContentAssistProcessor(new VelocityCompletionProcessor(fEditor, true),
				IDocument.DEFAULT_CONTENT_TYPE);
		assistant.setContentAssistProcessor(new VelocityCompletionProcessor(fEditor, false),
				IVelocityPartitions.PARSED_STRING);
		assistant.enableAutoInsert(true);
		assistant.enableAutoActivation(true);
		return assistant;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getDoubleClickStrategy(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
	 */
	public ITextDoubleClickStrategy getDoubleClickStrategy(
							ISourceViewer aSourceViewer, String aContentType) {
		return VelocityEditorEnvironment.getDoubleClickStrategy();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getDefaultPrefixes(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
	 */
	public String[] getDefaultPrefixes(ISourceViewer aSourceViewer,
										String aContentType) {
		return new String[] { "##", "" };
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer aSourceViewer) {
		VelocityColorProvider cp = VelocityEditorEnvironment.getColorProvider();
		PresentationReconciler rec = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(VelocityEditorEnvironment.getCodeScanner());
		rec.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		rec.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		NonRuleBasedDamagerRepairer ndr = new NonRuleBasedDamagerRepairer(
				new TextAttribute(cp.getColor(IColorConstants.COMMENT)));
		rec.setDamager(ndr, IVelocityPartitions.SINGLE_LINE_COMMENT);
		rec.setRepairer(ndr, IVelocityPartitions.SINGLE_LINE_COMMENT);

		ndr = new NonRuleBasedDamagerRepairer(new TextAttribute(cp.getColor(IColorConstants.COMMENT)));
		rec.setDamager(ndr, IVelocityPartitions.MULTI_LINE_COMMENT);
		rec.setRepairer(ndr, IVelocityPartitions.MULTI_LINE_COMMENT);

		ndr = new NonRuleBasedDamagerRepairer(new TextAttribute(cp.getColor(IColorConstants.DOC_COMMENT)));
		rec.setDamager(ndr, IVelocityPartitions.DOC_COMMENT);
		rec.setRepairer(ndr, IVelocityPartitions.DOC_COMMENT);

		ndr = new NonRuleBasedDamagerRepairer(new TextAttribute(cp.getColor(IColorConstants.STRING)));
		rec.setDamager(ndr, IVelocityPartitions.UNPARSED_STRING);
		rec.setRepairer(ndr, IVelocityPartitions.UNPARSED_STRING);

		dr = new DefaultDamagerRepairer(VelocityEditorEnvironment.getStringScanner());
		rec.setDamager(dr, IVelocityPartitions.PARSED_STRING);
		rec.setRepairer(dr, IVelocityPartitions.PARSED_STRING);
		return rec;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getReconciler(org.eclipse.jface.text.source.ISourceViewer)
	 */
	public IReconciler getReconciler(ISourceViewer aSourceViewer) {
		return new MonoReconciler(fEditor.getReconcilingStrategy(), false);
	}
}
