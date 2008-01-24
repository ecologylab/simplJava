package ecologylab.services.messages;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.appframework.types.AppFrameworkTranslations;
import ecologylab.appframework.types.prefs.PrefSet;
import ecologylab.generic.ConsoleUtils;
import ecologylab.io.Assets;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XMLTranslationException;
import ecologylab.xml.xml_inherit;

/**
 * The message sent by ServicesClientApplet to Java application running ServicesServer at 
 * startup to configure preferences.
 *
 * @author blake
 * @author andruid
 */
@xml_inherit
@Deprecated public class SetPreferences 
extends RequestMessage
{
	static boolean			firstTime		= true;
	
	@xml_nested protected	 PrefSet		preferencesSet	= new PrefSet();
	@xml_attribute protected String				preferencesSetAssetPath;
	//public PreferencesSet   overridePreferencesSet = new PreferencesSet();
	
	
	public SetPreferences()
	{
		super();
	}
	
	public SetPreferences(PrefSet preferencesSet)
	{
		super();
		this.preferencesSet = preferencesSet;
	}
	
	public SetPreferences(PrefSet preferencesSet, String preferencesSetAssetPath)
	{
		this(preferencesSet);
		this.preferencesSetAssetPath = preferencesSetAssetPath;
	}
	
	public SetPreferences(String preferencesSetString, TranslationSpace translationSpace)
	throws XMLTranslationException
	{
		this((PrefSet) translateFromXMLCharSequence(preferencesSetString, translationSpace));
	}
	
	public SetPreferences(String preferencesSetAssetPath, String overridePreferencesSetString, TranslationSpace nameSpace)
	throws XMLTranslationException
	{
		this (overridePreferencesSetString, nameSpace);
		this.preferencesSetAssetPath = preferencesSetAssetPath;
	}

    /**
	  * Adds the set of Prefs to the Preferences registry on the host machine. This is now generally handled automatically.
	 * @deprecated Use {@link #performService(ObjectRegistry,String)} instead
	  */
	public ResponseMessage performService(ObjectRegistry objectRegistry) 
	{
		return performService(objectRegistry, null);
	}

	/**
     * Adds the set of Prefs to the Preferences registry on the host machine. This is now generally handled automatically.
     */
	public ResponseMessage performService(ObjectRegistry objectRegistry, String sessionId) 
	{
		debug("performService(): " + preferencesSet +" " + preferencesSet.size());
		if (firstTime)
		{
			firstTime		= false;
			// if preferences file is provided, process first
			if (this.preferencesSetAssetPath != null)
			{
				debug("downloading preferencesSetAssetPath...");
				Assets.downloadPreferencesZip(preferencesSetAssetPath, null, true);
				try {
                    PrefSet preferencesSetAsset = 
						(PrefSet) ElementState.translateFromXML(Assets.getPreferencesFile(preferencesSetAssetPath + ".xml"), AppFrameworkTranslations.get());
//TODO happens automatically					preferencesSetAsset.loadIntoEnvironment();
					debug("performService() Received and loaded preferences: " + preferencesSetAsset);
				} catch (XMLTranslationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				debug("performService() Received and loaded preferences: " + preferencesSet);
			}
			
	    	//now internally set the preferences (overriding any identical preferences set from asset file)
//TODO happens automatically			preferencesSet.loadIntoEnvironment();
			//print the prefs
			
			ResponseMessage response = setupApplication(objectRegistry);
			if (response instanceof ErrorResponse)
			{
				handleErrorWhileLoading(objectRegistry);
				return response;
			}
			
	        ConsoleUtils.obtrusiveConsoleOutput("SetPreferences.sending ResponseMessage(OK)");
		}
		else
		{
			handleAlreadyLoaded(objectRegistry);
		}
		
		return OkResponse.get();
	}
	
	/**
	 * This is a hook inside the performServices method which should be overridden by any
	 * class that extends this object. It defines the steps necessary to setup an application
	 * upon receiving and processing a SetPreferences message.
	 * 
	 * @param objectRegistry
	 */
	protected ResponseMessage setupApplication(ObjectRegistry objectRegistry) { return OkResponse.get(); }

	/**
	 * Set the Asset path used for setPreferences.
	 * 
	 * @param preferencesSetAssetPath
	 */
	public void setPreferencesSetAssetPath(String preferencesSetAssetPath)
	{
		this.preferencesSetAssetPath = preferencesSetAssetPath;
	}
	
	/**
	 * Method for handling all SetPreferences messages after the first. Should be overridden by
	 * any class that extends this object to properly handle these situations on message specific basis.
	 *
	 */
	protected void handleAlreadyLoaded(ObjectRegistry objectRegistry)
	{
		debug("IGNORING: preferences were previously loaded.");
	}
	
	protected void handleErrorWhileLoading(ObjectRegistry objectRegistry)
	{
		
	}
	
	public PrefSet preferencesSet()
	{
		return this.preferencesSet;
	}
}
