package ecologylab.tutorials.game;

import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.Format;

public class PolymorphicJavaTutorial {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
try 
  {
    /*
     * Get translation scope
     */
    //TranslationScope tScope = get();
    
	SimplTypesScope example = SimplTypesScope.get("ThreatTest", Threat.class, Coordinate.class, OrbitingThreat.class, PatrollingThreat.class, RepellableThreat.class);

	
	
      
    
    
    
	//example.deserialize(test, Format.XML);

    
  }
  catch (Exception e) 
  {
    e.printStackTrace();
  }
	}

}
