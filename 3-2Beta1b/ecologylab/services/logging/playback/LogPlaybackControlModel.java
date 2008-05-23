/*
 * Created on Apr 12, 2007
 */
package ecologylab.services.logging.playback;

import java.util.LinkedList;
import java.util.List;

import javax.swing.BoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ecologylab.services.logging.Logging;
import ecologylab.services.logging.MixedInitiativeOp;
import ecologylab.services.logging.Prologue;

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

	/**
	 * 
	 */
	public LogPlaybackControlModel(T log)
	{
		this.log = log;
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
	public void addChangeListener(ChangeListener arg0)
	{
		changeListeners.add(arg0);
	}

	/**
	 * @see javax.swing.BoundedRangeModel#getExtent()
	 */
	public int getExtent()
	{
		return log.size();
	}

	/**
	 * @see javax.swing.BoundedRangeModel#getMaximum()
	 */
	public int getMaximum()
	{
		return log.size() - 1;
	}

	/**
	 * @see javax.swing.BoundedRangeModel#getMinimum()
	 */
	public int getMinimum()
	{
		return 0;
	}

	/**
	 * @see javax.swing.BoundedRangeModel#getValue()
	 */
	public int getValue()
	{
		return currentPlaybackOp;
	}

	/**
	 * @see javax.swing.BoundedRangeModel#getValueIsAdjusting()
	 */
	public boolean getValueIsAdjusting()
	{
		return singleEventMode;
	}

	/**
	 * @see javax.swing.BoundedRangeModel#removeChangeListener(javax.swing.event.ChangeListener)
	 */
	public void removeChangeListener(ChangeListener arg0)
	{
		changeListeners.remove(arg0);
	}

	/**
	 * Throws an UnsupportedOperationException; minimum is set by the underlying log data.
	 * 
	 * @see javax.swing.BoundedRangeModel#setMinimum(int)
	 */
	public void setMinimum(int arg0) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("Cannot set minimum.");
	}

	/**
	 * Throws an UnsupportedOperationException; maximum is set by the underlying log data.
	 * 
	 * @see javax.swing.BoundedRangeModel#setMaximum(int)
	 */
	public void setMaximum(int arg0) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("Cannot set maximum.");
	}

	public void setExtent(int arg0) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException("Extent set by underlying data; cannot set extent manually.");
	}

	public void setRangeProperties(int arg0, int arg1, int arg2, int arg3, boolean arg4)
	{
		throw new UnsupportedOperationException(
				"Range properties set by underlying data; cannot set properties manually.");
	}

	/**
	 * @see javax.swing.BoundedRangeModel#setValue(int)
	 */
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
			return this.getCurrentOp();
		}
	}
	
	public Prologue getLogPrologue()
	{
		return this.log.getPrologue();
	}
}
