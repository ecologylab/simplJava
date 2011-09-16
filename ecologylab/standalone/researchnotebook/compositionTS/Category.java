package ecologylab.standalone.researchnotebook.compositionTS;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_scalar;

public class Category extends ElementState{
	@simpl_scalar String name; 
	@simpl_scalar ParsedURL cat_link; 
	
}
