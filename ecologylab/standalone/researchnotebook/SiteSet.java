package ecologylab.standalone.researchnotebook;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;

public class SiteSet extends ElementState{
	@simpl_nowrap @simpl_collection("site") ArrayList<Site> site = new ArrayList<Site>(); 
}
