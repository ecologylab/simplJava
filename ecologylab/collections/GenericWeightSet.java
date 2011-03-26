/**
 * 
 */
package ecologylab.collections;

import ecologylab.generic.ThreadMaster;

/**
 * @author andruid
 *
 */
public class GenericWeightSet<GO> extends WeightSet<GenericElement<GO>>
{

	/**
	 * @param maxSize
	 * @param setSize
	 * @param weightingStrategy
	 */
	public GenericWeightSet(int maxSize, int setSize, WeightingStrategy<GenericElement<GO>> weightingStrategy)
	{
		super(maxSize, setSize, weightingStrategy);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param getWeightStrategy
	 */
	public GenericWeightSet(WeightingStrategy<GenericElement<GO>> getWeightStrategy)
	{
		super(getWeightStrategy);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param maxSize
	 * @param threadMaster
	 * @param weightStrategy
	 */
	public GenericWeightSet(int maxSize, ThreadMaster threadMaster,
			WeightingStrategy<GenericElement<GO>> weightStrategy)
	{
		super(maxSize, threadMaster, weightStrategy);
		// TODO Auto-generated constructor stub
	}
	
	public GO maxGenericSelect()
	{
		GenericElement<GO> resultElement	= maxSelect();
		return (resultElement == null) ? null : resultElement.getGeneric();
	}

	public GO pruneAndMaxGenericSelect()
	{
		GenericElement<GO> resultElement	= pruneAndMaxSelect();
		return (resultElement == null) ? null : resultElement.getGeneric();
	}

}
