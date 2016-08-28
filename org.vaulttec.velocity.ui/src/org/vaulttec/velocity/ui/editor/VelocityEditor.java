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
import org.vaulttec.velocity.core.model.ITreeNode;
import org.vaulttec.velocity.core.parser.VelocityMacro;
import org.vaulttec.velocity.ui.VelocityUIPlugin;
import org.vaulttec.velocity.ui.editor.actions.GotoDefinitionAction;
import org.vaulttec.velocity.ui.editor.actions.IVelocityActionConstants;
import org.vaulttec.velocity.ui.editor.actions.IVelocityActionDefinitionIds;
import org.vaulttec.velocity.ui.editor.outline.VelocityOutlinePage;
import org.vaulttec.velocity.ui.editor.text.VelocityTextGuesser;

public class VelocityEditor extends TextEditor {
	private static final String PREFIX = "VelocityEditor.";

	private final ModelTools modelTools;
	private final VelocityReconcilingStrategy reconcilingStrategy;
	private VelocityOutlinePage outlinePage;

	/**
	 * Last cursor position (line) handled in
	 * <code>handleCursorPositionChanged()</code>
	 */
	private int lastCursorLine;

	public VelocityEditor() {
		this.modelTools = new ModelTools(this);
		this.reconcilingStrategy = new VelocityReconcilingStrategy(this);
	}

	@Override
	protected void initializeEditor() {
		VelocityEditorEnvironment.connect();
		
		super.initializeEditor();

		setDocumentProvider(new VelocityDocumentProvider());
		setSourceViewerConfiguration(new VelocitySourceViewerConfiguration(this));
	}

	@Override
	protected void initializeKeyBindingScopes() {
		setKeyBindingScopes(new String[] { "org.vaulttec.velocity.ui.velocityEditorScope" });
	}

	@Override
	protected void createActions() {
		super.createActions();

		// Add goto definition action
		IAction action = new GotoDefinitionAction(VelocityUIPlugin.getDefault().getResourceBundle(),
				PREFIX + "GotoDefinition.", this);
		action.setActionDefinitionId(IVelocityActionDefinitionIds.GOTO_DEFINITION);
		setAction(IVelocityActionConstants.GOTO_DEFINITION, action);

		// Add content assist propsal action
		action = new ContentAssistAction(VelocityUIPlugin.getDefault().getResourceBundle(), PREFIX + "ContentAssist.",
				this);
		action.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
		setAction(IVelocityActionConstants.CONTENT_ASSIST, action);

		// Add comment action
		action = new TextOperationAction(VelocityUIPlugin.getDefault().getResourceBundle(), PREFIX + "Comment.", this,
				ITextOperationTarget.PREFIX);
		action.setActionDefinitionId(IJavaEditorActionDefinitionIds.COMMENT);
		setAction(IVelocityActionConstants.COMMENT, action);

		// Add uncomment action
		action = new TextOperationAction(VelocityUIPlugin.getDefault().getResourceBundle(), PREFIX + "Uncomment.", this,
				ITextOperationTarget.STRIP_PREFIX);
		action.setActionDefinitionId(IJavaEditorActionDefinitionIds.UNCOMMENT);
		setAction(IVelocityActionConstants.UNCOMMENT, action);
	}

