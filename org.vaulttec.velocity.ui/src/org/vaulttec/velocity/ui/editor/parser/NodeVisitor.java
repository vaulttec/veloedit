package org.vaulttec.velocity.ui.editor.parser;

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
import org.vaulttec.velocity.ui.editor.VelocityEditorEnvironment;
import org.vaulttec.velocity.ui.model.Directive;
import org.vaulttec.velocity.ui.model.IBlock;
import org.vaulttec.velocity.ui.model.ITreeNode;
import org.vaulttec.velocity.ui.model.Template;

public class NodeVisitor extends BaseVisitor {

	private String fName;
	private Template fTemplate;
	private Stack fBlocks = new Stack();
	private IBlock fCurrentBlock;

	public NodeVisitor(String aName) {
		fName = aName;
	}

	public Template getTemplate() {
		return fTemplate;
	}

	/**
	 * @see org.apache.velocity.runtime.parser.node.ParserVisitor#visit(org.apache.velocity.runtime.parser.node.ASTprocess, java.lang.Object)
	 */
	public Object visit(ASTprocess aNode, Object aData) {
		fTemplate = new Template(fName);
		fCurrentBlock = fTemplate;
		return super.visit(aNode, aData);
	}

	/**
	 * @see org.apache.velocity.runtime.parser.node.ParserVisitor#visit(org.apache.velocity.runtime.parser.node.ASTDirective, java.lang.Object)
	 */
	public Object visit(ASTDirective aNode, Object aData) {
		String name = aNode.getDirectiveName();
		int type = Directive.getType('#' + name);
		Directive directive;
		String id = null;
		switch (type) {
			case Directive.TYPE_MACRO :
				if (aNode.jjtGetNumChildren() > 0 &&
									aNode.jjtGetChild(0) instanceof ASTWord) {
					name = aNode.jjtGetChild(0).literal();
				} else {
					name = "";
				}
				id = name;
				break;

			case Directive.TYPE_FOREACH :
				if (aNode.jjtGetNumChildren() > 2) {
					name = aNode.jjtGetChild(2).literal();
				} else {
					name = "";
				}
				if (aNode.jjtGetNumChildren() > 0 &&
							   aNode.jjtGetChild(0) instanceof ASTReference) {
					id = aNode.jjtGetChild(0).literal();
				} else {
					id = "";
				}
				break;

			case Directive.TYPE_INCLUDE :
			case Directive.TYPE_PARSE :
				if (aNode.jjtGetNumChildren() > 0) {
					name = aNode.jjtGetChild(0).literal();
				} else {
					name = "";
				}
				id = "";
				break;

			case Directive.TYPE_MACRO_CALL :

				// Check if an already defined macro is referenced
				if (VelocityEditorEnvironment.getParser().isVelocimacro(name,
														fTemplate.getName())) {
					id = "";
				} else {
					id = null;
				}
				break;

			case Directive.TYPE_USER_DIRECTIVE :
				id = "";
				break;
		}

		// If valid directive then visit embedded nodes too 
		if (id != null) {
			directive = new Directive(type, name, id,
									  (ITreeNode)fCurrentBlock,
									  aNode.getFirstToken().beginLine,
									  aNode.getLastToken().endLine);
			aData = visitBlockDirective(aNode, aData, directive, false);

			// Add parameters of macro definition
			if (type == Directive.TYPE_MACRO && aNode.jjtGetNumChildren() > 1) {
				for (int i = 1; i < aNode.jjtGetNumChildren(); i++) {
					Node node = aNode.jjtGetChild(i);
					if (node instanceof ASTReference) {
						directive.addParameter(node.literal());
					} else {
						break;
					}
				}
			}
		}
		return aData;
	}

	/**
	 * @see org.apache.velocity.runtime.parser.node.ParserVisitor#visit(org.apache.velocity.runtime.parser.node.ASTIfStatement, java.lang.Object)
	 */
	public Object visit(ASTIfStatement aNode, Object aData) {

		// Check for first embedded #ELSE or #ELSEIF directive to get end line
		int endLine = aNode.getLastToken().endLine;
		int numChildren = aNode.jjtGetNumChildren();
		for (int i = 1; i < numChildren; i++) {
			Node node = aNode.jjtGetChild(i);
			if (node instanceof ASTElseStatement ||
										  node instanceof ASTElseIfStatement) {
				endLine = node.getFirstToken().beginLine - 1;
				break;
			}
		}
		Directive directive = new Directive(Directive.TYPE_IF,
											aNode.jjtGetChild(0).literal(),
											"", (ITreeNode)fCurrentBlock,
											aNode.getFirstToken().beginLine,
											endLine);
		return visitBlockDirective(aNode, aData, directive, false);
	}

	/**
	 * @see org.apache.velocity.runtime.parser.node.ParserVisitor#visit(org.apache.velocity.runtime.parser.node.ASTElseStatement, java.lang.Object)
	 */
	public Object visit(ASTElseStatement aNode, Object aData) {
		Directive directive = new Directive(Directive.TYPE_ELSE,
											null, "", (ITreeNode)fCurrentBlock,
											aNode.getFirstToken().beginLine,
											aNode.getLastToken().next.endLine);
		return visitBlockDirective(aNode, aData, directive, true);
	}

	/**
	 * @see org.apache.velocity.runtime.parser.node.ParserVisitor#visit(org.apache.velocity.runtime.parser.node.ASTElseIfStatement, java.lang.Object)
	 */
	public Object visit(ASTElseIfStatement aNode, Object aData) {
		Directive directive = new Directive(Directive.TYPE_ELSEIF,
											aNode.jjtGetChild(0).literal(),
											"", (ITreeNode)fCurrentBlock,
											aNode.getFirstToken().beginLine,
											aNode.getLastToken().endLine);
		return visitBlockDirective(aNode, aData, directive, true);
	}

	/**
	 * @see org.apache.velocity.runtime.parser.node.ParserVisitor#visit(org.apache.velocity.runtime.parser.node.ASTSetDirective, java.lang.Object)
	 */
	public Object visit(ASTSetDirective aNode, Object aData) {
		String expr = aNode.jjtGetChild(0).literal();
		int pos = expr.indexOf('=');
		if (pos >= 0) {
			expr = expr.substring(0, pos).trim();
		}
		Directive directive = new Directive(Directive.TYPE_SET, expr, expr,
					 (ITreeNode)fCurrentBlock, aNode.getFirstToken().beginLine,
					 aNode.getLastToken().endLine);
		fCurrentBlock.addDirective(directive);
		return null;
	}

	private Object visitBlockDirective(Node aNode, Object aData,
							Directive aDirective, boolean anAddToParentBlock) {
		if (anAddToParentBlock && fCurrentBlock instanceof Directive) {
			IBlock parent = (IBlock)((Directive)fCurrentBlock).getParent();
			parent.addDirective(aDirective);
		} else {
			fCurrentBlock.addDirective(aDirective);
		}
		fBlocks.push(fCurrentBlock);
		fCurrentBlock = aDirective;
		aData = aNode.childrenAccept(this, aData);
		fCurrentBlock = (IBlock)fBlocks.pop();
		return aData;
	}
}
