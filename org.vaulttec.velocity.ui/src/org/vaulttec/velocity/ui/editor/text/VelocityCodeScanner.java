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
import org.vaulttec.velocity.core.model.Directive;
import org.vaulttec.velocity.ui.IVelocityPreferencesConstants;
import org.vaulttec.velocity.ui.VelocityUIPlugin;
import org.vaulttec.velocity.ui.editor.VelocityEditorEnvironment;

public class VelocityCodeScanner extends RuleBasedScanner {

	public VelocityCodeScanner() {
		List<IRule> rules = new ArrayList<IRule>();

		// Add generic whitespace rule
		rules.add(new WhitespaceRule(new WhitespaceDetector()));

		// Add word rule for directives
		Token token = new Token(
				new TextAttribute(VelocityUIPlugin.getPreferenceColor(IVelocityPreferencesConstants.COLOR_DIRECTIVE)));
		WordRule wordRule = new WordRule(new DirectiveDetector(), token);
		token = new Token(new TextAttribute(
				VelocityUIPlugin.getPreferenceColor(IVelocityPreferencesConstants.COLOR_DIRECTIVE), null, SWT.BOLD));

		// System directives
		String[] directives = Directive.DIRECTIVES;
		for (int i = directives.length - 1; i >= 0; i--) {
			wordRule.addWord(directives[i], token);
		}

		// User directives
		Iterator<String> userDirectives = VelocityEditorEnvironment.getParser().getUserDirectives().iterator();
		while (userDirectives.hasNext()) {
			wordRule.addWord((String) userDirectives.next(), token);
		}
		rules.add(wordRule);

		// Add pattern rule for formal references
		token = new Token(
				new TextAttribute(VelocityUIPlugin.getPreferenceColor(IVelocityPreferencesConstants.COLOR_REFERENCE)));
		rules.add(new PatternRule("$!{", "}", token, (char) 0, true));
		rules.add(new PatternRule("${", "}", token, (char) 0, true));

		// Add pattern rule for shorthand references
		token = new Token(
				new TextAttribute(VelocityUIPlugin.getPreferenceColor(IVelocityPreferencesConstants.COLOR_REFERENCE)));
		rules.add(new WordPatternRule(new IdentifierDetector(), "$!", null, token));
		rules.add(new WordPatternRule(new IdentifierDetector(), "$", null, token));

		IRule[] result = rules.toArray(new IRule[rules.size()]);
		setRules(result);
	}

}
