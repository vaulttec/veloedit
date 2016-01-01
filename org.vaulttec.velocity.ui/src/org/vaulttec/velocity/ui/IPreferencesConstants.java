package org.vaulttec.velocity.ui;

/**
 * Defines constants which are used to refer to values in the plugin's
 * preference bundle.
 */
public interface IPreferencesConstants {
	String PREFIX = VelocityPlugin.PLUGIN_ID + ".";
	String PREFIX_COLOR = PREFIX + "color.";

	String EDITOR_SHOW_SEGMENTS = PREFIX + "editor.showSegments";

	String VELOCITY_COUNTER_NAME = PREFIX + "velocity.countName";
	String VELOCITY_USER_DIRECTIVES = PREFIX + "velocity.userDirectives";

	String LIBRARY_PATH = PREFIX + "library.path";
	String LIBRARY_LIST = PREFIX + "library.list";

	String COLOR_DEFAULT = PREFIX_COLOR + IColorConstants.DEFAULT;
	String COLOR_COMMENT = PREFIX_COLOR + IColorConstants.COMMENT;
	String COLOR_DOC_COMMENT = PREFIX_COLOR + IColorConstants.DOC_COMMENT;
	String COLOR_DIRECTIVE = PREFIX_COLOR + IColorConstants.DIRECTIVE;
	String COLOR_STRING = PREFIX_COLOR + IColorConstants.STRING;
	String COLOR_REFERENCE = PREFIX_COLOR + IColorConstants.REFERENCE;
	String COLOR_STRING_REFERENCE = PREFIX_COLOR +
											  IColorConstants.STRING_REFERENCE;
}
