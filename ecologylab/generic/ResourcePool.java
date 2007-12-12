/*
 * Created on Nov 19, 2007
 */
package ecologylab.generic;

import java.util.ArrayList;

/**
 * This class provides access to a pool of pre-allocated resources. The pool grows and contracts throughout its lifetime
 * to suit the number of resources necessary and to attempt to minimize memory footprints; acquire() and release() are
 * amortized O(1) complexity (although expansion/contraction triggers may take longer).
 * 
 * The primary way of accessing resources that are controlled by a pool are through the acquire and release methods.
 * Every acquire should have a matching release, to ensure that resources may be recycled by later calls to acquire.
 * 
 * Subclasses of ResourcePool ensure that the resources obtained through the acquire method are "clean", that is, they
 * are immediately ready for use as if they were just instantiated.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public abstract class ResourcePool<T> extends Debug
{
	protected static final int	DEFAULT_POOL_SIZE	= 64;

	ArrayList<T>					pool;

	int								capacity;

	/** Specifies the minimum size for the backing store, to prevent thrashing when small numbers of objects are needed. */
	int								minCapacity;

	float								loadFactor			= .75f;

	/**
	 * Special constructor that will only instantiate the backing pool resources if the first argument is true. This
	 * method can be used by subclasses to set up member variables before calling instantiateResourcesInPool(), so that
	 * the instantiation will use the member variables.
	 * 
	 * @param instantiateResourcesInPool
	 * @param initialPoolSize
	 * @param minimumPoolSize
	 */
	protected ResourcePool(boolean instantiateResourcesInPool, int initialPoolSize, int minimumPoolSize)
	{
		this.capacity = Math.max(initialPoolSize, minimumPoolSize);

		this.pool = new ArrayList<T>(capacity);

		if (instantiateResourcesInPool)
			instantiateResourcesInPool();

		this.minCapacity = minimumPoolSize;
	}

	/**
	 * Creates a new ResourcePool with the specified initialPoolSize (or minimumPoolSize, minimumPoolSize >
	 * initialPoolSize) and minimum capacity.
	 * 
	 * Note that this constructor will call generateNewResource (capacity) times to fill in the backing collection. If
	 * generateNewResource relies upon setting fields, the subclass should *NOT* call this constructor and should instead
	 * call ResourcePool(boolean, int, int).
	 * 
	 * @param initialPoolSize
	 *           the initial size of the backing pool of objects.
	 * @param minimumPoolSize
	 *           the minimum size for the backing pool of objects. This is important to specify, otherwise repeatedly
	 *           aquire()'ing and release()'ing resources can have detrimental performance effects.
	 */
	public ResourcePool(int initialPoolSize, int minimumPoolSize)
	{
		this(true, initialPoolSize, minimumPoolSize);
	}

	/**
	 * Take a resource from the pool, making it unavailable for other segments of the program to use.
	 * 
	 * Objects returned by calls to acquire() are "clean", that is, they are in the same state they would be in as if
	 * they were just instantiated.
	 * 
	 * @return
	 */
	public final T acquire()
	{
		T retVal;

		int freeIndex;

		synchronized (this)
		{ // when acquire()'ing it might be necessary to expand the size of the backing store
			freeIndex = this.pool.size() - 1;

			if (freeIndex == -1)
			{
				this.expandPool();
				freeIndex = this.pool.size() - 1;
			}

			retVal = pool.remove(freeIndex);
		}

		this.clean(retVal);

		return retVal;
	}

	/**
	 * Return a resource for use by another part of the program. The resource will be cleaned at some later time when it
	 * is acquire()'ed. Resources that are released should NOT be used again.
	 * 
	 * @param resourceToRelease
	 * @return null; this is meant as a convienence, so that the programmer can use the line: resource =
	 *         recPool.release(resource);, automatically unlinking the released resource from the binding in the resource
	 *         user's code.
	 */
	public final synchronized T release(T resourceToRelease)
	{
		if (resourceToRelease != null)
		{
			synchronized (this)
			{
				pool.add(resourceToRelease);

				int poolSize = pool.size();

				if (capacity > minCapacity && poolSize > loadFactor * capacity)
				{
					this.contractPool();
				}
			}
		}
		else
		{
			warning("attempt to load a null reference into resource pool.");
		}

		return null;
	}

	/**
	 * Instantiates a resource of type T.
	 * 
	 * @return
	 */
	protected abstract T generateNewResource();

	/**
	 * Ensure that the given Object is "clean", that is, in the state it would be in if it were just instantiated. For
	 * example, if this class were handling StringBuilders, it should ensure that the StringBuilder does not contain any
	 * characters from a previous use.
	 * 
	 * clean(T) is automatically called immediately before an object is returned from the acquire() method.
	 * 
	 * @param objectToClean
	 */
	protected abstract void clean(T objectToClean);

	/**
	 * Increases the number of resources to make available. Doubles the backing Collection size and instantiates objects
	 * to match.
	 * 
	 * expandPool() is not thread-safe and should only be called within a synchronized block.
	 */
	private void expandPool()
	{
		debug("expanding pool from " + capacity + " elements");

		if (capacity > 0)
		{
			/*
			 * double capacity by instantiating <capacity> new objects; this doubles, b/c when this method is called, we
			 * are empty and have already dealt-out <capacity> objects
			 */
			instantiateResourcesInPool();

			capacity *= 2;
		}
		else
		{ // capacity is 0, just put one in there
			this.pool.add(this.generateNewResource());
			capacity = 1;
		}

		debug("to " + capacity + " elements");

		this.pool.ensureCapacity(capacity);
	}

	private void contractPool()
	{
		debug("contracting pool from " + capacity + " elements");
		capacity /= 2;

		for (int i = 0; (i < capacity && pool.size() > minCapacity); i++)
		{
			pool.remove(pool.size() - 1);
		}

		if (capacity < minCapacity)
		{
			capacity = minCapacity;
		}

		// this is kludge, but it can't be done any other with with ArrayList. Both of these cost an arrayCopy(), but the
		// alternative is to end up with an arrayCopy on each subsequent add().
		pool.trimToSize();
		pool.ensureCapacity(capacity);

		debug("to " + capacity + " elements");
	}

	/**
	 * 
	 */
	protected void instantiateResourcesInPool()
	{
		for (int i = 0; i < capacity; i++)
		{
			pool.add(this.generateNewResource());
		}
	}

}
