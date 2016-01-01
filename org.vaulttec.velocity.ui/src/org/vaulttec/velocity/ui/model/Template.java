package org.vaulttec.velocity.ui.model;

import java.util.Iterator;
import java.util.Vector;

public class Template extends AbstractTreeNode implements IBlock {

	private String fName;
	private Vector fDirectives = new Vector();
	
	public Template(String aName) {
		super(null, -1, -1);
		fName = aName;
	}

	/**
	 * @see org.vaulttec.velocity.ui.model.IBlock#addDirective(org.vaulttec.velocity.ui.model.Directive)
	 */
	public void addDirective(Directive aDirective) {
		fDirectives.add(aDirective);
	}

	/**
	 * @see org.vaulttec.velocity.ui.model.ITreeNode#getName()
	 */
	public String getName() {
		return fName;
	}

	/**
	 * @see org.vaulttec.velocity.ui.model.ITreeNode#hasChildren()
	 */
	public boolean hasChildren() {
		return !fDirectives.isEmpty();
	}

	/**
	 * @see org.vaulttec.velocity.ui.model.ITreeNode#getChildren()
	 */
	public Object[] getChildren() {
	    return fDirectives.toArray();
	}

	/**
	 * @see org.vaulttec.velocity.ui.model.ITreeNode#accept(org.vaulttec.velocity.ui.model.ITreeVisitor)
	 */
	public boolean accept(ITreeVisitor aVisitor) {
		boolean more = true;
		
		// Visit all directives of this template
		Iterator iter = fDirectives.iterator();
		while (more && iter.hasNext()) {
			more = ((ITreeNode)iter.next()).accept(aVisitor);
		}

		// Finally visit this template
		if (more) {
			more = aVisitor.visit(this);
		}
		return more;
	}

	/**
	 * @see org.vaulttec.velocity.ui.model.ITreeNodeInfo#getUniqueID()
	 */
	public String getUniqueID() {
		return getName();
	}

	public String toString() {
	    return getUniqueID() + " [" + getStartLine() + ":" + getEndLine() +
	    		"] with directive(s) " + fDirectives;
	}
}
