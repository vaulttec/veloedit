package org.vaulttec.velocity.ui;

/**
 * Defines constants which are used to refer to values in the plugin's
 * preference bundle.
 */
public interface IPreferencesConstants {
	String PREFIX_COLOR = "color.";

	String EDITOR_SHOW_SEGMENTS = "editor.showSegments";

	String COLOR_DEFAULT = PREFIX_COLOR + IColorConstants.DEFAULT;
	String COLOR_COMMENT = PREFIX_COLOR + IColorConstants.COMMENT;
	String COLOR_DOC_COMMENT = PREFIX_COLOR + IColorConstants.DOC_COMMENT;
	String COLOR_DIRECTIVE = PREFIX_COLOR + IColorConstants.DIRECTIVE;
	String COLOR_STRING = PREFIX_COLOR + IColorConstants.STRING;
	String COLOR_REFERENCE = PREFIX_COLOR + IColorConstants.REFERENCE;
	String COLOR_STRING_REFERENCE = PREFIX_COLOR + IColorConstants.STRING_REFERENCE;
}
