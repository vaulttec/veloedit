package org.vaulttec.velocity.ui.editor.text;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordPatternRule;
import org.vaulttec.velocity.ui.IColorConstants;
import org.vaulttec.velocity.ui.VelocityColorProvider;

public class VelocityStringScanner extends RuleBasedScanner {

	public VelocityStringScanner(VelocityColorProvider aProvider) {
		List rules = new ArrayList();		
		
		// Add generic whitespace rule
		rules.add(new WhitespaceRule(new WhitespaceDetector()));

		// Add pattern rule for formal references
		Token token = new Token(new TextAttribute(aProvider.getColor(
								   IColorConstants.STRING_REFERENCE)));
		rules.add(new PatternRule("$!{", "}", token, (char)0, true));
		rules.add(new PatternRule("${", "}", token, (char)0, true));

		// Add pattern rule for shorthand references
		token = new Token(new TextAttribute(aProvider.getColor(
								   IColorConstants.STRING_REFERENCE)));
		rules.add(new WordPatternRule(new IdentifierDetector(), "$!", null,
									  token));
		rules.add(new WordPatternRule(new IdentifierDetector(), "$", null,
									  token));

		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);

		setDefaultReturnToken(new Token(new TextAttribute(aProvider.getColor(
											IColorConstants.STRING))));
	}

	private class WhitespaceDetector implements IWhitespaceDetector {
		public boolean isWhitespace(char aChar) {
			return (aChar == ' ' || aChar == '\t');
		}
	}
}
