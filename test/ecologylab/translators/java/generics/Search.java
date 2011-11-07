package ecologylab.translators.java.generics;

import java.util.List;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

@simpl_inherit
public class Search<T extends SearchResult> extends ElementState
{

	@simpl_scalar
	public String		query;

	@simpl_collection("search_result")
	public List<T>	searchResults;

}
