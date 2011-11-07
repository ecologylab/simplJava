package ecologylab.translators.java.generics;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

@simpl_inherit
public class SocialSearchResult extends SearchResult
{
	
	@simpl_scalar
	public String authorName;

}
