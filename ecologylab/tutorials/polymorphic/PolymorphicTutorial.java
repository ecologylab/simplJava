package ecologylab.tutorials.polymorphic;

import java.io.File;

import ecologylab.tutorials.polymorphic.rogue.entity.threat.OrbitingThreat;
import ecologylab.tutorials.polymorphic.rogue.entity.threat.PatrollingThreat;
import ecologylab.tutorials.polymorphic.rogue.entity.threat.RepellableThreat;
import ecologylab.tutorials.polymorphic.rogue.entity.threat.SingleSeekerThreat;
import ecologylab.tutorials.polymorphic.rogue.entity.threat.Threat;
import ecologylab.tutorials.polymorphic.rogue.game2d.entity.Entity;
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
			 * Get translation scope
			 */
			TranslationScope tScope = get();
			File inputGameData = new File("ecologylab/tutorials/polymorphic/GameData.xml");
			
			/*
			 * Translating back from sample gameData file
			 */
			Object gd = ElementState.translateFromXML(inputGameData, tScope);
			GameData<?> gameData = null;
			
			if(gd instanceof GameData<?>)
				gameData = (GameData<?>) ElementState.translateFromXML(inputGameData, tScope);
			else
				System.out.println("Invalid object returned by translateFromXML");
			
			/*
			 * Translating the game data back to XML 
			 */
			gameData.translateToXML(new File("ecologylab/tutorials/polymorphic/output.xml"));
			
			//Again to console
			gameData.translateToXML(System.out);
			
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/*
	 * Creating Translation Scope of all the classes used by game data object
	 */
	private static TranslationScope get()
	{

		
		TranslationScope tScope = TranslationScope.get("gamedata", GameData.class,
				Threat.class, SingleSeekerThreat.class, OrbitingThreat.class, RepellableThreat.class,
				PatrollingThreat.class, Entity.class);
		
		return tScope;
	}
}
