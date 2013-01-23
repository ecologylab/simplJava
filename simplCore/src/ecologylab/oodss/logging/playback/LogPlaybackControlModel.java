/*
 * Created on Apr 12, 2007
 */
package ecologylab.oodss.logging.playback;

import java.util.LinkedList;
import java.util.List;

import javax.swing.BoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ecologylab.generic.Debug;
import ecologylab.oodss.logging.Logging;
import ecologylab.oodss.logging.MixedInitiativeOp;
import ecologylab.oodss.logging.Prologue;

/**
 * Model of playback for logged operations. Controls advancement through a log during playback, as well as rewind, etc.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public class LogPlaybackControlModel<E extends MixedInitiativeOp, T extends Logging<E>> implements BoundedRangeModel
{
	protected T							log;

	private int							currentPlaybackOp	= 0;

	private boolean					singleEventMode	= false;

	private List<ChangeListener>	changeListeners	= new LinkedList<ChangeListener>();
	
	protected long						startTime 			= 0;

	/**
	 * 
	 */
	public LogPlaybackControlModel(T log)
	{
		this.log = log;
	}

	public T getLog()
	{
		return log;
	}
	
	/**
	 * Returns the control to the first op.
	 * 
	 */
	public void reset()
	{
		this.setPlaybackOpTo(0);
	}

	/**
	 * Advances the current op by one.
	 * 
	 */
	public void forward()
	{
		this.setPlaybackOpTo(currentPlaybackOp + 1);
	}

	/**
	 * Moves back one op.
	 * 
	 */
	public void back()
	{
		this.setPlaybackOpTo(currentPlaybackOp - 1);
	}

	public E getCurrentOp()
	{
		return log.getOpSequence().get(currentPlaybackOp);
	}

	/**
	 * @see javax.swing.BoundedRangeModel#addChangeListener(javax.swing.event.ChangeListener)
	 */
	@Override
	public void addChangeListener(ChangeListener arg0)
	{
		changeListeners.add(arg0);
	}

	/**
	 * @see javax.swing.BoundedRangeModel#getExtent()
	 */
	@Override
	public int getExtent()
	{
		return log.size();
	}

	/**
	 * @see javax.swing.BoundedRangeModel#getMaximum()
	 */
	@Override
	public int getMaximum()
	{
		return log.size() - 1;
	}

	/**
	 * @see javax.swing.BoundedRangeModel#getMinimum()
	 */
	@Override
	public int getMinimum()
	{
		return 0;
	}

	/**
	 * @see javax.swing.BoundedRangeModel#getValue()
	 */
	@Override
	public int getValue()
	{
		return currentPlaybackOp;
	}

	/**
	 * @see javax.swing.BoundedRangeModel#getValueIsAdjusting()
	 */
	@Override
	public boolean getValueIsAdjusting()
	{
		return singleEventMode;
	}

	/**
	 * @see javax.swing.BoundedRangeModel#removeChangeListener(javax.swing.event.ChangeListener)
	 */
	@Override
	public void removeChangeListener(ChangeListener arg0)
	{
		changeListeners.remove(arg0);
	}

	/**
	 * Throws an UnsupportedOperationException; minimum is set by the underlying log data.
	 * 
	 * @see javax.swing.BoundedRangeModel#setMinimum(int)
	 */
	@Override
	public void setMinimum(int arg0) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("Cannot set minimum.");
	}

	/**
	 * Throws an UnsupportedOperationException; maximum is set by the underlying log data.
	 * 
	 * @see javax.swing.BoundedRangeModel#setMaximum(int)
	 */
	@Override
	public void setMaximum(int arg0) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("Cannot set maximum.");
	}

	@Override
	public void setExtent(int arg0) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("Extent set by underlying data; cannot set extent manually.");
	}

	@Override
	public void setRangeProperties(int arg0, int arg1, int arg2, int arg3, boolean arg4)
	{
		throw new UnsupportedOperationException(
				"Range properties set by underlying data; cannot set properties manually.");
	}

	/**
	 * @see javax.swing.BoundedRangeModel#setValue(int)
	 */
	@Override
	public void setValue(int arg0)
	{
		this.setPlaybackOpTo(arg0);
	}

	protected void setPlaybackOpTo(int index)
	{
		if (index > (this.getMaximum()))
		{
			setPlaybackOpTo(this.getMaximum());
		}
		else if (index < 0)
		{
			setPlaybackOpTo(0);
		}
		else
		{
			currentPlaybackOp = index;
		}

		if (!singleEventMode)
		{
			this.fireChangeEvent();
		}
	}

	/**
	 * @see javax.swing.BoundedRangeModel#setValueIsAdjusting(boolean)
	 */
	@Override
	public void setValueIsAdjusting(boolean arg0)
	{
		// if (singleEventMode && !arg0)
		// { // transition from true to false fires events
		// this.fireChangeEvent();
		// }

		// this.singleEventMode = arg0;
	}

	private void fireChangeEvent()
	{
		for (ChangeListener l : changeListeners)
		{
			l.stateChanged(new ChangeEvent(this));
		}
	}

	/**
	 * Returns the next operation after the current one if one exists, otherwise returns the current operation.
	 * 
	 * @return
	 */
	public E getNext()
	{
		if (this.currentPlaybackOp != (this.getMaximum()))
		{
			return log.getOpSequence().get(currentPlaybackOp + 1);
		}
		else
		{
			Debug.println("last op");
			return this.getCurrentOp();
		}
	}
	
	public Prologue getLogPrologue()
	{
		return this.log.getPrologue();
	}
	
	public void setStartPoint()
	{
		startTime = this.getCurrentOp().getSessionTime();
	}
	
	public long getTimeOffset()
	{
		return this.getCurrentOp().getSessionTime() - startTime;
	}
	
}
