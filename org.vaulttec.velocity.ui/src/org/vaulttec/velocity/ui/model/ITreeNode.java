package org.vaulttec.velocity.ui.model;

public interface ITreeNode {
    public static final Object[] NO_CHILDREN = new Object[0];
	
	String getName();
	
	Object getParent();
	
	boolean hasChildren();
	
	Object[] getChildren();

	String getUniqueID();
	
	int getStartLine();

	int getEndLine();

	/**
	 * Visitor design pattern.
	 * @see ITreeVisitor#visit(ITreeNodeInfo)
	 */
	boolean accept(ITreeVisitor aVisitor);
}
