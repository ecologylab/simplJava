/**
 * 
 */
package ecologylab.services.messages.cf;

import ecologylab.generic.Debug;
import ecologylab.services.messages.*;
import ecologylab.xml.TranslationScope;

/**
 * TranslationSpace for client-side CFServices.
 * 
 * @author andruid
 */
public class CFMessagesTranslations extends Debug
{
	public static final String	PACKAGE_NAME	= "ecologylab.services.messages.cf";
	
	public static final Class	TRANSLATIONS[]	= 
	{ 
		SeedCf.class,

		SeedSet.class,
		Seed.class,
		DocumentState.class,
		SearchState.class,
				
		CfCollaborationGetSurrogate.class,
	};

	/**
	 * 
	 */
	public CFMessagesTranslations()
	{
		super();

	}

	/**
	 * This accessor will work from anywhere, in any order, and stay efficient.
	 * @return	TranslationSpace for cF services.
	 */
	public static TranslationScope get()
	{
		return TranslationScope.get(PACKAGE_NAME, TRANSLATIONS, DefaultServicesTranslations.get());
	}
}
