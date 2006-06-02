package ecologylab.generic;

import java.util.ArrayList;


/**
 * Handle running out of memory. Basically, inform the user, ask if they wish
 * to save, then exit combinformation.
 * 
 */
public class OutOfMemoryErrorHandler  
{
	private static ArrayList 		handlers 		= new ArrayList();
	private static ObjectRegistry 	objectRegistry;
	
	private static boolean			previouslyRanOut			= false;
	
	private OutOfMemoryErrorHandler() {}
	
	public static void registerObjectRegistry(ObjectRegistry oRegistry)
	{
		objectRegistry = oRegistry;
	}
	
	/**
	 * Register an ExceptionHandler for callback when an exception
	 * is thrown
	 * @param exceptionHandler The ExceptionHandler to add.
	 */
	public static void registerHandler(ExceptionHandler exceptionHandler)
	{
		handlers.add(exceptionHandler);
	}
	
	/**
	 * Call to register that an exception has ocurred. Results in all
	 * registered handlers being notified.
	 * @param e
	 */
	public static void handleException(OutOfMemoryError e)
	{
		//don't bother synchronizing because we're probably hosed and can't do it.
		if (previouslyRanOut) //we already handled it
			return;
		previouslyRanOut = true;
		
		for (int i=0; i<handlers.size(); i++)
		{
			((ExceptionHandler)handlers.get(i)).handleException(new Exception(e.getMessage()),
												objectRegistry);
		}
	}
}
