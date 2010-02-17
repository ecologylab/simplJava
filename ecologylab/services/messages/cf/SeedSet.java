package ecologylab.services.messages.cf;

import java.util.ArrayList;

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
{
	@xml_attribute protected boolean		dontPlayOnStart;
	
	@xml_attribute protected String			id;
	
	@xml_attribute protected String			category;
	
	@xml_attribute protected String			description;
	
	@xml_collection("SeedSet")
	@xml_nowrap
	ArrayList<S> seeds; 

	public SeedSet()
	{
		super();
	}

	public void add(S seed) {
		seeds.add(seed);
		
	}

	public void clear() {
		seeds.clear();
	}

}
