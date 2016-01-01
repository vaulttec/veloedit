package org.vaulttec.velocity.ui;

import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.vaulttec.velocity.ui.editor.VelocityEditorEnvironment;

/**
 * The main plugin class to be used in the desktop.
 */
public class VelocityPlugin extends AbstractUIPlugin
										 implements IPropertyChangeListener {
	/** The id of the Velocity plugin
	 * (value <code>"org.vaulttec.velocity.ui"</code>). */	
	public static final String PLUGIN_ID = "org.vaulttec.velocity.ui";

	/** The shared instance. */
	private static VelocityPlugin fPlugin;

	private static final String RESOURCE_NAME = PLUGIN_ID + ".messages";
	private ResourceBundle fResourceBundle;
	
	public VelocityPlugin() {
		fPlugin = this;
		try {
			fResourceBundle = ResourceBundle.getBundle(RESOURCE_NAME);
		} catch (MissingResourceException e) {
			log(e);
			fResourceBundle = null;
		}
	}

	public void startup() throws CoreException {
		getDefault().getPreferenceStore().addPropertyChangeListener(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#shutdown()
	 */
	public void shutdown() throws CoreException {
		getDefault().getPreferenceStore().removePropertyChangeListener(this);
	}

	/**
	 * Sets default preference values. These values will be used until some
	 * preferences are actually set using the preference dialog.
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#initializeDefaultPreferences(org.eclipse.jface.preference.IPreferenceStore)
	 */
	protected void initializeDefaultPreferences(IPreferenceStore aStore) {
		super.initializeDefaultPreferences(aStore);
		aStore.setDefault(IPreferencesConstants.EDITOR_SHOW_SEGMENTS, false);
		aStore.setDefault(IPreferencesConstants.VELOCITY_COUNTER_NAME, 
						  "velocityCount");
		aStore.setDefault(IPreferencesConstants.VELOCITY_USER_DIRECTIVES, 
						  "");
		VelocityColorProvider.initializeDefaults(aStore);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent anEvent) {
		String prop = anEvent.getProperty();
		if (prop.equals(IPreferencesConstants.VELOCITY_USER_DIRECTIVES) ||
							 prop.equals(IPreferencesConstants.LIBRARY_LIST)) {
			VelocityEditorEnvironment.createVelocityParser();
		}
	}

	/**
	 * Returns Velocity user directives.
	 */
	public static List getVelocityUserDirectives() {
		IPreferenceStore store = getDefault().getPreferenceStore();
		String directives = store.getString(
							   IPreferencesConstants.VELOCITY_USER_DIRECTIVES);
		StringTokenizer st = new StringTokenizer(directives, ",\n\r");
		ArrayList list = new ArrayList();
		while (st.hasMoreElements()) {
			list.add(st.nextElement());
		}
		return list;
	}

	/**
	 * Returns the shared instance.
	 */
	public static VelocityPlugin getDefault() {
		return fPlugin;
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
		return fResourceBundle;
	}

	public static Shell getActiveWorkbenchShell() {
		IWorkbenchWindow window = getActiveWorkbenchWindow();
		return (window != null ? window.getShell() : null);
	}

	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return getDefault().getWorkbench().getActiveWorkbenchWindow();
	}
	
	public static String getPluginId() {
		return getDefault().getDescriptor().getUniqueIdentifier();
	}

	public static IPath getInstallLocation() {
		return new Path(getInstallURL().getFile());
	}

	public static URL getInstallURL() {
		return getDefault().getDescriptor().getInstallURL();
	}

	public static void log(IStatus aStatus) {
		getDefault().getLog().log(aStatus);
	}
	
	public static void log(Throwable aThrowable) {
		log(new Status(IStatus.ERROR, getPluginId(), Status.OK,
						getMessage("VelocityPlugin.internal_error"),
						aThrowable));
	}
	
	public static void logErrorMessage(String aMessage) {
		log(new Status(IStatus.ERROR, getPluginId(), Status.OK, aMessage,
			null));
	}

	public static void logErrorStatus(String aMessage, IStatus aStatus) {
		if (aStatus == null) {
			logErrorMessage(aMessage);
		} else {
			MultiStatus multi = new MultiStatus(getPluginId(), Status.OK,
											    aMessage, null);
			multi.add(aStatus);
			log(multi);
		}
	}
	
	public static boolean isDebug() {
		return getDefault().isDebugging();
	}

	public static boolean isDebug(String anOption) {
		boolean debug;
		if (isDebug()) {
			String value = Platform.getDebugOption(anOption);
			debug = (value != null && value.equalsIgnoreCase("true") ?
					 true : false);
		} else {
			debug = false;
		}
		return debug;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'aKey' if not found.
	 */
	public static String getMessage(String aKey) {
	    String bundleString;
		ResourceBundle bundle = getDefault().getResourceBundle();
		if (bundle != null) {
			try {
				bundleString = bundle.getString(aKey);
			} catch (MissingResourceException e) {
			    log(e);
				bundleString = "!" + aKey + "!";
			}
		} else {
			bundleString = "!" + aKey + "!";
		}
		return bundleString;
	}

	public static String getFormattedMessage(String aKey, String anArg) {
		return getFormattedMessage(aKey, new String[] { anArg });
	}

	public static String getFormattedMessage(String aKey, Object[] anArgs) {
		return MessageFormat.format(getMessage(aKey), anArgs);
	}
}
