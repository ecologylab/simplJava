/*
 * Created on May 15, 2006
 */
package ecologylab.services.distributed.common;

/**
 * States for clients in a networked application.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public interface BaseStates
{
	/** Client not currently connected to any server. */
	public static final String	NOT_CONNECTED	= "Not connected.";

	/** Client currently attempting to connect to a server. */
	public static final String	CONNECTING		= "Connecting.";

	/** Client connected to a server. */
	public static final String	CONNECTED		= "Connected.";
}
