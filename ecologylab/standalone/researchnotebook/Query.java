package ecologylab.standalone.researchnotebook;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;

public class Query extends ElementState{
	@xml_tag("SearchTerms") @simpl_collection("SearchTerms") ArrayList<String> SearchTerms = new ArrayList<String>();  

}
