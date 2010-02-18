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
import ecologylab.xml.XMLTranslationException;

public class PolymorphicTutorial 
{
	
	
	private static GameData getGameDataFromXMLFile(File case1) throws XMLTranslationException 
	{
		GameData gd = (GameData) ElementState.translateFromXML(case1, TranslationScope.get("gamedata", GameData.class,
				Threat.class, SingleSeekerThreat.class, OrbitingThreat.class, RepellableThreat.class,
				PatrollingThreat.class, SeekerAvatar.class, LocationAwareSeekerAvatar.class,
				Targetter.class, Mover.class, Entity.class, ColState.class));
		return gd;
	}
	
	public static void main(String[] args) 
	{
		try {
			System.out.println(getGameDataFromXMLFile(new File("ecologylab/tutorials/polymorphic/GameData.xml")).translateToXML().toString());
		} catch (XMLTranslationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
