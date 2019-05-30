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

import java.util.Stack;

import org.apache.velocity.runtime.parser.node.ASTDirective;
import org.apache.velocity.runtime.parser.node.ASTElseIfStatement;
import org.apache.velocity.runtime.parser.node.ASTElseStatement;
import org.apache.velocity.runtime.parser.node.ASTIfStatement;
import org.apache.velocity.runtime.parser.node.ASTReference;
import org.apache.velocity.runtime.parser.node.ASTSetDirective;
import org.apache.velocity.runtime.parser.node.ASTWord;
import org.apache.velocity.runtime.parser.node.ASTprocess;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.visitor.BaseVisitor;
import org.vaulttec.velocity.core.VelocityCorePlugin;
import org.vaulttec.velocity.core.model.Directive;
import org.vaulttec.velocity.core.model.IBlock;
import org.vaulttec.velocity.core.model.ITreeNode;
import org.vaulttec.velocity.core.model.Template;

public class NodeVisitor extends BaseVisitor {

	private org.apache.velocity.Template velocityTemplate;
	private Template template;
	private Stack<IBlock> blocks = new Stack<>();
	private IBlock currentBlock;

	public NodeVisitor(org.apache.velocity.Template velocityTemplate) {
		this.velocityTemplate = velocityTemplate;
	}

	public Template getTemplate() {
		return template;
	}

	@Override
	public Object visit(ASTprocess node, Object data) {
		template = new Template(velocityTemplate.getName());
		currentBlock = template;
		return super.visit(node, data);
	}

	@Override
	public Object visit(ASTDirective node, Object data) {
		String name = node.getDirectiveName();
		int type = Directive.getType('#' + name);
		Directive directive;
		String id = null;
		switch (type) {
		case Directive.TYPE_MACRO:
			if (node.jjtGetNumChildren() > 0 && node.jjtGetChild(0) instanceof ASTWord) {
				name = node.jjtGetChild(0).literal();
			} else {
				name = "";
			}
			id = name;
			break;

		case Directive.TYPE_FOREACH:
			if (node.jjtGetNumChildren() > 2) {
				name = node.jjtGetChild(2).literal();
			} else {
				name = "";
			}
			if (node.jjtGetNumChildren() > 0 && node.jjtGetChild(0) instanceof ASTReference) {
				id = node.jjtGetChild(0).literal();
			} else {
				id = "";
			}
			break;

		case Directive.TYPE_INCLUDE:
		case Directive.TYPE_PARSE:
			if (node.jjtGetNumChildren() > 0) {
				name = node.jjtGetChild(0).literal();
			} else {
				name = "";
			}
			id = "";
			break;

		case Directive.TYPE_MACRO_CALL:

			// Check if an already defined macro is referenced
			if (VelocityCorePlugin.getParser().isVelocimacro(name, velocityTemplate)) {
				id = "";
			} else {
				id = null;
			}
			break;

		case Directive.TYPE_USER_DIRECTIVE:
			id = "";
			break;
		}

		// If valid directive then visit embedded nodes too
		if (id != null) {
			directive = new Directive(type, name, id, (ITreeNode) currentBlock, node.getFirstToken().beginLine,
					node.getLastToken().endLine);
			data = visitBlockDirective(node, data, directive, false);

			// Add parameters of macro definition
			if (type == Directive.TYPE_MACRO && node.jjtGetNumChildren() > 1) {
				for (int i = 1; i < node.jjtGetNumChildren(); i++) {
					Node child = node.jjtGetChild(i);
					if (child instanceof ASTReference) {
						directive.addParameter(child.literal());
					} else {
						break;
					}
				}
			}
		}
		return data;
	}

	@Override
	public Object visit(ASTIfStatement node, Object data) {

		// Check for first embedded #ELSE or #ELSEIF directive to get end line
		int endLine = node.getLastToken().endLine;
		int numChildren = node.jjtGetNumChildren();
		for (int i = 1; i < numChildren; i++) {
			Node child = node.jjtGetChild(i);
			if (child instanceof ASTElseStatement || child instanceof ASTElseIfStatement) {
				endLine = child.getFirstToken().beginLine - 1;
				break;
			}
		}
		Directive directive = new Directive(Directive.TYPE_IF, node.jjtGetChild(0).literal(), "",
				(ITreeNode) currentBlock, node.getFirstToken().beginLine, endLine);
		return visitBlockDirective(node, data, directive, false);
	}

	@Override
	public Object visit(ASTElseStatement node, Object data) {
		Directive directive = new Directive(Directive.TYPE_ELSE, null, "", (ITreeNode) currentBlock,
				node.getFirstToken().beginLine, node.getLastToken().next.endLine);
		return visitBlockDirective(node, data, directive, true);
	}

	@Override
	public Object visit(ASTElseIfStatement node, Object data) {
		Directive directive = new Directive(Directive.TYPE_ELSEIF, node.jjtGetChild(0).literal(), "",
				(ITreeNode) currentBlock, node.getFirstToken().beginLine, node.getLastToken().endLine);
		return visitBlockDirective(node, data, directive, true);
	}

	@Override
	public Object visit(ASTSetDirective node, Object data) {
		String expr = node.jjtGetChild(0).literal();
		int pos = expr.indexOf('=');
		if (pos >= 0) {
			expr = expr.substring(0, pos).trim();
		}
		Directive directive = new Directive(Directive.TYPE_SET, expr, expr, (ITreeNode) currentBlock,
				node.getFirstToken().beginLine, node.getLastToken().endLine);
		currentBlock.addDirective(directive);
		return null;
	}

	private Object visitBlockDirective(Node node, Object data, Directive directive, boolean addToParentBlock) {
		if (addToParentBlock && currentBlock instanceof Directive) {
			IBlock parent = (IBlock) ((Directive) currentBlock).getParent();
			parent.addDirective(directive);
		} else {
			currentBlock.addDirective(directive);
		}
		blocks.push(currentBlock);
		currentBlock = directive;
		data = node.childrenAccept(this, data);
		currentBlock = blocks.pop();
		return data;
	}
}
