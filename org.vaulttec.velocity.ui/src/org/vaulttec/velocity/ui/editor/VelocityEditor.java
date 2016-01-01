package org.vaulttec.velocity.ui.editor;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.ui.actions.IJavaEditorActionDefinitionIds;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.EditorActionBarContributor;
import org.eclipse.ui.texteditor.ContentAssistAction;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.MarkerAnnotation;
import org.eclipse.ui.texteditor.TextOperationAction;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.vaulttec.velocity.ui.VelocityPlugin;
import org.vaulttec.velocity.ui.editor.actions.GotoDefinitionAction;
import org.vaulttec.velocity.ui.editor.actions.IVelocityActionConstants;
import org.vaulttec.velocity.ui.editor.actions.IVelocityActionDefinitionIds;
import org.vaulttec.velocity.ui.editor.outline.VelocityOutlinePage;
import org.vaulttec.velocity.ui.editor.parser.VelocityMacro;
import org.vaulttec.velocity.ui.editor.text.VelocityTextGuesser;
import org.vaulttec.velocity.ui.model.ITreeNode;
import org.vaulttec.velocity.ui.model.ModelTools;

public class VelocityEditor extends /*Projection*/TextEditor {
	private static final String PREFIX = "VelocityEditor.";

//	private AnnotationModel fAnnotationModel;
	private ModelTools fModelTools;
	private VelocityReconcilingStrategy fReconcilingStrategy;
	private VelocityOutlinePage fOutlinePage;

	/** Last cursor position (line) handled in
	 * <code>handleCursorPositionChanged()</code> */
	private int fLastCursorLine;

