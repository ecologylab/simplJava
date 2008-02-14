package ecologylab.appframework.types;

import java.io.File;
import java.util.Collection;

import ecologylab.appframework.ApplicationEnvironment;
import ecologylab.appframework.ApplicationPropertyNames;
import ecologylab.appframework.Environment;
import ecologylab.collections.Scope;
import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XMLTranslationException;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.types.element.ArrayListState;

/**
 * A top level message between javascript and CFSessionLauncher.
 */
@Deprecated public @xml_inherit class PreferencesSet extends ArrayListState
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
	 * @param thatClass		The type of the ElementState superclass that could be stored in a Collection.
	 * @return		Collection for objects of the specified type.
	 */
	protected Collection getCollection(Class thatClass)
	{
		return Preference.class.equals(thatClass) ? set() : null;
	}
	
	public void processPreferences()
	{
		Scope preferencesRegistry		= Preference.preferencesRegistry();
		for (int i=0; i<size(); i++)
		{
			Preference pref = (Preference) get(i);
			pref.register(preferencesRegistry);
			if (CODEBASE.equals(pref.name))
			{
				String value	= pref.value;
				if (value != null)
				{
					ParsedURL codeBase	= ParsedURL.getAbsolute(value, "Setting up codebase");
					if (codeBase != null)
					{
						Environment env	= Environment.the.get();
						if (env instanceof ApplicationEnvironment)
						{
							debug("SetPreferences setting codeBase="+codeBase);
							((ApplicationEnvironment) env).setCodeBase(codeBase);
						}
					}
				}
			}
		}
	}

	/**
	 * Load an ecologylab style preferences file.
	 * 
	 * @param translationSpace
	 * @param path
	 * @param prefFilePath
	 */
	public static void loadPreferencesXML(TranslationSpace translationSpace, File path, String prefFilePath)
	{
		File preferencesXMLFile	= new File(path, prefFilePath);
		if (preferencesXMLFile.exists())
		{
			try
			{
				println("Loading preferences from: " + preferencesXMLFile);
				PreferencesSet ps	= (PreferencesSet) ElementState.translateFromXML(preferencesXMLFile, translationSpace);
				ps.processPreferences();
			} catch (XMLTranslationException e)
			{
				error(preferencesXMLFile, "Caught exception while reading preferences:");
				e.printStackTrace();
			}
		}
		else
			error(preferencesXMLFile, "Can't find preferences file.");
	}
}
