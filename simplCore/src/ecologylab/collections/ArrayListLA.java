package ecologylab.collections;

import java.util.ArrayList;
import java.util.Collection;

public class ArrayListLA<E> extends ArrayList<E> 
implements LinearAccess<E>
{

	public ArrayListLA() 
	{
		// TODO Auto-generated constructor stub
	}

	public ArrayListLA(int initialCapacity)
	{
		super(initialCapacity);
		// TODO Auto-generated constructor stub
	}

	public ArrayListLA(Collection<E> c) 
	{
		super(c);
		// TODO Auto-generated constructor stub
	}

}
