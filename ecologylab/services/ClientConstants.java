/*
 * Created on May 5, 2006
 */
package ecologylab.services;

/**
 * Constant settings for client functionality.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public interface ClientConstants
{
	/** Number of reconnect attempts to make before giving up. */
	static final int	RECONNECT_ATTEMPTS					= 50;

	/** Number of milliseconds to sleep bewteen attempts to reconnect. */
	static final int	WAIT_BEWTEEN_RECONNECT_ATTEMPTS	= 3000;
}
