/**
 * 
 */
package simpl.core;

import ecologylab.generic.Debug;

/**
 * A TranslationsClassProvider is used to supply an array of Classes for a translation scope. They
 * are extensible so that subclasses can provide expanded lists of Classes.
 * 
 * To use, one instantiates the specific subclass and invokes the provideClasses() method.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public abstract class TranslationsClassProvider extends Debug
{
	private final Class[]	providedClasses;

	/**
	 * 
	 */
	public TranslationsClassProvider()
	{
		providedClasses = specificSuppliedClasses();
	}

	/**
	 * Subclasses must override this method to provide their specific supplied classes. Subclasses of
	 * subclasses should provide the combination of their own (new) class arrays and those of their
	 * superclass.
	 * 
	 * The combineClassArrays static method is useful for this.
	 * 
	 * @return
	 */
	protected abstract Class[] specificSuppliedClasses();

	/**
	 * Convenience method for combining a number of Class arrays into a single array.
	 * 
	 * @param classArray1
	 * @param classArray2
	 * @return
	 */
	protected static Class[] combineClassArrays(Class[]... classArrays)
	{
		int combinedArrayLength = 0;
		
		for (Class[] classArray : classArrays)
		{
			combinedArrayLength += classArray.length;
		}

		Class[] returnArray = new Class[combinedArrayLength];
		
		int copyPos = 0;
		
		for (Class[] classArray : classArrays)
		{
			System.arraycopy(classArray, 0, returnArray, copyPos, classArray.length);
			copyPos += classArray.length;
		}
		
		return returnArray;
	}

	public final Class[] provideClasses()
	{
		return providedClasses;
	}
}
