package ecologylab.standalone.researchnotebook.compositionTS;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_nowrap;

public class Values extends ElementState{
	@simpl_nowrap @simpl_collection("interest_model_entry") ArrayList<InterestModelEntry> interest_model_entry = new ArrayList<InterestModelEntry>(); 
}
