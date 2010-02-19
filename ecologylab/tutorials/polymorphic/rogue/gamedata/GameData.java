/*
 * Created on Jul 6, 2005
 */
package ecologylab.tutorials.polymorphic.rogue.gamedata;

import java.util.ArrayList;


import ecologylab.tutorials.polymorphic.rogue.entity.threat.OrbitingThreat;
import ecologylab.tutorials.polymorphic.rogue.entity.threat.PatrollingThreat;
import ecologylab.tutorials.polymorphic.rogue.entity.threat.RepellableThreat;
import ecologylab.tutorials.polymorphic.rogue.entity.threat.SingleSeekerThreat;
import ecologylab.tutorials.polymorphic.rogue.entity.threat.Threat;
import ecologylab.xml.ElementState;

/**
 * GameData encapsulates all of the logical information about the game and is shared between the
 * clients and the server.
 * 
 * Although most of the information stored by GameData is changed on the server, serialized, and
 * sent to clients, some of it is not. Since some of this data is static, it is initialized when the
 * game starts, and does not change. Variables that are not serialized are described in detail in
 * their documentation.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */


//GameData.java

public class GameData<T extends Threat> extends
		ElementState
{
	@xml_attribute
	protected long																timestamp;

	/** Number of game cycles remaining. */
	@xml_attribute
	protected int																cycRem;

	/**
	 * Indicates that, if the game is running, it should be paused; by default, the game starts this
	 * way and a user needs to activate it.
	 */
	@xml_attribute
	protected boolean															paused	= false;

	@xml_attribute
	protected boolean															loaded	= false;

	/** Game state flag indicating that the game has ended (favorably or unfavorably). */
	@xml_attribute
	protected boolean															over										= false;

	/**
	 * Game state flag indicating that the game is currently executing play cycles.
	 */
	@xml_attribute
	protected boolean															running									= false;

	/** Game state flag indicating that the players have won the game. */
	@xml_attribute
	protected boolean															won											= false;

	/** List of Threat objects. */
	
	@xml_classes(
	{ Threat.class, SingleSeekerThreat.class, OrbitingThreat.class, RepellableThreat.class,
			PatrollingThreat.class })
	@xml_collection
	protected ArrayList<T>										threats									= new ArrayList<T>();


	@xml_attribute
	protected double															score										= 0;

	/** No-argument constructor, required for ElementState. */
	public GameData()
	{
		super();
	}
}
