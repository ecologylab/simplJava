package ecologylab.collections;

import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

import ecologylab.generic.VectorType;

public class FloatWeightDependantSet<E extends FloatSetVectorElement> 
extends FloatWeightSet<E>
{

  public FloatWeightDependantSet(int initialSize)
  {
    super(initialSize);
  }


  public E maxSelect()
  {
    return null;
  }

}
