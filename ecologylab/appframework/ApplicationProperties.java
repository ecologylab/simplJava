package ecologylab.generic;


/**
 * Reusable String constants for getting properties from the environment.
 * 
 * @author andruid
 * @author blake
 */
public interface ApplicationProperties
{
	public static final String	USERINTERFACE_NAME	= "userinterface";
	
	public static final String	USERINTERFACE	= Generic.parameter(USERINTERFACE_NAME);
}
