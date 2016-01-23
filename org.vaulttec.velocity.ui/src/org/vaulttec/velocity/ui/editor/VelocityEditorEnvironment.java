package org.vaulttec.velocity.ui.editor;

import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.vaulttec.velocity.core.VelocityCorePlugin;
import org.vaulttec.velocity.core.parser.VelocityParser;
import org.vaulttec.velocity.ui.VelocityColorProvider;
import org.vaulttec.velocity.ui.editor.text.VelocityCodeScanner;
import org.vaulttec.velocity.ui.editor.text.VelocityDoubleClickStrategy;
import org.vaulttec.velocity.ui.editor.text.VelocityStringScanner;

/**
 * This class maintains resources used by multiple instances of the editor.
 * <p>
 * To use this environment an editor has to <code>connect()</code>
 * first. Before disposing an editor a call to <code>disconnect()</code> is
 * necessary.
 */
public class VelocityEditorEnvironment {

	private static VelocityColorProvider fgColorProvider;
	private static RuleBasedScanner fgCodeScanner;
	private static RuleBasedScanner fgStringScanner;
	private static ITextDoubleClickStrategy fgDoubleClickStrategy;

	private static int fgRefCount = 0;

	/**
	 * A connection has occured - initialize all resources if it is the first
	 * activation.
	 */
	public static void connect() {
		if (++fgRefCount == 1) {
			fgColorProvider = new VelocityColorProvider();
			fgCodeScanner = new VelocityCodeScanner(fgColorProvider);
			fgStringScanner = new VelocityStringScanner(fgColorProvider);
			fgDoubleClickStrategy = new VelocityDoubleClickStrategy();
		}
	}

	/**
	 * A disconnection has occured - clear all resources if it is the last
	 * deactivation.
	 */
	public static void disconnect() {
		if (--fgRefCount == 0) {
			fgDoubleClickStrategy = null;
			fgStringScanner = null;
			fgCodeScanner = null;
			fgColorProvider.dispose();
			fgColorProvider = null;
		}
	}

	/**
	 * Returns the singleton color provider.
	 */
	public static VelocityColorProvider getColorProvider() {
		return fgColorProvider;
	}

	/**
	 * Returns the singleton code scanner.
	 */
	public static RuleBasedScanner getCodeScanner() {
		return fgCodeScanner;
	}

	/**
	 * Returns the singleton string scanner.
	 */
	public static RuleBasedScanner getStringScanner() {
		return fgStringScanner;
	}

	/**
	 * Returns the singleton double-click strategy.
	 */
	public static ITextDoubleClickStrategy getDoubleClickStrategy() {
		return fgDoubleClickStrategy;
	}

	/**
	 * Returns the singleton Velocity parser.
	 */
	public static VelocityParser getParser() {
		return VelocityCorePlugin.getParser();
	}
}
