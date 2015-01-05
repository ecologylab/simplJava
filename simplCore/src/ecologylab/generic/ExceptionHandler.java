package ecologylab.generic;

import ecologylab.collections.Scope;

/**
 * An exception handler used for callbacks. 
 * 
 *  @author Blake Dworaczyk
 */
public abstract class ExceptionHandler
implements Runnable
{
	protected Exception 		exception;
	protected Scope 	objectRegistry;
	
	public void handleException(Exception e, Scope objectRegistry)
	{
		this.exception = e;
		this.objectRegistry = objectRegistry;
		
		Thread thread = new Thread(this);
		thread.setPriority(10);
		thread.run();
	}
	
}
