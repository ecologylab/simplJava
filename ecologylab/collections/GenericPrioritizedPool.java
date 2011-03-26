/**
 * 
 */
package ecologylab.collections;

/**
 * @author andruid
 *
 */
public class GenericPrioritizedPool<GO> extends PrioritizedPool<GenericElement<GO>>
{
	public GenericPrioritizedPool(GenericWeightSet<GO>[] weightSets)
	{
		super(weightSets);
	}
	
	public GO maxGenericSelect(boolean prune)
	{
		GenericElement<GO>	element	= maxSelect(prune);
		
		return element == null ? null : element.getGeneric();
	}
	public GO maxGenericPeek(int index)
	{
		GenericElement<GO>	element	= maxPeek(index);
		
		return element == null ? null : element.getGeneric();
	}
	
	public GO pruneAndMaxGenericSelect()
	{
		return maxGenericSelect(true);
	}


}
