package ecologylab.logging;

import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_scalar;
import simpl.core.ElementState;
import simpl.core.TranslationContext;

/**
 * A basic operation that only logs the time that operation took place.
 * 
 * @author alexgrau
 */
abstract public @simpl_inherit
class BasicOp extends ElementState
{
	/** Elapsed time since the session started. */
	@simpl_scalar
	protected long	sessionTime;

	/** No-argument constructor for XML translation. */
	public BasicOp()
	{
		super();
	}

	/** @return the sessionTime */
	public long getSessionTime()
	{
		return sessionTime;
	}

	/** Free resources associated with this. */
	public void recycle(boolean invert)
	{
	}
}
