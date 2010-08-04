package ecologylab.standalone.researchnotebook;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.ElementState.xml_tag;

@simpl_inherit
@xml_tag("SearchResponse")
public class SearchResponseState extends ElementState{
	//TODO let xmlns be visible 
	@xml_tag("xmlns") @simpl_scalar ParsedURL xmlns;
	@xml_tag("Version") @simpl_scalar float version; 

	@xml_tag("Query") @simpl_composite Query query; 
	@xml_tag("mms:Image") @simpl_composite Image image; 
}
