package ecologylab.standalone.researchnotebook;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.ElementState.xml_tag;

@simpl_inherit
@xml_tag("SearchResponse")
public class SearchResponseState extends ElementState{
	@xml_tag("xmlns") @simpl_scalar String xmlns;
	@xml_tag("Version") @simpl_scalar float Version; 

	@xml_tag("Query") @simpl_composite Query query; 
}
