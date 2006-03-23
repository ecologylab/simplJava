package cf.services.messages;

import java.util.ArrayList;

import cf.app.CFPropertyNames;
import cf.app.CFSessionObjects;
import cf.app.CMMenuBar;
import cf.app.CollageMachine;



import ecologylab.generic.ApplicationEnvironment;
import ecologylab.generic.Environment;
import ecologylab.generic.Generic;
import ecologylab.generic.ObjectRegistry;
import ecologylab.generic.ParsedURL;
import ecologylab.generic.ApplicationProperties;
import ecologylab.gui.AWTBridge;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.ElementState;
import ecologylab.xml.NameSpace;
import ecologylab.xml.XmlTranslationException;

/**
 * The message sent by CFServicesClientApplet to combinFormation at startup to configure preferences.
 *
 * @author blake
 * @author andruid
 */
public class SetPreferences 
extends RequestMessage
implements ApplicationProperties, CFSessionObjects, CFPropertyNames
{
	static boolean	firstTime	= true;
	
	public PreferencesSet	preferencesSet = new PreferencesSet();
	
	
	public SetPreferences()
	{
		super();
	}

	public SetPreferences(PreferencesSet preferencesSet)
	{
		super();
		this.preferencesSet		= preferencesSet;
	}
	public SetPreferences(String preferencesSetString, NameSpace nameSpace)
	throws XmlTranslationException
	{
		this((PreferencesSet) translateFromXMLString(preferencesSetString, nameSpace));
	}
	public void addNestedElement(ElementState preferenceState)
	{
		if (preferenceState instanceof Preference)
			preferencesSet.addNestedElement(preferenceState);
	}

	public ResponseMessage performService(ObjectRegistry objectRegistry) 
	{
		System.out.println("cf services: received preferences: " + preferencesSet);
		
    	//now internally set the preferences

		ApplicationEnvironment appEnvironment = 
								(ApplicationEnvironment)Environment.the.get();
		ArrayList prefs = preferencesSet.set;
    	for (int i=0; i<prefs.size(); i++)
    	{
    		Preference pref = (Preference)prefs.get(i);
    		println("processing preference: " + pref);
    		appEnvironment.setProperty(pref.name, pref.value);
    	}
    	println("so now, userinterface=" + USERINTERFACE);
    	
    	String codeBasePref	= (String) appEnvironment.parameter(CODEBASE);

    	ParsedURL codeBase	= ParsedURL.getAbsolute(codeBasePref, "Setting up codebase");
    	if (codeBase != null)
    	{
    		debug("SetPreferences setting codeBase="+codeBase);
        	appEnvironment.setCodeBase(codeBase);
    	}
    	else
    	{
    		debug("SetPreferences ERROR! no codebase preference was passed in.");
    	}
    	//print the prefs
		System.out.println("Received and loaded preferences: " + preferencesSet);
		
		AWTBridge cmBridge	= (AWTBridge) objectRegistry.lookupObject(CM_BRIDGE);
		CMMenuBar cmMenuBar	= (CMMenuBar) objectRegistry.lookupObject(CM_MENU_BAR);

		  cmBridge.unstuckDebugger();
		  
		  CollageMachine collageMachine		= new CollageMachine(cmBridge, cmMenuBar);
		//  collageMachine.start();
		  collageMachine.run();
        println("sending ResponseMessage(OK)");
		return new ResponseMessage(OK);
	}
}
