/**
 * 
 */
package ecologylab.oodss.distributed.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import ecologylab.generic.Debug;

/**
 * Captures the semantics of the Shutdownable interface, but requires that subclasses implement the
 * shutdown method.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public abstract class Manager extends Debug implements Shutdownable
{
	private List<Shutdownable>		shutdownDependents	= new LinkedList<Shutdownable>();

	private List<ActionListener>	shutdownListeners		= new LinkedList<ActionListener>();

	private boolean								shutdownCalled			= false;

	/**
	 * 
	 */
	public Manager()
	{
		super();
	}

	/**
	 * @see ecologylab.oodss.distributed.impl.Shutdownable#addDependentShutdownable(ecologylab.oodss.distributed.impl.Shutdownable)
	 */
	@Override
	public void addDependentShutdownable(Shutdownable s)
	{
		this.shutdownDependents.add(s);
	}

	/**
	 * @see ecologylab.oodss.distributed.impl.Shutdownable#addShutdownListener(java.awt.event.ActionListener)
	 */
	@Override
	public void addShutdownListener(ActionListener l)
	{
		this.shutdownListeners.add(l);
	}

	/**
	 * @see ecologylab.oodss.distributed.impl.Shutdownable#shutdown()
	 */
	@Override
	public void shutdown()
	{
		if (!shutdownCalled)
		{
			this.shutdownCalled = true;

			// notify listeners of shutdown
			if (this.shutdownListeners.size() > 0)
			{
				ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, SHUTTING_DOWN, System
						.currentTimeMillis(), -1);

				for (ActionListener a : this.shutdownListeners)
				{
					a.actionPerformed(e);
				}
			}

			if (this.shutdownDependents.size() > 0)
			{
				for (Shutdownable s : this.shutdownDependents)
				{
					s.shutdown();
				}
			}

			this.shutdownImpl();
		}
	}

	protected abstract void shutdownImpl();
}
