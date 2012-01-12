package ecologylab.oodss.messages;

import ecologylab.appframework.types.prefs.PrefSet;
import ecologylab.collections.Scope;
import ecologylab.generic.ConsoleUtils;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.formatenums.StringFormat;

/**
 * The message sent by ServicesClientApplet to Java application running ServicesServer at 
 * startup to configure preferences.
 *
 * @author blake
 * @author andruid
 */
@simpl_inherit
@Deprecated public class SetPreferences 
extends RequestMessage
{
	static boolean			firstTime		= true;
	
	@simpl_composite protected	 PrefSet		preferencesSet;
	
	public SetPreferences()
	{
		super();
	}
	
	public SetPreferences(PrefSet preferencesSet)
	{
		super();
		this.preferencesSet = preferencesSet;
	}
	public SetPreferences(String preferencesSetString, SimplTypesScope translationScope)
	throws SIMPLTranslationException
	{
		this((PrefSet) translationScope.deserialize(preferencesSetString, StringFormat.XML));
	}

	/**
     * Adds the set of Prefs to the Preferences registry on the host machine. This is now generally handled automatically.
     */
	@Override public ResponseMessage performService(Scope clientConnectionScope) 
	{
		debug("performService() " + clientConnectionScope.dump());
		if (firstTime)
		{
			firstTime		= false;
			
			ResponseMessage response = setupApplication(clientConnectionScope);
			if (response instanceof ErrorResponse)
			{
				handleErrorWhileLoading(clientConnectionScope);
				return response;
			}
			
	        ConsoleUtils.obtrusiveConsoleOutput("SetPreferences.sending ResponseMessage(OK)");
		}
		else
		{
			handleAlreadyLoaded(clientConnectionScope);
		}
		
		return OkResponse.get();
	}
	
	/**
	 * This is a hook inside the performServices method which should be overridden by any
	 * class that extends this object. It defines the steps necessary to setup an application
	 * upon receiving and processing a SetPreferences message.
	 * 
	 * @param clientConnectionScope
	 */
	protected ResponseMessage setupApplication(Scope clientConnectionScope) { return OkResponse.get(); }
	
	/**
	 * Method for handling all SetPreferences messages after the first. Should be overridden by
	 * any class that extends this object to properly handle these situations on message specific basis.
	 *
	 */
	protected void handleAlreadyLoaded(Scope clientConnectionScope)
	{
		debug("IGNORING: preferences were previously loaded.");
	}
	
	protected void handleErrorWhileLoading(Scope clientConnectionScope)
	{
		
	}
	
	public PrefSet preferencesSet()
	{
		return this.preferencesSet;
	}
}
