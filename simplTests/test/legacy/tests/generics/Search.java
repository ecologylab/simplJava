package legacy.tests.generics;

import java.util.List;

import ecologylab.serialization.annotations.simpl_collection;

public class Search<T extends SearchResult>
{

	@simpl_collection("result")
	List<T>	results;

}
