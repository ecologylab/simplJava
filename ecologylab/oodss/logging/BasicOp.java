package ecologylab.oodss.logging;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.simpl_inherit;

/**
 * A basic operation that only logs the time that operation took place.
 * @author alexgrau
 */
abstract public @simpl_inherit class BasicOp extends ElementState
{
	/** Elapsed time since the session started. */
	@simpl_scalar protected long	sessionTime;
	
	/** No-argument constructor for XML translation. */
	public BasicOp()
	{
		super();
	}
	
	/**
	 * Sets the session time immediately before translating to XML. The session time is based from the time the log
	 * started recording.
	 * 
	 * @see ecologylab.serialization.ElementState#preTranslationProcessingHook()
	 */
	@Override protected void preTranslationProcessingHook(){
		//this.sessionTime = System.currentTimeMillis() - Logging.sessionStartTime();
	}

	/** @return the sessionTime */
	public long getSessionTime(){
		return sessionTime;
	}
	
	/** Free resources associated with this. */
	public void recycle(boolean invert){}
}
