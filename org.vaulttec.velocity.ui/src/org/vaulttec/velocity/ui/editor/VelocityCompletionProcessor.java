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

	private final VelocityEditor editor;
	private final boolean completeDirectives;

	private static Comparator<CompletionProposal> PROPOSAL_COMPARATOR = new Comparator<CompletionProposal>() {
		@Override
		public int compare(CompletionProposal proposal1, CompletionProposal proposal2) {
			String text1 = proposal1.getDisplayString();
			String text2 = proposal2.getDisplayString();
			return text1.compareTo(text2);
		}

		@Override
		public boolean equals(Object aProposal) {
			return false;
		}
	};

	public VelocityCompletionProcessor(VelocityEditor editor, boolean completeDirectives) {
		this.editor = editor;
		this.completeDirectives = completeDirectives;
	}

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		ICompletionProposal[] proposals = null;
		IDocument doc = viewer.getDocument();
		VelocityTextGuesser prefix = new VelocityTextGuesser(doc, offset, false);
		if (prefix.getType() == VelocityTextGuesser.TYPE_DIRECTIVE) {
			if (completeDirectives) {
				proposals = getDirectiveProposals(prefix.getText(), offset - prefix.getText().length());
			}
		} else if (prefix.getType() == VelocityTextGuesser.TYPE_VARIABLE) {
			proposals = getVariableProposals(prefix.getText(), offset - prefix.getText().length());
		}
		return proposals;
	}

	/**
	 * Returns proposals from all directives with given prefix.
	 */
	private ICompletionProposal[] getDirectiveProposals(String prefix, int offset) {
		List<CompletionProposal> proposals = new ArrayList<CompletionProposal>();

		// Add system directives
		String[] directives = Directive.DIRECTIVES;
		for (int i = directives.length - 1; i >= 0; i--) {
			String directive = directives[i];
			if (directive.substring(1).startsWith(prefix)) {
				int cursorPos;
				if (i == Directive.TYPE_ELSE || i == Directive.TYPE_END || i == Directive.TYPE_STOP) {
					cursorPos = directive.length() - 1;
				} else {
					directive += "()";
					cursorPos = directive.length() - 2;
				}
				proposals.add(new CompletionProposal(directive.substring(1), offset, prefix.length(), cursorPos,
						VelocityPluginImages.get(VelocityPluginImages.IMG_OBJ_SYSTEM_DIRECTIVE), directive, null,
						null));
			}
		}

		// Add Velocity library macros
		Iterator<VelocityMacro> macros = VelocityEditorEnvironment.getParser().getLibraryMacros().iterator();
		while (macros.hasNext()) {
			VelocityMacro macro = macros.next();
			String name = macro.getName();
			if (name.startsWith(prefix)) {
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
				proposals.add(new CompletionProposal(insert, offset, prefix.length(), cursorPos,
						VelocityPluginImages.get(VelocityPluginImages.IMG_OBJ_MACRO), buffer.toString(), null, null));
			}
		}

		// Add user directives
		Iterator<String> userDirectives = VelocityEditorEnvironment.getParser().getUserDirectives().iterator();
		while (userDirectives.hasNext()) {
			String directive = ((String) userDirectives.next());
			if (directive.substring(1).startsWith(prefix)) {
				directive += "()";
				int cursorPos = directive.length() - 1;
				proposals.add(new CompletionProposal(directive.substring(1), offset, prefix.length(), cursorPos,
						VelocityPluginImages.get(VelocityPluginImages.IMG_OBJ_USER_DIRECTIVE), directive, null, null));
			}
		}
		Collections.sort(proposals, PROPOSAL_COMPARATOR);
		return proposals.toArray(new ICompletionProposal[proposals.size()]);
	}

	/**
	 * Returns proposals from all variables with given prefix.
	 */
	private ICompletionProposal[] getVariableProposals(String prefix, int offset) {
		ICompletionProposal[] result = null;
		List<String> variables = editor.getVariables(editor.getLine(offset));
		if (!variables.isEmpty()) {
			List<CompletionProposal> proposals = new ArrayList<CompletionProposal>();
			Iterator<String> iter = variables.iterator();
			while (iter.hasNext()) {
				String variable = iter.next();
				if (variable.substring(1).startsWith(prefix)) {
					proposals.add(new CompletionProposal(variable.substring(1), offset, prefix.length(),
							variable.length() - 1, null, variable, null, null));
				}
			}
			Collections.sort(proposals, PROPOSAL_COMPARATOR);
			result = proposals.toArray(new ICompletionProposal[proposals.size()]);
		}
		return result;
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int documentOffset) {
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] { '#', '$' };
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

}
