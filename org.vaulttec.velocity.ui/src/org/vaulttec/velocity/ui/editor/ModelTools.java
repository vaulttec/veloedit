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
package org.vaulttec.velocity.ui.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.vaulttec.velocity.core.IPreferencesConstants;
import org.vaulttec.velocity.core.VelocityCorePlugin;
import org.vaulttec.velocity.core.model.Directive;
import org.vaulttec.velocity.core.model.ITreeNode;
import org.vaulttec.velocity.core.model.ITreeVisitor;
import org.vaulttec.velocity.ui.editor.text.VelocityTextGuesser;

public class ModelTools {
	private VelocityEditor editor;

	public ModelTools(VelocityEditor editor) {
		this.editor = editor;
	}

	/**
	 * Uses visitor design pattern to find tree node which contains given line.
	 * 
	 * @param line
	 *            line to find according tree node for
	 * @return tree node containing given line or null if no tree node found
	 */
	public ITreeNode getNodeByLine(int line) {
		ITreeNode node = editor.getRootNode();
		if (node != null) {
			TreeNodeLineVisitor visitor = new TreeNodeLineVisitor(line);
			node.accept(visitor);
			return visitor.getNode();
		}
		return null;
	}

	/**
	 * Uses visitor design pattern to find tree node which contains given
	 * guessed text.
	 * 
	 * @param guess
	 *            document region marking a reference to find according tree
	 *            node with referenced id for
	 * @return tree node containing referenced id or null if no tree node found
	 */
	public ITreeNode getNodeByGuess(VelocityTextGuesser guess) {
		ITreeNode node = editor.getRootNode();
		if (node != null) {
			String id;
			if (guess.getType() == VelocityTextGuesser.TYPE_VARIABLE) {
				id = "$" + guess.getText();
			} else if (guess.getType() == VelocityTextGuesser.TYPE_DIRECTIVE) {
				id = guess.getText();
			} else {
				return null;
			}

			// Search the model tree for a node with giben ID
			TreeNodeIdVisitor visitor = new TreeNodeIdVisitor(id, guess.getLine());
			node.accept(visitor);
			return visitor.getNode();
		}
		return null;
	}

	/**
	 * Returns true if specified line belongs to a <code>#foreach</code> block.
	 */
	public boolean isLineWithinLoop(int line) {
		ITreeNode node = editor.getRootNode();
		if (node != null) {
			if (line > 0) {
				// Use visitor pattern to find node which contains given line
				TreeNodeLineVisitor visitor = new TreeNodeLineVisitor(line);
				node.accept(visitor);
				node = visitor.getNode();
				while (node != null) {
					if (node instanceof Directive) {
						Directive directive = (Directive) node;
						if (directive.getType() == Directive.TYPE_FOREACH) {
							return true;
						}
					}
					node = (ITreeNode) node.getParent();
				}
			}
		}
		return false;
	}

	public List<String> getVariables(int line) {
		ITreeNode node = editor.getLastRootNode();
		if (node != null) {
			TreeNodeVariableVisitor visitor = new TreeNodeVariableVisitor(line);
			node.accept(visitor);
			List<String> variables = visitor.getVariables();
			if (isLineWithinLoop(editor.getCursorLine())) {
				IEclipsePreferences prefs = VelocityCorePlugin.getPreferences();
				String countName = "$" + prefs.get(IPreferencesConstants.VELOCITY_COUNTER_NAME, "");
				variables.add(countName);
			}
			return variables;
		}
		return new ArrayList<>();
	}

	public List<String> getMacros() {
		ITreeNode node = editor.getLastRootNode();
		if (node != null) {
			TreeNodeMacroVisitor visitor = new TreeNodeMacroVisitor();
			node.accept(visitor);
			return visitor.getMacros();
		}
		return new ArrayList<>();
	}

	private class TreeNodeLineVisitor implements ITreeVisitor {
		private int line;
		private ITreeNode node;

		public TreeNodeLineVisitor(int line) {
			this.line = line;
			this.node = null;
		}

		public boolean visit(ITreeNode node) {
			boolean more;
			if (line >= node.getStartLine() && line <= node.getEndLine()) {
				this.node = node;
				more = false;
			} else {
				more = true;
			}
			return more;
		}

		public ITreeNode getNode() {
			return node;
		}
	}

	private class TreeNodeIdVisitor implements ITreeVisitor {
		private final String id;
		private final int line;
		private ITreeNode node;

		public TreeNodeIdVisitor(String anId, int aLine) {
			this.id = anId;
			this.line = aLine;
			this.node = null;
		}

		public boolean visit(ITreeNode node) {
			if (node.getStartLine() < line && node instanceof Directive) {
				Directive directive = ((Directive) node);

				// If within macro block then check macro parameters instead
				// of ID
				if (directive.getType() == Directive.TYPE_MACRO) {
					if (directive.getId().equals(id)) {
						this.node = node;
					} else if (line >= node.getStartLine() && line <= node.getEndLine()) {
						List<String> parameters = ((Directive) node).getParameters();
						if (parameters != null && parameters.contains(id)) {
							this.node = node;
						}
					}
				} else {
					String id = directive.getId();
					if (id != null && id.equals(this.id)) {
						this.node = node;
					}
				}
			}
			return true;
		}

		public ITreeNode getNode() {
			return node;
		}
	}

	private class TreeNodeVariableVisitor implements ITreeVisitor {
		private int line;
		private List<String> variables;

		public TreeNodeVariableVisitor(int aLine) {
			this.line = aLine;
			this.variables = new ArrayList<>();
		}

		public boolean visit(ITreeNode aNode) {
			if (aNode instanceof Directive) {
				int type = ((Directive) aNode).getType();
				if (type == Directive.TYPE_FOREACH || type == Directive.TYPE_SET) {
					String variable = ((Directive) aNode).getId();
					if (!variables.contains(variable)) {
						variables.add(variable);
					}
				} else if (type == Directive.TYPE_MACRO) {
					if (line >= aNode.getStartLine() && line <= aNode.getEndLine()) {
						List<String> parameters = ((Directive) aNode).getParameters();
						if (parameters != null) {
							variables.addAll(parameters);
						}
					}
				}
			}
			return true;
		}

		public List<String> getVariables() {
			return variables;
		}
	}

	private class TreeNodeMacroVisitor implements ITreeVisitor {
		private List<String> macros;

		public TreeNodeMacroVisitor() {
			this.macros = new ArrayList<>();
		}

		public boolean visit(ITreeNode aNode) {
			if (aNode instanceof Directive) {
				int type = ((Directive) aNode).getType();
				if (type == Directive.TYPE_MACRO) {
					macros.add(((Directive) aNode).getId());
				}
			}
			return true;
		}

		public List<String> getMacros() {
			return macros;
		}
	}
}
