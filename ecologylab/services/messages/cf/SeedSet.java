package ecologylab.services.messages.cf;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_attribute;
import ecologylab.xml.types.element.ArrayListState;

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
public class SeedSet<S extends Seed> extends ArrayListState<S>
{
	@xml_attribute protected boolean		dontPlayOnStart;
	
	@xml_attribute protected String			id;
	
	@xml_attribute protected String			category;
	
	@xml_attribute protected String			description;

	public SeedSet()
	{
		super();
	}

}
