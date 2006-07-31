package cf.services.messages;

import cf.app.CMShellApplication;
import cf.app.CollageMachine;
import ecologylab.generic.ConsoleUtils;
import ecologylab.generic.ObjectRegistry;
import ecologylab.services.messages.OkResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;

/**
 * The message sent by CFServicesClientApplet to combinFormation at startup to configure preferences.
 *
 * @author blake
 * @author andruid
 */
public class SetPreferences 
extends RequestMessage
{
	static boolean			firstTime		= true;
	
	public PreferencesSet	preferencesSet	= new PreferencesSet();
	
	
	public SetPreferences()
	{
		super();
	}

	public SetPreferences(PreferencesSet preferencesSet)
	{
		super();
		this.preferencesSet		= preferencesSet;
	}
	public SetPreferences(String preferencesSetString, TranslationSpace nameSpace)
	throws XmlTranslationException
	{
		this((PreferencesSet) translateFromXMLString(preferencesSetString, nameSpace));
	}

	public ResponseMessage performService(ObjectRegistry objectRegistry) 
	{
		debug("cf services: received new preferences: " + preferencesSet);
		if (firstTime)
		{
			firstTime		= false;
	    	//now internally set the preferences
			preferencesSet.processPreferences();
			//print the prefs
			debug("performService() Received and loaded preferences: " + preferencesSet);
	
			
			CollageMachine collageMachine	= CMShellApplication.setupCollageMachine(objectRegistry);
			
			collageMachine.start(Thread.NORM_PRIORITY - 1); // build in new Thread to enable concurrency with seed transmission.
			//collageMachine.run();
	        ConsoleUtils.obtrusiveConsoleOutput("SetPreferences.sending ResponseMessage(OK)");
		}
		else
			debug("IGNORING: preferences were previously loaded.");
		
		return OkResponse.get();
	}
}
