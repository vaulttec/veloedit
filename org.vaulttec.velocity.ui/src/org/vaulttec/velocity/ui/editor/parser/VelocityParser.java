package org.vaulttec.velocity.ui.editor.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.log.NullLogChute;
import org.apache.velocity.runtime.parser.Parser;
import org.eclipse.jface.preference.IPreferenceStore;
import org.vaulttec.velocity.ui.IPreferencesConstants;
import org.vaulttec.velocity.ui.VelocityPlugin;

public class VelocityParser extends RuntimeInstance {
    
    /** 
     * Indicate whether the Parser has been fully initialized.
     */
    private boolean fIsInitialized = false;

	private List fUserDirectives;

    private Hashtable fMacros = new Hashtable();

	public Collection getUserDirectives() {
		return fUserDirectives;
	}

	public VelocityMacro getLibraryMacro(String aName) {
		return (fMacros.containsKey(aName) ?
									 (VelocityMacro)fMacros.get(aName) : null);
	}

	public Collection getLibraryMacros() {
		return fMacros.values();
	}

	public boolean isUserDirective(String aName) {
		return fUserDirectives.contains(aName);
	}

    public synchronized void init() {
    	if (!fIsInitialized) {

			// Set Velocity library
			IPreferenceStore store = VelocityPlugin.getDefault().
														  getPreferenceStore();
			setProperty(FILE_RESOURCE_LOADER_PATH,
						store.getString(IPreferencesConstants.LIBRARY_PATH));
			setProperty(VM_LIBRARY,
						store.getString(IPreferencesConstants.LIBRARY_LIST));

			// Disable Velocity logging
			setProperty(RUNTIME_LOG_LOGSYSTEM_CLASS,
						NullLogChute.class.getCanonicalName());

			// Call super implementation
			super.init();

			// Initialize user directives
    		initializeUserDirectives();

    		fIsInitialized = true;
	   	}
    }

    /**
     * This methods initializes all user directives.
     */
    private void initializeUserDirectives() {
        fUserDirectives = new ArrayList();
        Iterator userDirectives =
        				 VelocityPlugin.getVelocityUserDirectives().iterator();
        while (userDirectives.hasNext()) {
        	String directive = (String)userDirectives.next();
        	String name = directive.substring(0, directive.indexOf(' '));
        	int type = (directive.endsWith("[Block]") ? Directive.BLOCK :
        												Directive.LINE);
        	fUserDirectives.add('#' + name);

        	addDirective(new VelocityDirective(name, type));
        }
    }

   /**
     * Adds a new Velocimacro. Usually called by Macro only while parsing.
     *
     * @param String name  Name of velocimacro 
     * @param String macro  String form of macro body
     * @param String argArray  Array of strings, containing the 
     *                         #macro() arguments.  the 0th is the name.
     * @return boolean  True if added, false if rejected for some 
     *                  reason (either parameters or permission settings) 
     */
    public boolean addVelocimacro(String name, String macro,
    								String argArray[], String sourceTemplate) {
    	fMacros.put(name, new VelocityMacro(name, argArray, sourceTemplate));    
        return super.addVelocimacro(name, macro, argArray, sourceTemplate);
    }
}
