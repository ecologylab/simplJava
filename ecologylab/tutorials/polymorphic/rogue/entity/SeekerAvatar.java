/*
 * Created on Feb 12, 2005
 */

package ecologylab.tutorials.polymorphic.rogue.entity;


import ecologylab.tutorials.polymorphic.rogue.game2d.entity.Targetter;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_tag;
import ecologylab.xml.types.element.Mappable;

/**
 * SeekersAvatars represent players. A SeekerAvatar object can be dynamically moved around using its
 * methods. In addition, it can be caught by Threats and "restored" by bases on the map. It can also
 * be "online" and "offline" if it leaves an area of network or GPS support.
 * 
 * Unlike most physically-based entities, the SeekerAvatar works a little differently because it is
 * keyboard controlled by a player. Changes in motion, rather than being directed by applying
 * acceleration (derived from sources of force) are accomplished by changing the SA's speed, its
 * current facing, and/or it's motion vector. For convenience, the motion vector is a 0 or 1
 * magnitude vector retrieved from an array based on the direction(s) the seeker is moving.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
@xml_inherit
@xml_tag("s")
public class SeekerAvatar extends Targetter implements Mappable<String>
{
	/**
	 * The signal strength for the GPS satellites; currently ranges from 0 to 12; could be higher
	 * depending on the GPS hardware used.
	 */
	@xml_attribute
	protected int																gpsSats									= 0;

	@xml_attribute
	protected boolean														inact										= false;

	/** Indicates the number of remaining game cycles during which the seeker is safe. */
	@xml_attribute
	protected int																safeTime								= 0;

	@xml_attribute
	protected int																catcher									= -1;

	/**
	 * The speed at which the SeekerAvatar is currently moving.
	 */
	@xml_attribute
	protected float															currentSpeed						= 0;

	@xml_attribute
	protected int																noAct										= 0;

	/** The signal strength for the wifi connection; ranges from 0 to 4. */
	@xml_attribute
	protected int																wiFiStr									= 0;
	
	@xml_attribute
	protected int																hp											= 0;

	@xml_attribute
	protected boolean														ptt											= false;

	@xml_attribute
	protected boolean														panic										= false;

	/** c for color; indicates the color of the seeker in the game. */
	@xml_attribute
	protected int																c												= 0;

	/** The last game cycle during which the seeker collected a goal. */
	@xml_attribute
	@xml_tag("colTime")
	@xml_other_tags("last_goal_col_time")
	protected int																lastGoalCollectionTime	= 0;

	/**
	 * No-argument constructor, required for ElementState.
	 */
	public SeekerAvatar()
	{
		super();
	}

	public String key() 
	{
		return id;
	}
}