/*
 * Created on Feb 12, 2005
 */

package ecologylab.tutorials.polymorphic.rogue.game2d.entity.goal;

import java.awt.geom.Rectangle2D;
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
public class Goal extends Entity implements GoalConstants, Comparable<Goal>
{
	

	protected Rectangle2D.Double	uncertaintyZone	= null;

	protected int									group;

	@xml_attribute
	protected int								numReqSkr				= 1;

	@xml_attribute
	protected int									cycToCol;

	protected int 								sectorSize				= 200;
	/**
	 * @return the numReqSeekers
	 */
	public int getNumReqSkr()
	{
		return numReqSkr;
	}

	/**
	 * No-argument constructor, required for ElementState.
	 * 
	 */
	public Goal()
	{
		super();
	}

	@Override
	public int compareTo(Goal o) {
		// TODO Auto-generated method stub
		return 0;
	}

	
}