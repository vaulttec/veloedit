package org.vaulttec.velocity.ui;

import java.net.URL;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class VelocityUIPlugin extends AbstractUIPlugin {

	/**
	 * The id of the Velocity plugin (value
	 * <code>"org.vaulttec.velocity.ui"</code>).
	 */
	public static final String PLUGIN_ID = "org.vaulttec.velocity.ui";

	/** The shared instance. */
	private static VelocityUIPlugin plugin;

	/**
	 * The combined preference store.
	 */
	private IPreferenceStore combinedPreferenceStore;

	private static final String RESOURCE_NAME = PLUGIN_ID + ".messages";
	private ResourceBundle resourceBundle;

	public VelocityUIPlugin() {
		plugin = this;
		try {
			resourceBundle = ResourceBundle.getBundle(RESOURCE_NAME);
		} catch (MissingResourceException e) {
			log(e);
			this.resourceBundle = null;
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		try {
			VelocityColorManager.getDefault().dispose();
		} finally {
			super.stop(context);
		}
	}

	/**
	 * Returns the shared instance.
	 */
	public static VelocityUIPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns a combined preference store, this store is read-only.
	 */
	public IPreferenceStore getCombinedPreferenceStore() {
		if (combinedPreferenceStore == null) {
			combinedPreferenceStore = new ChainedPreferenceStore(
					new IPreferenceStore[] { getPreferenceStore(), EditorsUI.getPreferenceStore() });
		}
		return combinedPreferenceStore;
	}

	/**
	 * Returns the preference color, identified by the given name.
	 */
	public static Color getPreferenceColor(String name) {
		return VelocityColorManager.getDefault()
				.getColor(PreferenceConverter.getColor(getDefault().getCombinedPreferenceStore(), name));
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public static Shell getActiveWorkbenchShell() {
		IWorkbenchWindow window = getActiveWorkbenchWindow();
		return (window != null ? window.getShell() : null);
	}

	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return getDefault().getWorkbench().getActiveWorkbenchWindow();
	}

	public static String getPluginId() {
		return getDefault().getBundle().getSymbolicName();
	}

	public static IPath getInstallLocation() {
		return new Path(getInstallURL().getFile());
	}

	public static URL getInstallURL() {
		return getDefault().getBundle().getEntry("/");
	}

	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}

	public static void log(Throwable throwable) {
		log(new Status(IStatus.ERROR, getPluginId(), Status.OK, getMessage("VelocityPlugin.internal_error"),
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
	 * Returns the string from the plugin's resource bundle, or 'aKey' if not
	 * found.
	 */
	public static String getMessage(String key) {
		String bundleString;
		ResourceBundle bundle = getDefault().getResourceBundle();
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
