package ecologylab.tutorials.polymorphic.rogue.entity.goal;

import ecologylab.xml.ElementState;
import ecologylab.xml.types.element.Mappable;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public class ColState extends ElementState implements Mappable<String>
{
	

	/** The seeker working on collecting the goal this object is associated with. */
	@xml_attribute
	String										seekerId;

	/** The number of cycles the seeker HAS completed. */
	@xml_attribute
	int												cyclesDone;

	/**
	 * The number of cycles one seeker CAN complete; when seekers each need to acquire a fractional
	 * amount of the total, this value should be the ceiling (so that the total can be greater than
	 * the number of collect cycles required). This removes an chance that the sum of the seekers'
	 * rounds collecting will be less than the total required.
	 */
	@xml_attribute
	int												maxCycles;

	/**
     * 
     */
	public ColState()
	{
	}

	/**
	 * Creates a new CollectionState with a properly configured maxCycles value.
	 * 
	 * @param seekerId
	 * @param totalMaxCycles
	 * @param minSeekersToCollect
	 */
	public ColState(String seekerId, int totalMaxCycles, int minSeekersToCollect)
	{
		this.seekerId = seekerId;

		this.maxCycles = (int) Math.ceil((double) totalMaxCycles / (double) minSeekersToCollect);

		this.cyclesDone = 0;
	}

	/**
	 * This constructor is used for fixing point totals in broken logs. Adding a ColState with this
	 * constructor, one can increase the total collection cycles for a CollectorsMap.
	 * 
	 * @param dummyCycles
	 */
	public ColState(int dummyCycles)
	{
		this.cyclesDone = dummyCycles;
	}

	/**
	 * @see ecologylab.xml.types.element.Mappable#key()
	 */
	public String key()
	{
		return this.seekerId;
	}

}
