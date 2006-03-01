/*
 * Created on Jun 26, 2005
 */
package ecologylab.generic;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A set of FloatSetElements, that uses ArrayList as the storage medium.
 * 
 * @author andruid
 */
public class ArrayListFloatSet
extends ArrayList
implements BasicFloatSet 
{

	/**
	 * 
	 */
	public ArrayListFloatSet() 
	{
		super();
	}

	/**
	 * @param arg0
	 */
	public ArrayListFloatSet(int arg0)
	{
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public ArrayListFloatSet(Collection arg0)
	{
		super(arg0);
	}

   /**
	* Delete an element from the set.
	* If they are relevant, perhaps recompute internal structures, such as 
	* incremental sums, or tree/heap balancing, depending on the value of the 
	* recompute parameter.
	* 
	* @param el			The FloatSetElement element to delete.
	* @param recompute	-1 for absolutely no recompute.
	* 			 0 for recompute upwards from el.
	* 			 1 for recompute all.
	**/
	public void delete(FloatSetElement el, int recompute)
	{
		remove(el);
	}
	public void insert(FloatSetElement element)
	{
		element.setSet(this);
		int index	= size();
		add(element);
		element.setIndex(index);
	}

   /**
    * Get the ith element in the set.
    * 
    * @param i
    * @return
    */
   public FloatSetElement getElement(int i)
   {
   	  return (FloatSetElement) super.get(i);
   }
   /**
    * Get the last element in the set, or null if the set is empty.
    * 
    * @return
    */
   public FloatSetElement lastElement()
   {
	  int size	= size();
   	  return (size == 0) ? null : (FloatSetElement) get(size - 1);
   }
}
