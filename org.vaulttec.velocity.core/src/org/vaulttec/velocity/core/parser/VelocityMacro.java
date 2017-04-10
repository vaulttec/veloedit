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
