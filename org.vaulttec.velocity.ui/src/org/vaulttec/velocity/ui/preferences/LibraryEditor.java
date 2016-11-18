package org.vaulttec.velocity.ui.preferences;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;

/**
 * A field editor to maintain a list of Velocimacro files.
 */
public class LibraryEditor extends ListEditor {
	private DirectoryFieldEditor directory;

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
	public LibraryEditor(String name, String labelText, DirectoryFieldEditor directory, Composite parent) {
		init(name, labelText);
		this.directory = directory;
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
		FileDialog dialog = new FileDialog(getShell());
		dialog.setFilterPath(directory.getStringValue());
		String library = dialog.open();
		if (library != null) {
			library = new File(library).getName();
		}
		return library;
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
