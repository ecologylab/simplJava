package ecologylab.standalone.researchnotebook;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.ElementState.xml_tag;

@simpl_inherit

public class Query extends ElementState{
	@xml_tag("SearchTerms") @simpl_collection("SearchTerms") ArrayList<String> SearchTerms = new ArrayList<String>();  

}
