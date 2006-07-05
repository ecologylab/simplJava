package ecologylab.generic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Utility routines for working with reflection.
 *
 * @author andruid
 */
public class ReflectionTools extends Debug
{

/**
 * Wraps the no argument getInstance() method.
 * Checks to see if the class object passed in is null, or if
 * any exceptions are thrown by newInstance().
 * 
 * @param thatClass
 * @return	An instance of an object of the specified class, or null if the Class object was null or
 * an InstantiationException or IllegalAccessException was thrown in the attempt to instantiate.
 */
  	public static Object getInstance(Class thatClass)
  	{
  		Object result		= null;
  		if (thatClass != null)
  		{
  			try
  			{
  				result        	= thatClass.newInstance();
  			} catch (InstantiationException e)
  			{
  				e.printStackTrace();
  			} catch (IllegalAccessException e)
  			{
  				e.printStackTrace();
  			}
  		}
  		return result;
  	}

  	/**
  	 * Wraps the no argument getInstance() method.
  	 * Checks to see if the class object passed in is null, or if
  	 * any exceptions are thrown by newInstance().
  	 * 
  	 * @param thatClass
  	 * @return	An instance of an object of the specified class, or null if the Class object was null or
  	 * an InstantiationException or IllegalAccessException was thrown in the attempt to instantiate.
  	 */
  	  	public static Object getInstance(Class thatClass, Class[] parameterTypes, Object[] args)
  	  	{
  	  		Object result				= null;
  	  		if (thatClass != null)
  	  		{
	  	  		try
	  	  		{
					Constructor constructor	= thatClass.getDeclaredConstructor(parameterTypes);
					if (constructor != null)
						result		 		= constructor.newInstance(args);
					
				} catch (SecurityException e1)
				{
					e1.printStackTrace();
				} catch (NoSuchMethodException e1)
				{
					e1.printStackTrace();
				} catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				} catch (InstantiationException e)
				{
					e.printStackTrace();
				} catch (IllegalAccessException e)
				{
					e.printStackTrace();
				} catch (InvocationTargetException e)
				{
					e.printStackTrace();
				}
  	  		}
  	  		return result;
  	  	}

}
