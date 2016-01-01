package org.vaulttec.velocity.ui.editor.parser;

/**
 * Container used to store Velocity macro information.
 */
public class VelocityMacro {
	protected String fName;
	protected String[] fArguments;
	protected String fTemplate;

	public VelocityMacro(String aName, String[] anArguments,
														    String aTemplate) {
		fName = aName;
		fArguments = anArguments;
		fTemplate = aTemplate;
	}

	public String getName() {
		return fName;
	}

	public String[] getArguments() {
		return fArguments;
	}

	public String getTemplate() {
		return fTemplate;
	}
}
