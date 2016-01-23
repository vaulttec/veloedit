package org.vaulttec.velocity.core.parser;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

/**
 * Dummy implementation of a Velocity user directive. It only provides a name
 * and a type but no rendering.
 */
public class VelocityDirective extends Directive {
	private String fName;
	private int fType;

	public VelocityDirective(String aName, int aType) {
		fName = aName;
		fType = aType;
	}

	public String getName() {
		return fName;
	}

	public int getType() {
		return fType;
	}

	public boolean render(InternalContextAdapter aContext, Writer aWriter,
							Node aNode) throws IOException,
								ResourceNotFoundException, ParseErrorException,
								MethodInvocationException {
		return true;
	}
}
