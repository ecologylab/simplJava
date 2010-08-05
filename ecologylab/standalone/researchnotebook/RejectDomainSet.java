package ecologylab.standalone.researchnotebook;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;

public class RejectDomainSet extends ElementState{
	@simpl_nowrap @simpl_collection("reject") ArrayList<String> reject = new ArrayList<String>();  
}
