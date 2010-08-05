package ecologylab.standalone.researchnotebook;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;

public class Values extends ElementState{
	@simpl_nowrap @simpl_collection("interest_model_entry") ArrayList<InterestModelEntry> interest_model_entry = new ArrayList<InterestModelEntry>(); 
}
