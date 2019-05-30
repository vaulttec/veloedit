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
package org.vaulttec.velocity.core.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.velocity.Template;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.directive.Macro;
import org.apache.velocity.runtime.parser.node.Node;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.vaulttec.velocity.core.IPreferencesConstants;
import org.vaulttec.velocity.core.VelocityCorePlugin;

public class VelocityParser extends RuntimeInstance {

	private List<String> userDirectives;
	private Map<String, VelocityMacro> macros = new HashMap<>();

	public boolean isUserDirective(String name) {
		return userDirectives.contains(name);
	}

	public List<String> getUserDirectives() {
		return userDirectives;
	}

	public VelocityMacro getLibraryMacro(String name) {
		return macros.get(name);
	}

	public Collection<VelocityMacro> getLibraryMacros() {
		return macros.values();
	}

	public synchronized void initialize() {

		// Set Velocity library
		IEclipsePreferences preferences = VelocityCorePlugin.getPreferences();
		setProperty(FILE_RESOURCE_LOADER_PATH, preferences.get(IPreferencesConstants.LIBRARY_PATH, ""));
		setProperty(VM_LIBRARY, preferences.get(IPreferencesConstants.LIBRARY_LIST, ""));

		setProperty(PARSER_POOL_SIZE, 0);
		
		// Initialize user directives
		initializeUserDirectives();

		// Call super implementation - Massage TCCL to deal with bug in m2e
		// (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=396554)
		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(RuntimeInstance.class.getClassLoader());
		try {
			super.init();
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	/**
	 * This methods initializes all user directives.
	 */
	private void initializeUserDirectives() {
		userDirectives = new ArrayList<>();
		IEclipsePreferences preferences = VelocityCorePlugin.getPreferences();
		String directives = preferences.get(IPreferencesConstants.VELOCITY_USER_DIRECTIVES, "");
		StringTokenizer st = new StringTokenizer(directives, ",\n\r");
		while (st.hasMoreElements()) {
			String directive = (String) st.nextElement();
			String name = directive.substring(0, directive.indexOf(' '));
			int type = (directive.endsWith("[Block]") ? Directive.BLOCK : Directive.LINE);
			userDirectives.add('#' + name);

			addDirective(new VelocityDirective(name, type));
		}
	}

	/**
	 * Returns a new Velocity template with given name.
	 */
	public Template createTemplate(String name) {
		Template template = new Template();
		template.setName(name);
		return template;
	}

	/**
	 * Hooks into internal parser initialization to build a list of macros known
	 * to the Velocity engine.
	 */
	@Override
	public boolean addVelocimacro(String name, Node macro, List<Macro.MacroArg> macroArgs, Template definingTemplate) {
		macros.put(name, new VelocityMacro(name, macroArgs, definingTemplate));
		return super.addVelocimacro(name, macro, macroArgs, definingTemplate);
	}

}
