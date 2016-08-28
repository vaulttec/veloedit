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
import org.vaulttec.velocity.ui.IVelocityPreferencesConstants;
import org.vaulttec.velocity.ui.VelocityUIPlugin;

public class VelocityStringScanner extends RuleBasedScanner {

	public VelocityStringScanner() {
		List<IRule> rules = new ArrayList<IRule>();

		// Add generic whitespace rule
		rules.add(new WhitespaceRule(new WhitespaceDetector()));

		// Add pattern rule for formal references
		Token token = new Token(new TextAttribute(
				VelocityUIPlugin.getPreferenceColor(IVelocityPreferencesConstants.COLOR_STRING_REFERENCE)));
		rules.add(new PatternRule("$!{", "}", token, (char) 0, true));
		rules.add(new PatternRule("${", "}", token, (char) 0, true));

		// Add pattern rule for shorthand references
		token = new Token(new TextAttribute(
				VelocityUIPlugin.getPreferenceColor(IVelocityPreferencesConstants.COLOR_STRING_REFERENCE)));
		rules.add(new WordPatternRule(new IdentifierDetector(), "$!", null, token));
		rules.add(new WordPatternRule(new IdentifierDetector(), "$", null, token));

		IRule[] result = rules.toArray(new IRule[rules.size()]);
		setRules(result);

		setDefaultReturnToken(new Token(
				new TextAttribute(VelocityUIPlugin.getPreferenceColor(IVelocityPreferencesConstants.COLOR_STRING))));
	}

	private class WhitespaceDetector implements IWhitespaceDetector {
		public boolean isWhitespace(char c) {
			return (c == ' ' || c == '\t');
		}
	}

}
