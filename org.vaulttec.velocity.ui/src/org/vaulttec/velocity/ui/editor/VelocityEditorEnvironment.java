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

import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.vaulttec.velocity.core.VelocityCorePlugin;
import org.vaulttec.velocity.core.parser.VelocityParser;
import org.vaulttec.velocity.ui.editor.text.VelocityCodeScanner;
import org.vaulttec.velocity.ui.editor.text.VelocityDoubleClickStrategy;
import org.vaulttec.velocity.ui.editor.text.VelocityStringScanner;

/**
 * This class maintains resources used by multiple instances of the editor.
 * <p>
 * To use this environment an editor has to <code>connect()</code> first. Before
 * disposing an editor a call to <code>disconnect()</code> is necessary.
 */
public class VelocityEditorEnvironment {

	private static RuleBasedScanner codeScanner;
	private static RuleBasedScanner stringScanner;
	private static ITextDoubleClickStrategy doubleClickStrategy;

	private static int refCount = 0;

	/**
	 * A connection has occured - initialize all resources if it is the first
	 * activation.
	 */
	public static void connect() {
		if (++refCount == 1) {
			codeScanner = new VelocityCodeScanner();
			stringScanner = new VelocityStringScanner();
			doubleClickStrategy = new VelocityDoubleClickStrategy();
		}
	}

	/**
	 * A disconnection has occured - clear all resources if it is the last
	 * deactivation.
	 */
	public static void disconnect() {
		if (--refCount == 0) {
			doubleClickStrategy = null;
			stringScanner = null;
			codeScanner = null;
		}
	}

	/**
	 * Returns the singleton code scanner.
	 */
	public static RuleBasedScanner getCodeScanner() {
		return codeScanner;
	}

	/**
	 * Returns the singleton string scanner.
	 */
	public static RuleBasedScanner getStringScanner() {
		return stringScanner;
	}

	/**
	 * Returns the singleton double-click strategy.
	 */
	public static ITextDoubleClickStrategy getDoubleClickStrategy() {
		return doubleClickStrategy;
	}

	/**
	 * Returns the singleton Velocity parser.
	 */
	public static VelocityParser getParser() {
		return VelocityCorePlugin.getParser();
	}

}
