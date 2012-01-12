package ecologylab.standalone.researchnotebook.testxml;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_scalar;

public class TestSubItem extends ElementState{	
	@simpl_scalar String priority;
	
	@simpl_scalar String target; 
}
