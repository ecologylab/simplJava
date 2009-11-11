/*
 * Created on May 3, 2006
 */
package ecologylab.services.distributed.common;


/**
 * Constants used by ServicesServers and their components.
 */
public interface ServerConstants extends NetworkingConstants
{
	/** If we get more bad messages than this, it may be malicous. */
	static final int	MAXIMUM_TRANSMISSION_ERRORS			= 3;

	/** Limit the maximum number of client connection to the server */
	static final int	MAX_CONNECTIONS							= 100;

	/** Maximum amount of time, in milliseconds, a connection can be idle, even if we don't want to drop idle connections. */
	static final int	GARBAGE_CONNECTION_CLEANUP_TIMEOUT	= 8 * 60 * 60 * 1000;	// 8 hours
}
