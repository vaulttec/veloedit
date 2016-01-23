package org.vaulttec.velocity.core;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferenceNodeVisitor;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class ScopedPreferences implements IEclipsePreferences {

	private final IEclipsePreferences preferences;
	private final IEclipsePreferences defaultPreferences;
	private final IEclipsePreferences[] preferenceNodes;

	public ScopedPreferences(IScopeContext context, String qualifier) {
		this.preferences = context.getNode(VelocityCorePlugin.PLUGIN_ID);
		this.defaultPreferences = DefaultScope.INSTANCE.getNode(VelocityCorePlugin.PLUGIN_ID);
		this.preferenceNodes = new IEclipsePreferences[] { preferences, defaultPreferences };
	}

	@Override
	public void put(String key, String value) {
		preferences.put(key, value);
	}

	@Override
	public String get(String key, String def) {
		String value = internalGet(key);
		if (value == null) {
			value = defaultPreferences.get(key, def);
		}
		return value;
	}

	@Override
	public void remove(String key) {
		preferences.remove(key);
	}

	@Override
	public void clear() throws BackingStoreException {
		preferences.clear();
	}

	@Override
	public void putInt(String key, int value) {
		preferences.putInt(key, value);
	}

	@Override
	public int getInt(String key, int def) {
		String value = internalGet(key);
		if (value == null) {
			return defaultPreferences.getInt(key, def);
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	@Override
	public void putLong(String key, long value) {
		preferences.putLong(key, value);
	}

	@Override
	public long getLong(String key, long def) {
		String value = internalGet(key);
		if (value == null) {
			return defaultPreferences.getLong(key, def);
		}
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			return 0L;
		}
	}

	@Override
	public void putBoolean(String key, boolean value) {
		preferences.putBoolean(key, value);
	}

	@Override
	public boolean getBoolean(String key, boolean def) {
		String value = internalGet(key);
		if (value == null) {
			return defaultPreferences.getBoolean(key, def);
		}
		try {
			return Boolean.parseBoolean(value);
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public void putFloat(String key, float value) {
		preferences.putFloat(key, value);
	}

	@Override
	public float getFloat(String key, float def) {
		String value = internalGet(key);
		if (value == null) {
			return defaultPreferences.getFloat(key, def);
		}
		try {
			return Float.parseFloat(value);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	@Override
	public void putDouble(String key, double value) {
		preferences.putDouble(key, value);
	}

	@Override
	public double getDouble(String key, double def) {
		String value = internalGet(key);
		if (value == null) {
			return defaultPreferences.getDouble(key, def);
		}
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	@Override
	public void putByteArray(String key, byte[] value) {
		preferences.putByteArray(key, value);
	}

	@Override
	public byte[] getByteArray(String key, byte[] def) {
		String value = internalGet(key);
		if (value == null) {
			return defaultPreferences.getByteArray(key, def);
		}
		return value.getBytes();
	}

	@Override
	public String[] keys() throws BackingStoreException {
		return preferences.keys();
	}

	@Override
	public String[] childrenNames() throws BackingStoreException {
		return preferences.childrenNames();
	}

	@Override
	public Preferences parent() {
		return preferences.parent();
	}

	@Override
	public boolean nodeExists(String pathName) throws BackingStoreException {
		return preferences.nodeExists(pathName);
	}

	@Override
	public String name() {
		return preferences.name();
	}

	@Override
	public String absolutePath() {
		return preferences.absolutePath();
	}

	@Override
	public void flush() throws BackingStoreException {
		preferences.flush();
	}

	@Override
	public void sync() throws BackingStoreException {
		preferences.sync();
	}

	@Override
	public void addNodeChangeListener(INodeChangeListener listener) {
		preferences.addNodeChangeListener(listener);
	}

	@Override
	public void removeNodeChangeListener(INodeChangeListener listener) {
		preferences.removeNodeChangeListener(listener);
	}

	@Override
	public void addPreferenceChangeListener(IPreferenceChangeListener listener) {
		preferences.addPreferenceChangeListener(listener);
	}

	@Override
	public void removePreferenceChangeListener(IPreferenceChangeListener listener) {
		preferences.removePreferenceChangeListener(listener);
	}

	@Override
	public void removeNode() throws BackingStoreException {
		preferences.removeNode();
	}

	@Override
	public Preferences node(String path) {
		return preferences.node(path);
	}

	@Override
	public void accept(IPreferenceNodeVisitor visitor) throws BackingStoreException {
		preferences.accept(visitor);
	}

	private String internalGet(String key) {
		return Platform.getPreferencesService().get(key, null, preferenceNodes);
	}

}
