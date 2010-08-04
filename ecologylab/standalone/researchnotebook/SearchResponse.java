package ecologylab.standalone.researchnotebook;

import ecologylab.serialization.ElementState;

public class SearchResponse extends ElementState{
	@xml_tag("xmlns") @simpl_scalar String xmlns;
	@xml_tag("Version") @simpl_scalar float Version; 

	@xml_tag("Query") @simpl_composite Query query; 
}
