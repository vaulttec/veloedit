package org.vaulttec.velocity.ui.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.vaulttec.velocity.core.model.Directive;
import org.vaulttec.velocity.core.parser.VelocityMacro;
import org.vaulttec.velocity.ui.VelocityPluginImages;
import org.vaulttec.velocity.ui.editor.text.VelocityTextGuesser;

public class VelocityCompletionProcessor implements IContentAssistProcessor {

	private VelocityEditor fEditor;
	private boolean fCompleteDirectives;

	private static Comparator PROPOSAL_COMPARATOR = new Comparator() {
		public int compare(Object aProposal1, Object aProposal2) {
			String text1 = ((CompletionProposal)aProposal1).getDisplayString();
			String text2 = ((CompletionProposal)aProposal2).getDisplayString();
			return text1.compareTo(text2);
		}

		public boolean equals(Object aProposal) {
			return false;
		}
	};

	public VelocityCompletionProcessor(VelocityEditor anEditor,
										boolean aCompleteDirectives) {
		fEditor = anEditor;
		fCompleteDirectives = aCompleteDirectives;
	}

	/**
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#computeCompletionProposals(org.eclipse.jface.text.ITextViewer, int)
	 */
	public ICompletionProposal[] computeCompletionProposals(
										  ITextViewer aViewer, int anOffset) {
		ICompletionProposal[] proposals = null;		
		IDocument doc = aViewer.getDocument();
		VelocityTextGuesser prefix = new VelocityTextGuesser(doc, anOffset,
															 false);
		if (prefix.getType() == VelocityTextGuesser.TYPE_DIRECTIVE) {
			if (fCompleteDirectives) {
				proposals = getDirectiveProposals(prefix.getText(), anOffset -
												  prefix.getText().length());
			}
		} else if (prefix.getType() == VelocityTextGuesser.TYPE_VARIABLE) {
			proposals = getVariableProposals(prefix.getText(), anOffset -
											 prefix.getText().length());
		}
		return proposals;
	}

	/**
	 * Returns proposals from all directives with given prefix.
	 */
	private ICompletionProposal[] getDirectiveProposals(String aPrefix,
														 int anOffset) {
		List proposals = new ArrayList();

		// Add system directives
		String[] directives = Directive.DIRECTIVES;
		for (int i = directives.length - 1; i >= 0; i--) {
			String directive = directives[i];
			if (directive.substring(1).startsWith(aPrefix)) {
				int cursorPos;
				if (i == Directive.TYPE_ELSE || i == Directive.TYPE_END ||
					 								i == Directive.TYPE_STOP) {
					cursorPos = directive.length() - 1;
				} else {
					directive += "()";
					cursorPos = directive.length() - 2;
				}
				proposals.add(new CompletionProposal(directive.substring(1),
							anOffset, aPrefix.length(), cursorPos,
							VelocityPluginImages.get(
								VelocityPluginImages.IMG_OBJ_SYSTEM_DIRECTIVE),
							directive, null, null)); 
			}
		}

		// Add Velocity library macros
		Iterator macros = VelocityEditorEnvironment.getParser().
												 getLibraryMacros().iterator();
		while (macros.hasNext()) {
			VelocityMacro macro = ((VelocityMacro)macros.next());
			String name = macro.getName();
			if (name.startsWith(aPrefix)) {
				String insert = name + "()";
				int cursorPos;
				StringBuffer buffer = new StringBuffer();
				buffer.append('#');
				buffer.append(name);
				buffer.append('(');
				if (macro.getArguments().length == 1) {
					cursorPos = insert.length();
					buffer.append(')');
				} else {
					cursorPos = insert.length() - 1;
					String args[] = macro.getArguments();
					for (int i = 1; i < args.length; i++) {
						buffer.append('$');
						buffer.append(args[i]);
						if (i < (args.length - 1)) {
							buffer.append(" ");
						}
					}
					buffer.append(')');
				}
				buffer.append(" - ");
				buffer.append(macro.getTemplate());
				proposals.add(new CompletionProposal(insert, anOffset,
							aPrefix.length(), cursorPos,
							VelocityPluginImages.get(
										   VelocityPluginImages.IMG_OBJ_MACRO),
							buffer.toString(), null, null)); 
			}
		}

		// Add user directives
		Iterator userDirectives = VelocityEditorEnvironment.getParser().
											    getUserDirectives().iterator();
		while (userDirectives.hasNext()) {
			String directive = ((String)userDirectives.next());
			if (directive.substring(1).startsWith(aPrefix)) {
				directive += "()";
				int cursorPos = directive.length() - 1;
				proposals.add(new CompletionProposal(directive.substring(1),
							anOffset, aPrefix.length(), cursorPos,
							VelocityPluginImages.get(
								  VelocityPluginImages.IMG_OBJ_USER_DIRECTIVE),
							directive, null, null)); 
			}
		}
		Collections.sort(proposals, PROPOSAL_COMPARATOR);
		return (ICompletionProposal[])proposals.toArray(
									new ICompletionProposal[proposals.size()]);
	}

	/**
	 * Returns proposals from all variables with given prefix.
	 */
	private ICompletionProposal[] getVariableProposals(String aPrefix,
														int anOffset) {
		ICompletionProposal[] result = null;
		List variables = fEditor.getVariables(fEditor.getLine(anOffset));
		if (!variables.isEmpty()) {
			List proposals = new ArrayList();
			Iterator iter = variables.iterator();
			while (iter.hasNext()) {
				String variable = (String)iter.next();
				if (variable.substring(1).startsWith(aPrefix)) {
					proposals.add(new CompletionProposal(variable.substring(1),
							 anOffset, aPrefix.length(), variable.length() - 1,
							 null, variable, null, null)); 
				}
			}
			Collections.sort(proposals, PROPOSAL_COMPARATOR);
			result = (ICompletionProposal[])proposals.toArray(
							new ICompletionProposal[proposals.size()]);
		}
		return result;
	}

	/**
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#computeContextInformation(org.eclipse.jface.text.ITextViewer, int)
	 */
	public IContextInformation[] computeContextInformation(ITextViewer viewer,
														  int documentOffset) {
		return null;
	}
	
	/**
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getCompletionProposalAutoActivationCharacters()
	 */
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] { '#', '$' };
	}
	
	/**
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getContextInformationAutoActivationCharacters()
	 */
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}
	
	/**
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getContextInformationValidator()
	 */
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}
	
	/**
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getErrorMessage()
	 */
	public String getErrorMessage() {
		return null;
	}
}
