package ecologylab.standalone.researchnotebook;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;

public class SeedSet extends ElementState{
	@simpl_nowrap @simpl_collection("search") ArrayList<Search> search = new ArrayList<Search>(); 
}
