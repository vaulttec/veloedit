/*******************************************************************************
 * Copyright (c) 2016 Torsten Juergeleit.
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
package org.vaulttec.velocity.core;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.vaulttec.velocity.core.parser.VelocityParser;

public class VelocityCorePlugin extends Plugin implements BundleActivator, IPreferenceChangeListener {

	public static final String PLUGIN_ID = "org.vaulttec.velocity.core";
	private static final String RESOURCE_NAME = PLUGIN_ID + ".messages";

	private static VelocityCorePlugin plugin;
	private static BundleContext bundleContext;
	private IEclipsePreferences preferences;
	private ResourceBundle resourceBundle;
	private VelocityParser parser;

	public VelocityCorePlugin() {
		plugin = this;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		bundleContext = context;
		preferences = new ScopedPreferences(InstanceScope.INSTANCE, PLUGIN_ID);
		preferences.addPreferenceChangeListener(this);
		try {
			resourceBundle = ResourceBundle.getBundle(RESOURCE_NAME);
		} catch (MissingResourceException e) {
			log(e);
		}
		parser = new VelocityParser();
		try {
			parser.initialize();
		} catch (Exception e) {
			log(e);
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		parser = null;
		resourceBundle = null;
		preferences.removePreferenceChangeListener(this);
		preferences = null;
		bundleContext = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static VelocityCorePlugin getDefault() {
		return plugin;
	}

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

	public static String getPluginId() {
		return getDefault().getBundle().getSymbolicName();
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public static ResourceBundle getResourceBundle() {
		return getDefault().resourceBundle;
	}

	public static IEclipsePreferences getPreferences() {
		return getDefault().preferences;
	}

	@Override
	public void preferenceChange(PreferenceChangeEvent event) {
		String key = event.getKey();
		if (IPreferencesConstants.LIBRARY_PATH.equals(key)
				|| IPreferencesConstants.LIBRARY_LIST.equals(key)
				|| IPreferencesConstants.VELOCITY_USER_DIRECTIVES.equals(key)) {
			getParser().initialize();
		}
	}

	public static VelocityParser getParser() {
		return getDefault().parser;
	}

	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}

	public static void log(Throwable throwable) {
		log(new Status(IStatus.ERROR, getPluginId(), Status.OK, getMessage("VelocityCorePlugin.internal_error"),
				throwable));
	}

	public static void logErrorMessage(String message) {
		log(new Status(IStatus.ERROR, getPluginId(), Status.OK, message, null));
	}

	public static void logErrorStatus(String message, IStatus status) {
		if (status == null) {
			logErrorMessage(message);
		} else {
			MultiStatus multi = new MultiStatus(getPluginId(), Status.OK, message, null);
			multi.add(status);
			log(multi);
		}
	}

	public static boolean isDebug() {
		return getDefault().isDebugging();
	}

	public static boolean isDebug(String option) {
		boolean debug;
		if (isDebug()) {
			String value = Platform.getDebugOption(option);
			debug = (value != null && value.equalsIgnoreCase("true") ? true : false);
		} else {
			debug = false;
		}
		return debug;
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getMessage(String key) {
		String bundleString;
		ResourceBundle bundle = getResourceBundle();
		if (bundle != null) {
			try {
				bundleString = bundle.getString(key);
			} catch (MissingResourceException e) {
				log(e);
				bundleString = "!" + key + "!";
			}
		} else {
			bundleString = "!" + key + "!";
		}
		return bundleString;
	}

	public static String getFormattedMessage(String key, String arg) {
		return getFormattedMessage(key, new String[] { arg });
	}

	public static String getFormattedMessage(String key, Object[] args) {
		return MessageFormat.format(getMessage(key), args);
	}

}
