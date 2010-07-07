/**
 * 
 */
package ecologylab.oodss.distributed.impl;

/**
 * A listener that reacts to server events, such as when a client connects. Mostly useful for logging events on a
 * server.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public interface ServerListener
{
	/**
	 * Takes an action in response to a server event.
	 * 
	 * @param e
	 *           the incoming server event.
	 */
	public void serverEventOccurred(ServerEvent e);
}
