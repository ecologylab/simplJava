package ecologylab.collections;

import java.util.Collection;
import java.util.Vector;

/**
 * Linear Access Vector. Extends <code>Vector</code>
 * 
 *
 * @param <E>
 */
public class VectorLA<E> extends Vector<E>
implements LinearAccess<E>
{

	public VectorLA() 
	{
		// TODO Auto-generated constructor stub
	}

	public VectorLA(int initialCapacity) 
	{
		super(initialCapacity);
		// TODO Auto-generated constructor stub
	}

	public VectorLA(Collection<E> c) 
	{
		super(c);
		// TODO Auto-generated constructor stub
	}

	public VectorLA(int initialCapacity, int capacityIncrement) 
	{
		super(initialCapacity, capacityIncrement);
		// TODO Auto-generated constructor stub
	}

}
