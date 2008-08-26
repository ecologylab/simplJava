package ecologylab.collections;

import ecologylab.generic.VectorType;

public class FloatWeightDependantSet<E extends FloatSetDependantElement, P extends VectorType> 
extends FloatWeightSet<E>
{

	P dependantVector;
	public FloatWeightDependantSet(int initialSize)
	{
		super(initialSize);
	}


	public E maxSelect()
	{
		return null;
	}
	
}
