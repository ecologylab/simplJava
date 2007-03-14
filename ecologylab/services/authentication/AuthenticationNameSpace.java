package ecologylab.services.authentication;

import ecologylab.xml.TranslationSpace;
import ecologylab.xml.library.geom.Ellipse2DDoubleState;
import ecologylab.xml.types.element.HashMapState;
import ecologylab.xml.types.element.StringState;

/**
 * Contains all of the information necessary to translate XML objects used in
 * the network communications of the Rogue game. Use MasterNameSpace.get() to
 * acquire a NameSpace object fit for use in the game.
 * 
 * @author Zach Toups
 * 
 */
public class AuthenticationNameSpace
{

    public static final String    NAME           = "rogue_services";

    protected static final String PACKAGE_NAME   = "rogue";

    protected static final Class  TRANSLATIONS[] =
                                                 {

            ecologylab.services.authentication.messages.Login.class,
            ecologylab.services.authentication.messages.Logout.class,
            ecologylab.services.authentication.messages.LoginStatusResponse.class,
            ecologylab.services.authentication.messages.LogoutStatusResponse.class,
            ecologylab.services.authentication.AuthenticationListEntry.class,
            ecologylab.services.authentication.AuthenticationList.class };

    public static TranslationSpace get()
    {
        return TranslationSpace.get(NAME, PACKAGE_NAME, TRANSLATIONS);
    }

    /**
     * @return the TRANSLATIONS
     */
    public static Class[] getTRANSLATIONS()
    {
        return TRANSLATIONS;
    }
}
