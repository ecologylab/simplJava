package ecologylab.standalone.researchnotebook.compositionTS;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.standalone.researchnotebook.Anchor;

public class Anchors extends ElementState{
	@simpl_nowrap @simpl_collection("anchor") ArrayList<Anchor> anchor = new ArrayList<Anchor>(); 
}
