package ecologylab.standalone.researchnotebook.compositionTS;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;

public class Set extends ElementState{
	@simpl_nowrap @simpl_collection("container") ArrayList<Container> container = new ArrayList<Container>(); 
}
