/*
 * Created on Apr 14, 2006
 */
package ecologylab.tutorials.polymorphic.rogue.game2d.common;

/**
 * This interface describes the different types of Entities as constants. Any
 * class that needs to check an Entity's type should implement this interface to
 * get at the constants.
 * 
 * We use bit masks for these, as threats may have multiple subtypes. Generally,
 * the bitmasks are not otherwise useful, but could be expanded upon.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public interface EntityType
{
	public static final int	NONE					= 0;

	public static final int	SEEKER_AVATAR		= 1;

	public static final int	GOAL					= 2;

	public static final int	THREAT				= 4;

	public static final int	NEW_THREAT			= 8;

	public static final int	ORBITING_THREAT	= 16;

	public static final int	PATROLLING_THREAT	= 32;

	public static final int	BASE_TYPES			= SEEKER_AVATAR | GOAL | THREAT;
}
