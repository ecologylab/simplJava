package ecologylab.standalone.researchnotebook.compositionTS;

import ecologylab.serialization.ElementState;

public class Search extends ElementState{
	@simpl_scalar String query; 
	@simpl_scalar float bias; 
	@simpl_scalar String engine; 
	@simpl_scalar int current_first_result_index; 
}
