package org.vaulttec.velocity.ui.editor.text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordPatternRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.vaulttec.velocity.ui.IColorConstants;
import org.vaulttec.velocity.ui.VelocityColorProvider;
import org.vaulttec.velocity.ui.editor.VelocityEditorEnvironment;
import org.vaulttec.velocity.ui.model.Directive;

public class VelocityCodeScanner extends RuleBasedScanner {

	public VelocityCodeScanner(VelocityColorProvider aProvider) {
		List rules = new ArrayList();		

		// Add generic whitespace rule
		rules.add(new WhitespaceRule(new WhitespaceDetector()));

		// Add word rule for directives
		Token token = new Token(new TextAttribute(aProvider.getColor(
										  IColorConstants.DIRECTIVE)));
		WordRule wordRule = new WordRule(new DirectiveDetector(), token);
		token = new Token(new TextAttribute(aProvider.getColor(
											 IColorConstants.DIRECTIVE),
											 null, SWT.BOLD));
		// System directives
		String[] directives = Directive.DIRECTIVES;
		for (int i = directives.length - 1; i >= 0; i--) {
			wordRule.addWord(directives[i], token);
		}

		// User directives
		Iterator userDirectives = VelocityEditorEnvironment.getParser().
											    getUserDirectives().iterator();
		while (userDirectives.hasNext()) {
			wordRule.addWord((String)userDirectives.next(), token);
		}
		rules.add(wordRule);

		// Add pattern rule for formal references
		token = new Token(new TextAttribute(aProvider.getColor(
										  IColorConstants.REFERENCE)));
		rules.add(new PatternRule("$!{", "}", token, (char)0, true));
		rules.add(new PatternRule("${", "}", token, (char)0, true));

		// Add pattern rule for shorthand references
		token = new Token(new TextAttribute(aProvider.getColor(
										  IColorConstants.REFERENCE)));
		rules.add(new WordPatternRule(new IdentifierDetector(), "$!", null,
									  token));
		rules.add(new WordPatternRule(new IdentifierDetector(), "$", null,
									  token));

		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);
	}
}
