package ecologylab.services.messages;

import java.util.Collection;

import ecologylab.generic.ApplicationEnvironment;
import ecologylab.generic.ApplicationPropertyNames;
import ecologylab.generic.Environment;
import ecologylab.generic.ObjectRegistry;
import ecologylab.net.ParsedURL;
import ecologylab.xml.ArrayListState;
import ecologylab.xml.ElementState;
import ecologylab.xml.xml_inherit;

/**
 * A top level message between javascript and CFSessionLauncher.
 */
public @xml_inherit class PreferencesSet extends ArrayListState
implements ApplicationPropertyNames
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
	 * @return
	 */
	protected Collection getCollection(Class thatClass)
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
		
		// note! read directly here instead of with a static in ApplicationProperties,
		// because now is too early to initialize the other static variables in that interface,
		// because their values are likely to be changed through subsequent preference initialization
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
