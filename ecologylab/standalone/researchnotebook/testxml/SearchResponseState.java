package ecologylab.standalone.researchnotebook.testxml;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

@simpl_inherit
@simpl_tag("SearchResponse")
public class SearchResponseState extends ElementState{
	//TODO let xmlns be visible 
	@simpl_tag("xmlns") @simpl_scalar ParsedURL xmlns;
	@simpl_tag("Version") @simpl_scalar float version; 

	@simpl_tag("Query") @simpl_composite Query query; 
	@simpl_tag("mms:Image") @simpl_composite Image image; 
}
