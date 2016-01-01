package org.vaulttec.velocity.ui.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.vaulttec.velocity.ui.editor.VelocityEditorEnvironment;

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
	private int fType;
	private String fName;
	
	/** List of parameters of Velocity macro */
	private ArrayList fParameters;

	public static final String DIRECTIVES[] = { "#set", "#if", "#else",
									"#elseif", "#end", "#foreach", "#include",
									"#parse", "#macro", "#stop" };
	private String fId;
	protected Vector fDirectives = new Vector();
	
	public Directive(int aType, String aName, String anId,
					  ITreeNode aParent, int aStartLine, int anEndLine) {
		super(aParent, aStartLine, anEndLine);
		fName = aName;
		fType = aType;
		fId = anId;
	}

	public int getType() {
		return fType;
	}

	public static int getType(String aName) {
		for (int i = 0; i < DIRECTIVES.length; i++) {
			if (Directive.DIRECTIVES[i].equals(aName)) {
				return i;
			}
		}
		if (VelocityEditorEnvironment.getParser().isUserDirective(aName)) {
			return TYPE_USER_DIRECTIVE;
		}
		return TYPE_MACRO_CALL;
	}

	public String getId() {
		return fId;
	}

	public void addParameter(String aParameter) {
		if (fParameters == null) {
			fParameters = new ArrayList();
		}
		fParameters.add(aParameter);
	}

	public ArrayList getParameters() {
		return fParameters;
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
		return (fType < TYPE_MACRO_CALL ? DIRECTIVES[fType] +
				 (fType != TYPE_ELSE ? " (" + fName + ")" : "") : "#" + fName);
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
		
		// Visit all embedded directives of this directive
		Iterator iter = fDirectives.iterator();
		while (more && iter.hasNext()) {
			more = ((ITreeNode)iter.next()).accept(aVisitor);
		}

		// Finally visit this directive
		if (more) {
			more = aVisitor.visit(this);
		}
		return more;
	}

	/**
	 * @see org.vaulttec.velocity.ui.model.ITreeNodeInfo#getUniqueID()
	 */
	public String getUniqueID() {
		return getName() + ":" + getStartLine();
	}

	public String toString() {
	    return getUniqueID() + ":" + getEndLine() + " with directive(s) " +
	    		fDirectives;
	}
}