	public VelocityEditor() {
		fModelTools = new ModelTools(this);
		fReconcilingStrategy = new VelocityReconcilingStrategy(this);
	}

//	public void collapse(int anOffset, int aLength) {
//		ProjectionSourceViewer viewer = (ProjectionSourceViewer)getSourceViewer();
//		viewer.collapse(anOffset, aLength);
//	}
//	
//	public void expand(int anOffset, int aLength) {
//		ProjectionSourceViewer viewer = (ProjectionSourceViewer)getSourceViewer();
//		viewer.expand(anOffset, aLength);
//	}
//	
//	public void defineProjection(int anOffset, int aLength) {
//		Position pos = new Position(anOffset, aLength);
//		fAnnotationModel.addAnnotation(new ProjectionAnnotation(pos),
//												 pos);
//	}
//	
//	public void removeAllProjections() {
//		fAnnotationModel.removeAllAnnotations();
//	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.editors.text.TextEditor#initializeEditor()
	 */
	protected void initializeEditor() {
		super.initializeEditor();

		VelocityEditorEnvironment.connect();

		setDocumentProvider(new VelocityDocumentProvider());
		setSourceViewerConfiguration(new VelocityConfiguration(this));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.editors.text.TextEditor#initializeKeyBindingScopes()
	 */
	protected void initializeKeyBindingScopes() {
		setKeyBindingScopes(new String[] {
							 "org.vaulttec.velocity.ui.velocityEditorScope" });
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#createSourceViewer(org.eclipse.swt.widgets.Composite, org.eclipse.jface.text.source.IVerticalRuler, int)
	 */
//	protected ISourceViewer createSourceViewer(Composite parent,
//											IVerticalRuler ruler, int styles) {
//		ProjectionSourceViewer viewer = new ProjectionSourceViewer(parent,
//																ruler, styles);
//		if (fAnnotationModel != null) {
//			viewer.setProjectionAnnotationModel(fAnnotationModel);
//			StyledText text= viewer.getTextWidget();
//			text.addPaintListener(new ProjectionPainter(viewer));
//		}
//		return viewer;
//	}

	/* (non-Javadoc)
	 * @see AbstractTextEditor#createVerticalRuler()
	 */
//	protected IVerticalRuler createVerticalRuler() {
//		CompositeRuler ruler = new CompositeRuler(1);
//		ruler.addDecorator(0, new AnnotationRulerColumn(VERTICAL_RULER_WIDTH));
//		fAnnotationModel = new AnnotationModel();
//		IVerticalRulerColumn column = new OutlinerRulerColumn(
//						 fAnnotationModel, VERTICAL_RULER_WIDTH - 1);
//		ruler.addDecorator(1, column);
//		return ruler;
//	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#createActions()
	 */
	protected void createActions() {
		super.createActions();

		// Add goto definition action
		IAction action = new GotoDefinitionAction(
								VelocityPlugin.getDefault().getResourceBundle(),
								PREFIX + "GotoDefinition.", this);
		action.setActionDefinitionId(
								  IVelocityActionDefinitionIds.GOTO_DEFINITION);
		setAction(IVelocityActionConstants.GOTO_DEFINITION, action);

		// Add content assist propsal action
		action = new ContentAssistAction(
								VelocityPlugin.getDefault().getResourceBundle(),
								PREFIX + "ContentAssist.", this);
		action.setActionDefinitionId(
					   ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
		setAction(IVelocityActionConstants.CONTENT_ASSIST, action);

		// Add comment action
		action = new TextOperationAction(
					  VelocityPlugin.getDefault().getResourceBundle(),
					  PREFIX + "Comment.", this, ITextOperationTarget.PREFIX);
		action.setActionDefinitionId(IJavaEditorActionDefinitionIds.COMMENT);		
		setAction(IVelocityActionConstants.COMMENT, action);

		// Add uncomment action
		action = new TextOperationAction(
			  VelocityPlugin.getDefault().getResourceBundle(),
			  PREFIX + "Uncomment.", this, ITextOperationTarget.STRIP_PREFIX);
		action.setActionDefinitionId(IJavaEditorActionDefinitionIds.UNCOMMENT);		
		setAction(IVelocityActionConstants.UNCOMMENT, action);
	}
	
	/**
	 * Get the outline page if requested.
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable.getAdapter(Class)
	 */ 
	public Object getAdapter(Class aClass) {
	    Object adapter;
		if (aClass.equals(IContentOutlinePage.class)) {
			if (fOutlinePage == null || fOutlinePage.isDisposed()) {
			    fOutlinePage = new VelocityOutlinePage(this);
				if (getEditorInput() != null) {
					fOutlinePage.setInput(getEditorInput());
				}
			}
			adapter = fOutlinePage;
		} else {
		    adapter = super.getAdapter(aClass);
		}
		return adapter;
	}

	/**
	 * Disconnect from editor environment and dispose outline page.
	 *
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose() {
		if (fOutlinePage != null && !fOutlinePage.isDisposed()) {
			fOutlinePage.dispose();
			fOutlinePage = null;
		}
		VelocityEditorEnvironment.disconnect();
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#editorContextMenuAboutToShow(org.eclipse.jface.action.IMenuManager)
	 */
	protected void editorContextMenuAboutToShow(IMenuManager aMenu) {
		super.editorContextMenuAboutToShow(aMenu);
		addAction(aMenu, ITextEditorActionConstants.MB_ADDITIONS,
				  IVelocityActionConstants.GOTO_DEFINITION);
		addAction(aMenu, ITextEditorActionConstants.MB_ADDITIONS,
				  IVelocityActionConstants.COMMENT);
		addAction(aMenu, ITextEditorActionConstants.MB_ADDITIONS,
				  IVelocityActionConstants.UNCOMMENT);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#handleCursorPositionChanged()
	 */
	protected void handleCursorPositionChanged() {
		super.handleCursorPositionChanged();
		int line = getCursorLine();
		if (line > 0 && line != fLastCursorLine) {
			fLastCursorLine = line;
			if (fOutlinePage != null && !fOutlinePage.isDisposed()) {
//System.out.println("handleCursorPositionChanged: line=" + line);
				fOutlinePage.selectNode(line, false);
			}
		}
	}

	public IDocument getDocument() {
		ISourceViewer viewer = getSourceViewer();
		if (viewer != null) {
			return viewer.getDocument();
		}
		return null;
	}

	public int getLine(int anOffset) {
		int line;
		try {
			line = getDocument().getLineOfOffset(anOffset) + 1;
		} catch (BadLocationException e) {
			line = -1;
		}
		return line;
	}

	public int getCursorLine() {
		int line = -1;

		ISourceViewer sourceViewer = getSourceViewer();
		if (sourceViewer != null) {
			StyledText styledText = sourceViewer.getTextWidget();
			int caret = widgetOffset2ModelOffset(sourceViewer,
												 styledText.getCaretOffset());
			IDocument document = sourceViewer.getDocument();
			if (document != null) {
				try {
					line = document.getLineOfOffset(caret) + 1;
				} catch (BadLocationException e) {
					VelocityPlugin.log(e);
				}
			}
		}
		return line;
	}

	public void highlightNode(ITreeNode aNode, boolean aMoveCursor) {
	    IDocument doc = getDocument();
		try {
			int offset = doc.getLineOffset(aNode.getStartLine() - 1);
			IRegion endLine = doc.getLineInformation(aNode.getEndLine() - 1);
			int length = endLine.getOffset() + endLine.getLength() - offset;
			setHighlightRange(offset, length + 1, aMoveCursor);
		} catch (BadLocationException e) {
			resetHighlightRange();
		}
	}

	public void revealNode(ITreeNode aNode) {
		ISourceViewer viewer = getSourceViewer();
		if (viewer != null) {
		    IDocument doc = getDocument();
			try {
				int offset = doc.getLineOffset(aNode.getStartLine() - 1);
				IRegion endLine = doc.getLineInformation(aNode.getEndLine() - 1);
				int length = endLine.getOffset() + endLine.getLength() - offset;
				
				// Reveal segment's text area in document
				StyledText widget = getSourceViewer().getTextWidget();
				widget.setRedraw(false);
				viewer.revealRange(offset, length);
				widget.setRedraw(true);
			} catch (BadLocationException e) {
				resetHighlightRange();
			}
		}
	}

	public ITreeNode getNodeByLine(int aLine) {
		return fModelTools.getNodeByLine(aLine);
	}

	public String getDefinitionLine(IRegion aRegion) {
		if (aRegion != null) {
    		VelocityTextGuesser guess = new VelocityTextGuesser(getDocument(),
    											   aRegion.getOffset(), true);
			// Check if guessed text references an externally defined macro
    		if (guess.getType() == VelocityTextGuesser.TYPE_DIRECTIVE) {
    			VelocityMacro macro = VelocityEditorEnvironment.getParser().
    										  getLibraryMacro(guess.getText());
    			if (macro != null) {
					String template = ((IFileEditorInput)getEditorInput()).
														   getFile().getName();
					if (!macro.getTemplate().equals(template)) {
						StringBuffer buf = new StringBuffer();
						buf.append("#macro (");
						buf.append(macro.getName());
						buf.append(") - ");
						buf.append(macro.getTemplate());
						return buf.toString();
					}
    			}
    		}

			// Look through model tree for guessed text
			ITreeNode node = fModelTools.getNodeByGuess(guess);
			if (node != null) {
				IDocument doc = getDocument();
				try {
					aRegion = doc.getLineInformation(node.getStartLine() - 1);
					StringBuffer buf = new StringBuffer();
					buf.append(node.getStartLine());
					buf.append(": ");
					buf.append(doc.get(aRegion.getOffset(),
									   aRegion.getLength()).trim()); 
					return buf.toString();
				} catch(BadLocationException e) {
				}
			}
		}
		return null;
	}

	public void gotoDefinition(IRegion aRegion) {
		if (aRegion != null) {
    		VelocityTextGuesser guess = new VelocityTextGuesser(getDocument(),
    											   aRegion.getOffset(), true);
			// Check if guessed text references an externally defined macro
    		if (guess.getType() == VelocityTextGuesser.TYPE_DIRECTIVE) {
    			VelocityMacro macro = VelocityEditorEnvironment.getParser().
    										  getLibraryMacro(guess.getText());
    			if (macro != null) {
					String template = ((IFileEditorInput)getEditorInput()).
														   getFile().getName();
					if (!macro.getTemplate().equals(template)) {
						return;
					}
    			}
    		}

			// Look through model tree for guessed text
			ITreeNode node = fModelTools.getNodeByGuess(guess);
			if (node != null) {
				markInNavigationHistory();
				highlightNode(node, true);
				markInNavigationHistory();
			}
		}
	}

	/**
	 * Returns true if specified line belongs to a <code>#foreach</code> block.
	 */
	public boolean isLineWithinLoop(int aLine) {
		return fModelTools.isLineWithinLoop(aLine);
	}

	public List getVariables(int aLine) {
		return fModelTools.getVariables(aLine);
	}

	public List getMacros() {
		return fModelTools.getMacros();
	}

	public VelocityReconcilingStrategy getReconcilingStrategy() {
		return fReconcilingStrategy;
	}

	public Object[] getRootElements() {
		return fReconcilingStrategy.getRootElements();
	}

	public ITreeNode getRootNode() {
		return fReconcilingStrategy.getRootNode();
	}

	public ITreeNode getLastRootNode() {
		return fReconcilingStrategy.getLastRootNode();
	}

	public void updateOutlinePage() {
		if (fOutlinePage != null) {
			fOutlinePage.update();
		}
	}

	public void moveCursor(int aLine) {
		ISourceViewer sourceViewer = getSourceViewer();
		try {
			int offset = getDocument().getLineOffset(aLine - 1);
			sourceViewer.setSelectedRange(offset, 0);
			sourceViewer.revealRange(offset, 0);
		} catch (BadLocationException e) {
		}
	}
	
	/**
     * Determines if the specified character may be part of a Velocity
     * reference. A character may be part of a Velocity directive if and only if
     * it is one of the following:
     * <ul>
     * <li>a letter (a..z, A..Z)
     * <li>a digit (0..9)
     * <li>a hyphen ("-")
     * <li>a connecting punctuation character ("_")
     * </ul>
     * 
     * @param aChar  the character to be tested.
     * @return true if the character may be part of a Velocity reference;
     * 				 false otherwise.
     * @see java.lang.Character#isLetterOrDigit(char)
	 */
	public static boolean isReferencePart(char aChar) {
		return Character.isLetterOrDigit(aChar) || aChar == '-' ||
													aChar == '_';
	}
	
	/**
	 * Returns the desktop's StatusLineManager.
	 */
	protected IStatusLineManager getStatusLineManager() {
		IStatusLineManager manager;
		IEditorActionBarContributor contributor =
									 getEditorSite().getActionBarContributor();
		if (contributor != null &&
						  contributor instanceof EditorActionBarContributor) {
			manager = ((EditorActionBarContributor)contributor).
										getActionBars().getStatusLineManager();
		} else {
			manager = null;
		}
		return manager;
	}	

	/**
	 * Displays an error message in editor's status line.
	 */
	public void displayErrorMessage(String aMessage) {
		IStatusLineManager manager = getStatusLineManager();
		if (manager != null) {
			manager.setErrorMessage(aMessage);
		}
	}	

	public void addProblemMarker(String aMessage, int aLine) {
		IFile file = ((IFileEditorInput)getEditorInput()).getFile(); 
		try {
			IMarker marker = file.createMarker(IMarker.PROBLEM);
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			marker.setAttribute(IMarker.MESSAGE, aMessage);
			marker.setAttribute(IMarker.LINE_NUMBER, aLine);
			Position pos = new Position(getDocument().getLineOffset(aLine - 1));
			getSourceViewer().getAnnotationModel().addAnnotation(
											new MarkerAnnotation(marker), pos);
		} catch (Exception e) {
			VelocityPlugin.log(e);
		}
	}
}
