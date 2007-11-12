/**
 * 
 */
package ecologylab.services.distributed.impl;

/**
 * Interface for objects that should have a shutdown sequence.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public interface Shutdownable
{
	/**
	 * Causes this to start to shutdown, and fires a SERVER_SHUTTING_DOWN event to all listeners.
	 */
	public void shutdown();
}
