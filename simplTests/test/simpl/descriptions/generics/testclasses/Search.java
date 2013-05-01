package simpl.descriptions.generics.testclasses;

import java.util.ArrayList;
import java.util.List;

import simpl.annotations.dbal.simpl_collection;


public class Search<T extends SearchResult>
{

	@simpl_collection("result")
	public List<T>	results;
	
	public Search()
	{
		results = new ArrayList<T>();
	}

}
