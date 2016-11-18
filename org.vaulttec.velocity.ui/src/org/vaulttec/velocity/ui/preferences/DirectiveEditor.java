package org.vaulttec.velocity.ui.preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;

/**
 * A field editor to maintain a list of Velocity user directives.
 */
public class DirectiveEditor extends ListEditor {

	/**
	 * Creates a new field editor.
	 * 
	 * @param name
	 *            the name of the preference this field editor works on
	 * @param labelText
	 *            the label text of the field editor
	 * @param parent
	 *            the parent of the field editor's control
	 */
	public DirectiveEditor(String name, String labelText, Composite parent) {
		init(name, labelText);
		createControl(parent);
	}

	@Override
	protected String createList(String[] directives) {
		StringBuffer directivesList = new StringBuffer();
		for (int i = 0; i < directives.length; i++) {
			directivesList.append(directives[i]);
			directivesList.append(',');
		}
		return directivesList.toString();
	}

	@Override
	protected String getNewInputObject() {
		DirectiveDialog dialog = new DirectiveDialog(getShell());
		if (dialog.open() == Window.OK) {
			return dialog.getValue();
		}
		return null;
	}

	@Override
	protected String[] parseString(String directivesList) {
		StringTokenizer st = new StringTokenizer(directivesList, ",\n\r");
		List<String> directives = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			directives.add(st.nextToken());
		}
		return directives.toArray(new String[directives.size()]);
	}

}
