package ecologylab.services.messages;

import java.util.Collection;

import ecologylab.generic.ApplicationEnvironment;
import ecologylab.generic.ApplicationProperties;
import ecologylab.generic.Environment;
import ecologylab.generic.ObjectRegistry;
import ecologylab.net.ParsedURL;
import ecologylab.xml.ArrayListState;
import ecologylab.xml.ElementState;

/**
 * A top level message between javascript and CFSessionLauncher.
 */
public class PreferencesSet extends ArrayListState
implements ApplicationProperties
{
	ElementState child;
	
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
		return Preference.class.equals(thatClass) ? set() : null;
	}
	
	public void processPreferences()
	{
		ApplicationEnvironment appEnvironment = 
			(ApplicationEnvironment)Environment.the.get();
		/*
		for (int i=0; i<size(); i++)
		{
			Preference pref = (Preference) get(i);
			println("processing preference: " + pref);
			appEnvironment.setProperty(pref.name, pref.value);
		}
		*/
		ObjectRegistry preferencesRegistry		= appEnvironment.preferencesRegistry();
		for (int i=0; i<size(); i++)
		{
			Preference pref = (Preference) get(i);
			println("processing preference: " + pref);
			ElementState child	= pref.child();
			// is there at least one child?
			if (child != null)
			{
				preferencesRegistry.registerObject(pref.name, pref);
			}
			else
			{
				String value = pref.value;
				if (value != null)
					preferencesRegistry.registerObject(pref.name, value);
			}
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
