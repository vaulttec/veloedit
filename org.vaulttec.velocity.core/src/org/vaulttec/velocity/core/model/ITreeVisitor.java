package org.vaulttec.velocity.core.model;

/**
 * Visitor design pattern.
 * 
 * @see ITreeNode#accept(ITreeVisitor)
 */
public interface ITreeVisitor {

	boolean visit(ITreeNode node);
}
