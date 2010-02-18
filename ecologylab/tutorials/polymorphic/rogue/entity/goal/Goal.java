/*
 * Created on Feb 12, 2005
 */

package ecologylab.tutorials.polymorphic.rogue.entity.goal;


import ecologylab.tutorials.polymorphic.rogue.game2d.entity.Entity;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_tag;

/**
 * Goal represents a goal location. Goals can be collected by SeekerAvatar objects if the
 * SeekerAvatar passes over the Goal in the game space.
 * 
 * Whether or not a goal has been collected is indicated by whether or not its state is in (an in
 * goal has not been collected yet). The other flags in its state have no meaning.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
@xml_inherit
@xml_tag("g")
public class Goal extends Entity implements Comparable<Goal>
{

	@xml_attribute
	protected int								numReqSkr				= 1;

	@xml_attribute
	protected int									cycToCol;

	/**
	 * No-argument constructor, required for ElementState.
	 * 
	 */
	public Goal()
	{
		super();
	}

	/**
	 * Constructs a Goal at the specified coordinates with the given unique identifier.
	 * 
	 * @param x
	 *          - the x-coordinate of the center of the Goal object
	 * @param y
	 *          - the y-coordinate of the center of the Goal object
	 * @param uId
	 *          - the unique identifier for the Goal object
	 * @param inGameOrder
	 *          - the order this was added to the game; used for indexing.
	 * @param group
	 *          - the group this Goal is in; used for Threat behavior.
	 * @param numRequiredSeeker
	 *          - the number of Seekers required to collect this goal.
	 */
	public Goal(double x, double y, String uId, int inGameOrder, int group, int totalMaxCycles,
			int minSeekersToCollect)
	{
		super(x, y, uId, inGameOrder);

		this.numReqSkr = minSeekersToCollect;

		this.cycToCol = totalMaxCycles;
	}
	
	public int compareTo(Goal arg0)
	{
		return this.getOrd() - arg0.getOrd();
	}
}