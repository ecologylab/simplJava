package ecologylab.translators.java.generics;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

@simpl_inherit
public class SearchResult extends ElementState
{

	@simpl_scalar
	public String	title;
	
}
