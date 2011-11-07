package ecologylab.translators.java.generics;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

@simpl_inherit
public class SocialSearch extends Search<SocialSearchResult>
{

	@simpl_scalar
	public long userId;
	
}
