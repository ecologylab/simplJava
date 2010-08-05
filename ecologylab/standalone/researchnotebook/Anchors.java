package ecologylab.standalone.researchnotebook;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;

public class Anchors extends ElementState{
	@simpl_nowrap @simpl_collection("anchor") ArrayList<Anchor> anchor = new ArrayList<Anchor>(); 
}
