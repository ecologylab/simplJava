package ecologylab.standalone.researchnotebook.compositionTS;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_scalar;

public class Extent extends ElementState{
	@simpl_scalar int x; 
	@simpl_scalar int y; 
	@simpl_scalar int width;
	@simpl_scalar int height; 
}
