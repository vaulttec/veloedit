package org.vaulttec.velocity.ui;

/**
 * Defines constants which are used to refer to values in the plugin's
 * preference bundle.
 */
public interface IVelocityPreferencesConstants {
	String PREFIX_COLOR = "color.";

	String EDITOR_SHOW_SEGMENTS = "editor.showSegments";

	String COLOR_DEFAULT = PREFIX_COLOR + "default";
	String COLOR_COMMENT = PREFIX_COLOR + "comment";
	String COLOR_DOC_COMMENT = PREFIX_COLOR + "doc_comment";
	String COLOR_DIRECTIVE = PREFIX_COLOR + "directive";
	String COLOR_STRING = PREFIX_COLOR + "string";
	String COLOR_REFERENCE = PREFIX_COLOR + "reference";
	String COLOR_STRING_REFERENCE = PREFIX_COLOR + "string_reference";
}
