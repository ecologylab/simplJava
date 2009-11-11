/*
 * Created on Nov 19, 2007
 */
package ecologylab.io;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import ecologylab.generic.Debug;
import ecologylab.generic.ResourcePoolWithSize;

/**
 * This class maintains a collection of ByteBuffers, which may be acquir()'ed for temporary use.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class ByteBufferPool extends ResourcePoolWithSize<ByteBuffer>
{
	/**
	 * Instantiates a new, empty ByteBufferPool.
	 * 
	 * @param maxBufferSize -
	 *           specifies the size of the (immutable) ByteBuffers that will be created within this pool.
	 */
	public ByteBufferPool(int poolSize, int minimumCapacity, int maxBufferSize)
	{
		super(poolSize, minimumCapacity, maxBufferSize);
	}

	/**
	 * @see ecologylab.generic.ResourcePool#clean(java.lang.Object)
	 */
	@Override protected void clean(ByteBuffer objectToClean)
	{
		// clear is fast a cheap, just adjusts the mark, capacity, and position
		objectToClean.clear();
	}

	/**
	 * @see ecologylab.generic.ResourcePool#generateNewResource()
	 */
	@Override protected ByteBuffer generateNewResource()
	{
		return ByteBuffer.allocate(this.resourceObjectCapacity);
	}

	public static void main(String[] args)
	{
		Debug.println("make a pool of 3");
		ByteBufferPool p = new ByteBufferPool(9, 3, 100);
		
		ArrayList<ByteBuffer> b = new ArrayList<ByteBuffer>(8);
		
		Debug.println("acquire 100 times");
		for (int i = 0; i < 100; i++)
		{
			b.add(p.acquire());
		}
		
		Debug.println("release 100 times");
		for (int i = 0; i < 100; i++)
		{
			p.release(b.remove(b.size()-1));
		}
		
		Debug.println("test release function");
		
		ByteBuffer bb = p.acquire();
		
		Debug.println("bb: "+bb);
		
		bb = p.release(bb);
		
		Debug.println("bb: "+bb);
		
		Debug.println("acquire 100 times");
		for (int i = 0; i < 100; i++)
		{
			b.add(p.acquire());
		}
		
		Debug.println("acquire 100 times");
		for (int i = 0; i < 100; i++)
		{
			b.add(p.acquire());
		}
		
		Debug.println("acquire 100 times");
		for (int i = 0; i < 100; i++)
		{
			b.add(p.acquire());
		}
		
		Debug.println("release 100 times");
		for (int i = 0; i < 100; i++)
		{
			p.release(b.remove(b.size()-1));
		}
		
		Debug.println("release 100 times");
		for (int i = 0; i < 100; i++)
		{
			p.release(b.remove(b.size()-1));
		}
		
		Debug.println("release 100 times");
		for (int i = 0; i < 100; i++)
		{
			p.release(b.remove(b.size()-1));
		}
	}
}
