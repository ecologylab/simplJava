package ecologylab.tutorials.polymorphic;

import java.io.File;
import ecologylab.tutorials.polymorphic.rogue.entity.LocationAwareSeekerAvatar;
import ecologylab.tutorials.polymorphic.rogue.entity.SeekerAvatar;
import ecologylab.tutorials.polymorphic.rogue.entity.goal.ColState;
import ecologylab.tutorials.polymorphic.rogue.entity.threat.OrbitingThreat;
import ecologylab.tutorials.polymorphic.rogue.entity.threat.PatrollingThreat;
import ecologylab.tutorials.polymorphic.rogue.entity.threat.RepellableThreat;
import ecologylab.tutorials.polymorphic.rogue.entity.threat.SingleSeekerThreat;
import ecologylab.tutorials.polymorphic.rogue.entity.threat.Threat;
import ecologylab.tutorials.polymorphic.rogue.game2d.entity.Entity;
import ecologylab.tutorials.polymorphic.rogue.game2d.entity.Mover;
import ecologylab.tutorials.polymorphic.rogue.game2d.entity.Targetter;
import ecologylab.tutorials.polymorphic.rogue.gamedata.GameData;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationScope;


public class PolymorphicTutorial 
{
	

	public static void main(String[] args) 
	{
		try 
		{
			/*
			 * Creating Translation Scope of all the classes used by game data object
			 */
			TranslationScope tScope = TranslationScope.get("gamedata", GameData.class,
					Threat.class, SingleSeekerThreat.class, OrbitingThreat.class, RepellableThreat.class,
					PatrollingThreat.class, SeekerAvatar.class, LocationAwareSeekerAvatar.class,
					Targetter.class, Mover.class, Entity.class, ColState.class);
			
			/*
			 * Translating back from sample gameData file
			 */
			GameData gd = (GameData) ElementState.translateFromXML(new File("ecologylab/tutorials/polymorphic/GameData.xml"), tScope);
			
			
			
			/*
			 * Translating the game data back to XML 
			 */
			gd.translateToXML(new File("ecologylab/tutorials/polymorphic/output.xml"));
			
		}
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
