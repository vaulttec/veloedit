package org.vaulttec.velocity.ui.model;

/**
 * Visitor design pattern.
 * @see ITreeNode#accept(ITreeVisitor)
 */
public interface ITreeVisitor {
	
	boolean visit(ITreeNode anElement);
}