	/**
	 * Get the outline page if requested.
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable.getAdapter(Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class clazz) {
		Object adapter;
		if (clazz.equals(IContentOutlinePage.class)) {
			if (outlinePage == null || outlinePage.isDisposed()) {
				outlinePage = new VelocityOutlinePage(this);
				if (getEditorInput() != null) {
					outlinePage.setInput(getEditorInput());
				}
			}
			adapter = outlinePage;
		} else {
			adapter = super.getAdapter(clazz);
		}
		return adapter;
	}

	/**
	 * Disconnect from editor environment and dispose outline page.
	 *
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		if (outlinePage != null && !outlinePage.isDisposed()) {
			outlinePage.dispose();
			outlinePage = null;
		}
		VelocityEditorEnvironment.disconnect();
		super.dispose();
	}

	@Override
	protected void editorContextMenuAboutToShow(IMenuManager menu) {
		super.editorContextMenuAboutToShow(menu);
		addAction(menu, ITextEditorActionConstants.MB_ADDITIONS, IVelocityActionConstants.GOTO_DEFINITION);
		addAction(menu, ITextEditorActionConstants.MB_ADDITIONS, IVelocityActionConstants.COMMENT);
		addAction(menu, ITextEditorActionConstants.MB_ADDITIONS, IVelocityActionConstants.UNCOMMENT);
	}

	@Override
	protected void handleCursorPositionChanged() {
		super.handleCursorPositionChanged();
		int line = getCursorLine();
		if (line > 0 && line != lastCursorLine) {
			lastCursorLine = line;
			if (outlinePage != null && !outlinePage.isDisposed()) {
				outlinePage.selectNode(line, false);
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

	public int getLine(int offset) {
		int line;
		try {
			line = getDocument().getLineOfOffset(offset) + 1;
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
			int caret = widgetOffset2ModelOffset(sourceViewer, styledText.getCaretOffset());
			IDocument document = sourceViewer.getDocument();
			if (document != null) {
				try {
					line = document.getLineOfOffset(caret) + 1;
				} catch (BadLocationException e) {
					VelocityUIPlugin.log(e);
				}
			}
		}
		return line;
	}

	public void highlightNode(ITreeNode node, boolean moveCursor) {
		IDocument doc = getDocument();
		try {
			int offset = doc.getLineOffset(node.getStartLine() - 1);
			IRegion endLine = doc.getLineInformation(node.getEndLine() - 1);
			int length = endLine.getOffset() + endLine.getLength() - offset;
			setHighlightRange(offset, length + 1, moveCursor);
		} catch (BadLocationException e) {
			resetHighlightRange();
		}
	}

	public void revealNode(ITreeNode node) {
		ISourceViewer viewer = getSourceViewer();
		if (viewer != null) {
			IDocument doc = getDocument();
			try {
				int offset = doc.getLineOffset(node.getStartLine() - 1);
				IRegion endLine = doc.getLineInformation(node.getEndLine() - 1);
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

	public ITreeNode getNodeByLine(int line) {
		return modelTools.getNodeByLine(line);
	}

	public String getDefinitionLine(IRegion region) {
		if (region != null) {
			VelocityTextGuesser guess = new VelocityTextGuesser(getDocument(), region.getOffset(), true);
			// Check if guessed text references an externally defined macro
			if (guess.getType() == VelocityTextGuesser.TYPE_DIRECTIVE) {
				VelocityMacro macro = VelocityEditorEnvironment.getParser().getLibraryMacro(guess.getText());
				if (macro != null) {
					String template = ((IFileEditorInput) getEditorInput()).getFile().getName();
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
			ITreeNode node = modelTools.getNodeByGuess(guess);
			if (node != null) {
				IDocument doc = getDocument();
				try {
					region = doc.getLineInformation(node.getStartLine() - 1);
					StringBuffer buf = new StringBuffer();
					buf.append(node.getStartLine());
					buf.append(": ");
					buf.append(doc.get(region.getOffset(), region.getLength()).trim());
					return buf.toString();
				} catch (BadLocationException e) {
				}
			}
		}
		return null;
	}

	public void gotoDefinition(IRegion region) {
		if (region != null) {
			VelocityTextGuesser guess = new VelocityTextGuesser(getDocument(), region.getOffset(), true);
			// Check if guessed text references an externally defined macro
			if (guess.getType() == VelocityTextGuesser.TYPE_DIRECTIVE) {
				VelocityMacro macro = VelocityEditorEnvironment.getParser().getLibraryMacro(guess.getText());
				if (macro != null) {
					String template = ((IFileEditorInput) getEditorInput()).getFile().getName();
					if (!macro.getTemplate().equals(template)) {
						return;
					}
				}
			}

			// Look through model tree for guessed text
			ITreeNode node = modelTools.getNodeByGuess(guess);
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
	public boolean isLineWithinLoop(int line) {
		return modelTools.isLineWithinLoop(line);
	}

	public List<String> getVariables(int line) {
		return modelTools.getVariables(line);
	}

	public List<String> getMacros() {
		return modelTools.getMacros();
	}

	public VelocityReconcilingStrategy getReconcilingStrategy() {
		return reconcilingStrategy;
	}

	public Object[] getRootElements() {
		return reconcilingStrategy.getRootElements();
	}

	public ITreeNode getRootNode() {
		return reconcilingStrategy.getRootNode();
	}

	public ITreeNode getLastRootNode() {
		return reconcilingStrategy.getLastRootNode();
	}

	public void updateOutlinePage() {
		if (outlinePage != null) {
			outlinePage.update();
		}
	}

	public void moveCursor(int line) {
		ISourceViewer sourceViewer = getSourceViewer();
		try {
			int offset = getDocument().getLineOffset(line - 1);
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
	 * @param ch
	 *            the character to be tested.
	 * @return true if the character may be part of a Velocity reference; false
	 *         otherwise.
	 * @see java.lang.Character#isLetterOrDigit(char)
	 */
	public static boolean isReferencePart(char ch) {
		return Character.isLetterOrDigit(ch) || ch == '-' || ch == '_';
	}

	/**
	 * Returns the desktop's StatusLineManager.
	 */
	@Override
	protected IStatusLineManager getStatusLineManager() {
		IStatusLineManager manager;
		IEditorActionBarContributor contributor = getEditorSite().getActionBarContributor();
		if (contributor != null && contributor instanceof EditorActionBarContributor) {
			manager = ((EditorActionBarContributor) contributor).getActionBars().getStatusLineManager();
		} else {
			manager = null;
		}
		return manager;
	}

	/**
	 * Displays an error message in editor's status line.
	 */
	public void displayErrorMessage(String message) {
		IStatusLineManager manager = getStatusLineManager();
		if (manager != null) {
			manager.setErrorMessage(message);
		}
	}

	public void addProblemMarker(String message, int line) {
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		try {
			IMarker marker = file.createMarker(IMarker.PROBLEM);
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.LINE_NUMBER, line);
			Position pos = new Position(getDocument().getLineOffset(line - 1));
			getSourceViewer().getAnnotationModel().addAnnotation(new MarkerAnnotation(marker), pos);
		} catch (Exception e) {
			VelocityUIPlugin.log(e);
		}
	}

}
