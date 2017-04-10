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
package org.vaulttec.velocity.core.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.vaulttec.velocity.core.VelocityCorePlugin;

public class Directive extends AbstractTreeNode implements IBlock {

	public static final int TYPE_SET = 0;
	public static final int TYPE_IF = 1;
	public static final int TYPE_ELSE = 2;
	public static final int TYPE_ELSEIF = 3;
	public static final int TYPE_END = 4;
	public static final int TYPE_FOREACH = 5;
	public static final int TYPE_INCLUDE = 6;
	public static final int TYPE_PARSE = 7;
	public static final int TYPE_MACRO = 8;
	public static final int TYPE_STOP = 9;
	public static final int TYPE_MACRO_CALL = 10;
	public static final int TYPE_USER_DIRECTIVE = 11;

	public static final String DIRECTIVES[] = { "#set", "#if", "#else", "#elseif", "#end", "#foreach", "#include",
			"#parse", "#macro", "#stop" };

	private int type;
	private String name;
	private String id;

	/** List of parameters of Velocity macro */
	private List<String> parameters;

	/** List of embedded directives of Velocity macro */
	private List<Directive> directives = new ArrayList<>();

	public Directive(int type, String name, String id, ITreeNode parent, int startLine, int endLine) {
		super(parent, startLine, endLine);
		this.type = type;
		this.name = name;
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public static int getType(String name) {
		for (int i = 0; i < DIRECTIVES.length; i++) {
			if (Directive.DIRECTIVES[i].equals(name)) {
				return i;
			}
		}
		if (VelocityCorePlugin.getParser().isUserDirective(name)) {
			return TYPE_USER_DIRECTIVE;
		}
		return TYPE_MACRO_CALL;
	}

	public String getId() {
		return id;
	}

	public void addParameter(String parameter) {
		if (parameters == null) {
			parameters = new ArrayList<>();
		}
		parameters.add(parameter);
	}

	public List<String> getParameters() {
		return parameters;
	}

	@Override
	public void addDirective(Directive aDirective) {
		directives.add(aDirective);
	}

	@Override
	public String getName() {
		return (type < TYPE_MACRO_CALL ? DIRECTIVES[type] + (type != TYPE_ELSE ? " (" + name + ")" : "") : "#" + name);
	}

	@Override
	public boolean hasChildren() {
		return !directives.isEmpty();
	}

	@Override
	public Object[] getChildren() {
		return directives.toArray();
	}

	@Override
	public boolean accept(ITreeVisitor visitor) {
		boolean more = true;

		// Visit all embedded directives of this directive
		Iterator<Directive> iter = directives.iterator();
		while (more && iter.hasNext()) {
			more = iter.next().accept(visitor);
		}

		// Finally visit this directive
		if (more) {
			more = visitor.visit(this);
		}
		return more;
	}

	@Override
	public String getUniqueID() {
		return getName() + ":" + getStartLine();
	}

	@Override
	public String toString() {
		return getUniqueID() + ":" + getEndLine() + " with directive(s) " + directives;
	}
}
