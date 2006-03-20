package cf.services.messages;

import java.util.ArrayList;



import ecologylab.generic.ApplicationEnvironment;
import ecologylab.generic.Environment;
import ecologylab.generic.ObjectRegistry;
import ecologylab.generic.ParsedURL;
import ecologylab.generic.ApplicationProperties;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.ArrayListState;
import ecologylab.xml.ElementState;

/**
 * The message sent by CFServicesClientApplet to combinFormation at startup to configure preferences.
 *
 * @author blake
 * @author andruid
 */
public class SetPreferences 
extends RequestMessage
implements ApplicationProperties
{
	public ArrayListState	preferencesSet = new ArrayListState();
	
	public void addNestedElement(ElementState preferenceState)
	{
		if (preferenceState instanceof Preference)
			preferencesSet.addNestedElement(preferenceState);
	}

	public ResponseMessage performService(ObjectRegistry objectRegistry) 
	{
		System.out.println("cf services: received preferences");
		
    	//now internally set the preferences

		ApplicationEnvironment appEnvironment = 
								(ApplicationEnvironment)Environment.the.get();
		ArrayList prefs = preferencesSet.set;
    	for (int i=0; i<prefs.size(); i++)
    	{
    		Preference pref = (Preference)prefs.get(i);
    		appEnvironment.setProperty(pref.name, pref.value);
    	}
    	
    	String codeBasePref	= (String) appEnvironment.parameter(CODEBASE);

    	ParsedURL codeBase	= ParsedURL.getAbsolute(codeBasePref, "codebase");
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
		System.out.println("Preferences: " + preferencesSet);

		return new ResponseMessage(OK);
	}
}
