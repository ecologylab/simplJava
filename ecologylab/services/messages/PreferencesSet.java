package cf.services.messages;

import java.util.ArrayList;



import ecologylab.generic.ApplicationEnvironment;
import ecologylab.generic.Environment;
import ecologylab.generic.ObjectRegistry;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.services.messages.ResponseTypes;
import ecologylab.xml.ArrayListState;
import ecologylab.xml.ElementState;

public class PreferencesSet 
extends RequestMessage
{
	public ArrayListState	preferencesSet = new ArrayListState();
	
	public void addNestedElement(ElementState preferenceState)
	{
		if (preferenceState instanceof Preference)
			preferencesSet.addNestedElement(preferenceState);
	}

	public ResponseMessage performService(RequestMessage requestMessage, ObjectRegistry objectRegistry) 
	{
		System.out.println("cf services: received preferences");
		
	    PreferencesSet prefSet = (PreferencesSet) requestMessage;
	    if (prefSet != null)
	    {
	    	//now internally set the preferences
	    	if (prefSet != null)
            {
	    		ApplicationEnvironment appEnvironment = 
	    								(ApplicationEnvironment)Environment.the.get();
	    		ArrayList prefs = prefSet.preferencesSet.set;
            	for (int i=0; i<prefs.size(); i++)
            	{
            		Preference pref = (Preference)prefs.get(i);
            		appEnvironment.setProperty(pref.name, pref.value);
            	}
            	//print the prefs
				System.out.println("Preferences: " + prefSet);
            }
	    	
	    	System.out.println("cf services: sending postive response");
	    	return new ResponseMessage(OK);
	    }
	    else
	    {
	    	System.out.println("cf services: PREFS FAILED! sending NEGATIVE response");
	    	return new ResponseMessage(BAD);
	    }
	}
}
