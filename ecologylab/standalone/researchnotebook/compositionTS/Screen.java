package ecologylab.standalone.researchnotebook.compositionTS;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_scalar;

public class Screen extends ElementState{
	@simpl_scalar int width; 
	@simpl_scalar int height; 
	@simpl_scalar int size; 
}
