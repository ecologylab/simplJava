package ecologylab.services.authentication;

import ecologylab.services.messages.DefaultServicesTranslations;
import ecologylab.xml.TranslationSpace;

/**
 * Contains all of the information necessary to translate XML objects used in an authenticating server. Use
 * AuthenticationTranslations.get() to acquire a TranslationSpace.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public class AuthenticationTranslations
{
	protected static final String	NAME					= "authentication";

	protected static final String	DEFAULT_PACKAGE	= "ecologylab.services.authentication";

	protected static final Class	TRANSLATIONS[]		=
																	{ ecologylab.services.authentication.messages.Login.class,
			ecologylab.services.authentication.messages.Logout.class,
			ecologylab.services.authentication.messages.LoginStatusResponse.class,
			ecologylab.services.authentication.messages.LogoutStatusResponse.class,
			ecologylab.services.authentication.AuthenticationListEntry.class,
			ecologylab.services.authentication.AuthenticationList.class };

	public static TranslationSpace get()
	{
		return TranslationSpace.get(NAME, TRANSLATIONS, DefaultServicesTranslations.get(), DEFAULT_PACKAGE);
	}
}
