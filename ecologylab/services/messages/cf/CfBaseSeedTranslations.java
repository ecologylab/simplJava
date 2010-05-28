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
public class CfBaseSeedTranslations extends Debug
{
	public static final String	TSCOPE_NAME	= "cf_base_seed_translations";
	
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
	public CfBaseSeedTranslations()
	{
		super();

	}

	/**
	 * This accessor will work from anywhere, in any order, and stay efficient.
	 * @return	TranslationSpace for cF services.
	 */
	public static TranslationScope get()
	{
		return TranslationScope.get(TSCOPE_NAME, DefaultServicesTranslations.get(), TRANSLATIONS);
	}
}
