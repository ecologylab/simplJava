package ecologylab.services.messages;

import javax.swing.JOptionPane;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.generic.ConsoleUtils;
import ecologylab.io.Assets;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;
import ecologylab.xml.xml_inherit;

/**
 * The message sent by ServicesClientApplet to Java application running ServicesServer at 
 * startup to configure preferences.
 *
 * @author blake
 * @author andruid
 */
@xml_inherit
public class SetPreferences 
extends RequestMessage
{
	static boolean			firstTime		= true;
	
	@xml_nested protected	 PreferencesSet		preferencesSet	= new PreferencesSet();
	@xml_attribute protected String				preferencesSetAssetPath;
	//public PreferencesSet   overridePreferencesSet = new PreferencesSet();
	
	
	public SetPreferences()
	{
		super();
	}
	
	public SetPreferences(PreferencesSet preferencesSet)
	{
		super();
		this.preferencesSet = preferencesSet;
	}
	
	public SetPreferences(PreferencesSet preferencesSet, String preferencesSetAssetPath)
	{
		this(preferencesSet);
		this.preferencesSetAssetPath = preferencesSetAssetPath;
	}
	
	public SetPreferences(String preferencesSetString, TranslationSpace translationSpace)
	throws XmlTranslationException
	{
		this((PreferencesSet) translateFromXMLString(preferencesSetString, translationSpace));
	}
	
	public SetPreferences(String preferencesSetAssetPath, String overridePreferencesSetString, TranslationSpace nameSpace)
	throws XmlTranslationException
	{
		this (overridePreferencesSetString, nameSpace);
		this.preferencesSetAssetPath = preferencesSetAssetPath;
	}

	public ResponseMessage performService(ObjectRegistry objectRegistry) 
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
					PreferencesSet preferencesSetAsset = 
						(PreferencesSet) ElementState.translateFromXML(Assets.getPreferencesFile(preferencesSetAssetPath + ".xml"), DefaultServicesTranslations.get());
					preferencesSetAsset.processPreferences();
					debug("performService() Received and loaded preferences: " + preferencesSetAsset);
				} catch (XmlTranslationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
	    	//now internally set the preferences (overriding any identical preferences set from asset file)
			preferencesSet.processPreferences();
			//print the prefs
			debug("performService() Received and loaded preferences: " + preferencesSet);
			
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
	
	public PreferencesSet preferencesSet()
	{
		return this.preferencesSet;
	}
}
