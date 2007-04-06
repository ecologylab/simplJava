package ecologylab.appframework;

import java.util.ArrayList;

import ecologylab.generic.ConsoleUtils;
import ecologylab.generic.Debug;
import ecologylab.generic.ExceptionHandler;


/**
 * Handle running out of memory. Basically, inform the user, ask if they wish
 * to save, then exit combinformation.
 * 
 */
public class OutOfMemoryErrorHandler  
extends Debug
{
	private static ArrayList 		handlers 		= new ArrayList();
	private static ObjectRegistry 	objectRegistry;
	
	private static boolean			previouslyRanOut			= false;
	
	/**
	 * The OutOfMemoryError memory threshold (in bytes) that means we should shut down
	 */
	private static final long		OUT_OF_MEMORY_THRESHOLD		= 4000000;
	
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
		//in case we get here prematurely
		if (Memory.getFreeMemoryInBytes() > OUT_OF_MEMORY_THRESHOLD)
		{
			ConsoleUtils.obtrusiveConsoleOutput("OutOfMemoryErrorHandler	BYPASSED PREMATURE MEMORY ERROR");
			e.printStackTrace();
			return;
		}
		
		//ConsoleUtils.obtrusiveConsoleOutput("Free Memory: " + currentFreeMemory + " bytes");
		//ConsoleUtils.obtrusiveConsoleOutput("Memory Threshold: " + OUT_OF_MEMORY_THRESHOLD + " bytes");
		
		//don't bother synchronizing because we're probably hosed and can't do it.
		if (previouslyRanOut) //we already handled it
			return;
		previouslyRanOut = true;
		
		Debug.println("Calling OUT OF MEMORY Handlers cause: ");
		e.printStackTrace();
		for (int i=0; i<handlers.size(); i++)
		{
			((ExceptionHandler)handlers.get(i)).handleException(new Exception(e.getMessage()),
												objectRegistry);
		}
	}
}
