/*
 * Created on Nov 19, 2007
 */
package ecologylab.generic;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This class provides access to a pool of pre-allocated resources. The pool
 * grows and shrinks throughout its lifetime to suit the number of resources
 * necessary.
 * 
 * The primary way of accessing resources that are controlled by a pool are
 * through the acquire and release methods. Every acquire should have a matching
 * release, to ensure that resources may be recycled by later calls to acquire.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class ResourcePool<T>
{
	ArrayList<T>	pool;

	/**
	 * 
	 */
	public ResourcePool()
	{
		pool = new LinkedList<T>();
	}

	public T acquire()
	{
		T retVal;

		synchronized (pool)
		{
			retVal = pool.poll();
			
			if (retVal == null)
			{
				retVal = this.generateNewResource();
			}
		}
	}

	public synchronized void release(T resourceToRelease)
	{

	}
}
