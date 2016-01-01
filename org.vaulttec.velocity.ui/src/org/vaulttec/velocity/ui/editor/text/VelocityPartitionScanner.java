package org.vaulttec.velocity.ui.editor.text;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordPatternRule;

/**
 * This scanner recognizes the Velocity strings and comments.
 */
public class VelocityPartitionScanner extends RuleBasedPartitionScanner {

	public final static String PARSED_STRING = "__parsed_string";
	public final static String UNPARSED_STRING = "__unparsed_string";
	public final static String SINGLE_LINE_COMMENT = "__singleline_comment";
	public final static String MULTI_LINE_COMMENT = "__multiline_comment";
	public final static String DOC_COMMENT = "__doc_comment";

	public final static String[] TYPES = new String[] {
			IDocument.DEFAULT_CONTENT_TYPE, SINGLE_LINE_COMMENT,
			MULTI_LINE_COMMENT, DOC_COMMENT, PARSED_STRING, UNPARSED_STRING };
	
	/**
	 * Creates the partitioner and sets up the appropriate rules.
	 */
	public VelocityPartitionScanner() {
		List rules = new ArrayList();

		// Add rule for single line comments
		rules.add(new EndOfLineRule("##", new Token(SINGLE_LINE_COMMENT)));

		// Add rule for strings
		rules.add(new SingleLineRule("\"", "\"",
									 new Token(PARSED_STRING), '\\'));
		// Add rule for character constants.
		rules.add(new SingleLineRule("'", "'",
									 new Token(UNPARSED_STRING), '\\'));
		// Add rules for multi-line comments and doc comments
		rules.add(new MultiLineRule("#**", "*#", new Token(DOC_COMMENT)));
		rules.add(new MultiLineRule("#*", "*#", new Token(MULTI_LINE_COMMENT)));

		// Add special empty comment word rules
		rules.add(new WordPatternRule(new EmptyCommentDetector(), "#***#", null,
									  new Token(DOC_COMMENT)));
		rules.add(new WordPatternRule(new EmptyCommentDetector(), "#**#", null,
									  new Token(MULTI_LINE_COMMENT)));

		IPredicateRule[] result = new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}
}
