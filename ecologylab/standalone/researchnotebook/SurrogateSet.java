package ecologylab.standalone.researchnotebook;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;

public class SurrogateSet extends ElementState{
	@simpl_nowrap @simpl_collection("surrogate") ArrayList<Surrogate> surrogate = new ArrayList<Surrogate>(); 
}
