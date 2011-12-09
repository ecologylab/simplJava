package ecologylab.translators.java.generics;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

@simpl_inherit
public class TypedSocialSearch<SSR extends SocialSearchResult> extends Search<SSR>
{
	
	@simpl_scalar
	public String serviceId;

	@simpl_scalar
	public long userId;
	
}
