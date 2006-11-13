package ecologylab.services.messages;

import java.io.File;

import ecologylab.generic.ConsoleUtils;
import ecologylab.generic.Debug;
import ecologylab.generic.Generic;
import ecologylab.generic.ObjectRegistry;
import ecologylab.generic.AssetsCache.Assets;
import ecologylab.gui.Status;
import ecologylab.net.ParsedURL;
import ecologylab.services.messages.OkResponse;
import ecologylab.services.messages.PreferencesSet;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_attribute;

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
	
	public SetPreferences(String preferencesSetString, TranslationSpace nameSpace)
	throws XmlTranslationException
	{
		this((PreferencesSet) translateFromXMLString(preferencesSetString, nameSpace));
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
			
			setupApplication(objectRegistry);
			
	        ConsoleUtils.obtrusiveConsoleOutput("SetPreferences.sending ResponseMessage(OK)");
		}
		else
			debug("IGNORING: preferences were previously loaded.");
		
		return OkResponse.get();
	}
	
	/**
	 * This is a hook inside the performServices method which should be overridden by any
	 * class that extends this object. It defines the steps necessary to setup an application
	 * upon receiving and processing a SetPreferences message.
	 * 
	 * @param objectRegistry
	 */
	protected void setupApplication(ObjectRegistry objectRegistry) { }
}
