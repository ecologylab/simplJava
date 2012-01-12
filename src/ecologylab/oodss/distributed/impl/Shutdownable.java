/**
 * 
 */
package ecologylab.oodss.distributed.impl;

import java.awt.event.ActionListener;

/**
 * Interface for objects that should have a shutdown sequence.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public interface Shutdownable
{
	public static final String	SHUTTING_DOWN	= "SHUTTING_DOWN";

	/**
	 * Causes this to start to shutdown, and fires a SHUTTING_DOWN event to all
	 * listeners.
	 */
	public void shutdown();

	/**
	 * This method allows another application to indicate its dependence on this
	 * to be shutdown. That is, when this's shutdown() method is called, it
	 * should call the shutdown() method on each component that depends on it.
	 * 
	 * Implementors and callers should take care not to create an infinite loop
	 * of shutdown() calls through this method.
	 * 
	 * @param s
	 */
	public void addDependentShutdownable(Shutdownable s);

	/**
	 * This method allows other components to be notified when the shutdown()
	 * method is called. Listeners will get an ActionEvent whose action command
	 * is SHUTTING_DOWN.
	 * 
	 * @param l
	 */
	public void addShutdownListener(ActionListener l);
}
