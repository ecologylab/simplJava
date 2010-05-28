package ecologylab.services.messages.cf;

import java.util.ArrayList;
import java.util.Iterator;

import ecologylab.xml.ElementState;
import ecologylab.xml.xml_inherit;

/**
 * A collection of seeds that will be performed by the agent, or elsewhere,
 * by composition space services.
 * These are directives from the user via the startup enviornment.
 * 
 * Version for client only:
 * 	<li>data slot definitions only with no other functionality.</li>
 * 
 * @author andruid
 */
@xml_inherit
public class SeedSet<S extends Seed> extends ElementState
implements Iterable<S>
{
	@xml_attribute protected boolean		dontPlayOnStart;
	
	@xml_attribute protected String			id;
	
	@xml_attribute protected String			category;
	
	@xml_attribute protected String			description;
	
	@xml_collection("SeedSet")
	@xml_nowrap protected ArrayList<S> 	seeds; 

	public SeedSet()
	{
		super();
	}

	public void add(S seed) 
	{
		if (seed != null)
			if (seeds == null)
				seeds	= new ArrayList<S>();
		seeds.add(seed);
	}

	public void clear() 
	{
		if (seeds != null)
			seeds.clear();
	}

	public int size()
	{
		return (seeds != null) ? seeds.size() : 0;
	}
	
	public Iterator<S> iterator()
	{
		return seeds.iterator();
	}
	
	public S get(int i)
	{
		return seeds != null ? seeds.get(i) : null;
	}
	
	public int indexOf(S that)
	{
		return seeds.indexOf(that);
	}
	
	public boolean isEmpty()
	{
		return seeds.isEmpty();
	}
}
