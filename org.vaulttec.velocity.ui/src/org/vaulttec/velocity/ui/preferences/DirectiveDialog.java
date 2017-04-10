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
package org.vaulttec.velocity.ui.preferences;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.vaulttec.velocity.ui.VelocityUIPlugin;

/**
 * Dialog used to define a Velocity user directive (name and type [block or
 * line]).
 */
public class DirectiveDialog extends InputDialog {
	protected static String PREFIX = "VelocityPreferences.directive.dialog.";
	protected static IInputValidator VALIDATOR = new DirectiveValidator();

	private Button lineButton;
	private Button blockButton;
	private boolean isBlock;

	public DirectiveDialog(Shell shell) {
		super(shell, VelocityUIPlugin.getMessage(PREFIX + "title"), VelocityUIPlugin.getMessage(PREFIX + "message"),
				null, VALIDATOR);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 8; // Gap between label and control
		layout.numColumns = 2;

		Group group = new Group(composite, SWT.LEFT);
		group.setFont(parent.getFont());
		group.setText(VelocityUIPlugin.getMessage(PREFIX + "typeGroup"));
		group.setLayout(layout);

		lineButton = createRadioButton(group, VelocityUIPlugin.getMessage(PREFIX + "typeLine"));
		lineButton.setSelection(!isBlock);
		lineButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isBlock = false;
			}
		});

		blockButton = createRadioButton(group, VelocityUIPlugin.getMessage(PREFIX + "typeBlock"));
		blockButton.setSelection(isBlock);
		blockButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isBlock = true;
			}
		});
		return composite;
	}

	/**
	 * Utility method that creates a radio button instance and sets the default
	 * layout data.
	 *
	 * @param parent
	 *            the parent for the new radio button
	 * @param label
	 *            the label for the new radio button
	 * @return the newly-created radio button
	 */
	protected static Button createRadioButton(Composite parent, String label) {
		Button button = new Button(parent, SWT.RADIO | SWT.LEFT);
		button.setText(label);
		button.setFont(parent.getFont());
		return button;
	}

	/**
	 * Returns the name of the directive typed into this input dialog and the
	 * selected type (format '&lt;name&gt;[&lt;B|L&gt;]'.
	 *
	 * @return the input string and the selected type
	 */
	@Override
	public String getValue() {
		return super.getValue() + " [" + (isBlock ? "Block" : "Line") + ']';
	}

	private static class DirectiveValidator implements IInputValidator {

		@Override
		public String isValid(String text) {
			if (text.length() == 0) {
				return "";
			}
			for (int i = text.length() - 1; i >= 0; i--) {
				if (!Character.isLetterOrDigit(text.charAt(i))) {
					return VelocityUIPlugin.getMessage(PREFIX + "error");
				}
			}
			return null;
		}
	}

}
