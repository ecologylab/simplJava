package cf.services.messages;

import java.util.ArrayList;

import cf.app.CFPropertyNames;
import cf.app.CFSessionObjects;

import ecologylab.generic.ApplicationEnvironment;
import ecologylab.generic.ApplicationProperties;
import ecologylab.generic.Environment;
import ecologylab.generic.ParsedURL;
import ecologylab.xml.ArrayListState;
import ecologylab.xml.ElementState;

/**
 * A top level message between javascript and CFSessionLauncher.
 */
public class PreferencesSet extends ArrayListState
implements ApplicationProperties, CFSessionObjects, CFPropertyNames
{
	
	public PreferencesSet()
	{
		super();
	}
	/**
	 * overriding the addElement of ElementState class to add non-primitive objects
	 */
	public void addNestedElement(ElementState elementState)
	{
		if (elementState instanceof Preference)
			set.add(elementState);
	}
	
	public void processPreferences()
	{
		ApplicationEnvironment appEnvironment = 
			(ApplicationEnvironment)Environment.the.get();
		ArrayList prefs = set;
		for (int i=0; i<prefs.size(); i++)
		{
			Preference pref = (Preference) prefs.get(i);
			println("processing preference: " + pref);
			appEnvironment.setProperty(pref.name, pref.value);
		}
		debug("so now, userinterface=" + USERINTERFACE);
		
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
	}
}
