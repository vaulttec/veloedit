package org.vaulttec.velocity.ui.editor.text;

/**
 * Velocity file partitioning definition.
 * <p>
 * The Velocity directives / macros and the template text is represented by the
 * {@link org.eclipse.jface.text.IDocument#DEFAULT_CONTENT_TYPE default
 * partition}.
 */
public interface IVelocityPartitions {

	String VELOCITY_PARTITIONING = "___vtl_partitioning";

	String SINGLE_LINE_COMMENT = "__vtl_singleline_comment";
	String MULTI_LINE_COMMENT = "__vtl_multiline_comment";
	String DOC_COMMENT = "__vtl_doc_comment";

	String PARSED_STRING = "__vtl_parsed_string";
	String UNPARSED_STRING = "__vtl_unparsed_string";

	String[] PARTITIONS = new String[] { SINGLE_LINE_COMMENT, MULTI_LINE_COMMENT, DOC_COMMENT, PARSED_STRING,
			UNPARSED_STRING };
}
