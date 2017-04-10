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
import java.util.List;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordPatternRule;

/**
 * This {@link IPartitionTokenScanner} recognizes the Velocity strings and comments.
 */
public class VelocityPartitionScanner extends RuleBasedPartitionScanner implements IVelocityPartitions {

	/**
	 * Creates the partitioner and sets up the appropriate rules.
	 */
	public VelocityPartitionScanner() {
		List<IPredicateRule> rules = new ArrayList<>();

		// Add rule for single line comments
		rules.add(new EndOfLineRule("##", new Token(SINGLE_LINE_COMMENT)));

		// Add rule for strings
		rules.add(new SingleLineRule("\"", "\"", new Token(PARSED_STRING), '\\'));
		// Add rule for character constants.
		rules.add(new SingleLineRule("'", "'", new Token(UNPARSED_STRING), '\\'));
		// Add rules for multi-line comments and doc comments
		rules.add(new MultiLineRule("#**", "*#", new Token(DOC_COMMENT)));
		rules.add(new MultiLineRule("#*", "*#", new Token(MULTI_LINE_COMMENT)));

		// Add special empty comment word rules
		rules.add(new WordPatternRule(new EmptyCommentDetector(), "#***#", null, new Token(DOC_COMMENT)));
		rules.add(new WordPatternRule(new EmptyCommentDetector(), "#**#", null, new Token(MULTI_LINE_COMMENT)));

		IPredicateRule[] result = new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}
}
