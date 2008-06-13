package ecologylab.services.logging;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_attribute;
import ecologylab.xml.types.element.ArrayListState;

/**
 * A basic operation that only logs the time that operation took place.
 * @author alexgrau
 */
abstract public @xml_inherit class BasicOp extends ArrayListState
{
	/** Elapsed time since the session started. */
	@xml_attribute protected long	sessionTime;
	
	/** No-argument constructor for XML translation. */
	public BasicOp()
	{
		super();
	}
	
	/**
	 * Sets the session time immediately before translating to XML. The session time is based from the time the log
	 * started recording.
	 * 
	 * @see ecologylab.xml.ElementState#preTranslationProcessingHook()
	 */
	@Override protected void preTranslationProcessingHook(){
		this.sessionTime = System.currentTimeMillis() - Logging.sessionStartTime();
	}

	/** @return the sessionTime */
	public long getSessionTime(){
		return sessionTime;
	}
	
	/** Free resources associated with this. */
	public void recycle(boolean invert){}
}
