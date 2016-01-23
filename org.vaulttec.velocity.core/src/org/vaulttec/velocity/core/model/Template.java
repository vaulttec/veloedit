package org.vaulttec.velocity.core.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Template extends AbstractTreeNode implements IBlock {

	private String name;
	private List<Directive> directives = new ArrayList<>();

	public Template(String name) {
		super(null, -1, -1);
		this.name = name;
	}

	@Override
	public void addDirective(Directive directive) {
		directives.add(directive);
	}

	@Override
	public String getName() {
		return name;
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
	public boolean accept(ITreeVisitor aVisitor) {
		boolean more = true;

		// Visit all directives of this template
		Iterator<Directive> iter = directives.iterator();
		while (more && iter.hasNext()) {
			more = ((ITreeNode) iter.next()).accept(aVisitor);
		}

		// Finally visit this template
		if (more) {
			more = aVisitor.visit(this);
		}
		return more;
	}

	@Override
	public String getUniqueID() {
		return getName();
	}

	@Override
	public String toString() {
		return getUniqueID() + " [" + getStartLine() + ":" + getEndLine() + "] with directive(s) " + directives;
	}
}
