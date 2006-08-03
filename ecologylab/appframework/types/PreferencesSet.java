package cf.services.messages;

import java.util.ArrayList;
import java.util.Collection;

import cf.app.CFPropertyNames;
import cf.app.CFSessionObjects;

import ecologylab.generic.ApplicationEnvironment;
import ecologylab.generic.ApplicationProperties;
import ecologylab.generic.Environment;
import ecologylab.net.ParsedURL;
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
	 * When translating from XML, if a tag is encountered with no matching field, perhaps
	 * it belongs in a Collection.
	 * This method tells us which collection object that would be.
	 * 
	 * @param thatClass		The class of the ElementState superclass that could be stored in a Collection.
	 * @param tag 			The tag that represents the field in the XML where the ElementState object is stored.
	 * @return
	 */
	protected Collection getCollection(Class thatClass, String tag)
	{
		return Preference.class.equals(thatClass) ? set : null;
	}
	
	public void processPreferences()
	{
		ApplicationEnvironment appEnvironment = 
			(ApplicationEnvironment)Environment.the.get();
		ArrayList prefs = set;
		for (int i=0; i<size(); i++)
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
