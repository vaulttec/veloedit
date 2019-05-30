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
package org.vaulttec.velocity.core.parser;

import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.runtime.directive.Macro;

/**
 * Container used to store Velocity macro information.
 */
public class VelocityMacro {
	protected String name;
	protected List<Macro.MacroArg> arguments;
	protected Template template;

	public VelocityMacro(String name, List<Macro.MacroArg> arguments, Template template) {
		this.name = name;
		this.arguments = arguments;
		this.template = template;
	}

	public String getName() {
		return name;
	}

	public List<Macro.MacroArg> getArguments() {
		return arguments;
	}

	public Template getTemplate() {
		return template;
	}

}
