package ecologylab.services.authentication;

/**
 * Constants for administrator levels. May be extended to include other levels.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public interface AuthLevels
{
	/** User is an administrator. */
	final static int	ADMINISTRATOR	= 10;

	/** User is normal. */
	final static int	NORMAL_USER		= 0;
}
