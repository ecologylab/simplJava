package ecologylab.xml.dc;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementStateWithLeafElements;

/**
 * Dublin Core Metadata Element Set, as defined at
 * 
 * http://dublincore.org/documents/dces/
 * 
 * There is no enclosing parent element in the XML markup.
 *
 * @author andruid
 */
public class Dc extends ElementStateWithLeafElements
{
	public String				title;
	public String				creator;
	public String				subject;
	public String				description;
	public String				publisher;
	// Contributor
	// public Date 			date; // we need to implement a Date type!
	// values for type: Collection, Dataset, Event, Image, MovingImage, 
	// InteractiveResource, PhysicalObject, Service, Software, Sound,
	// StillImage, Text
	public String				type;
	/**
	 * Probably the mime-type, but alas not necessarily.
	 */
	public String				format;
	/**
	 * Recommended best practice is to identify the resource by means of a string or number conforming to a formal identification system. Formal identification systems include but are not limited to the Uniform Resource Identifier (URI) (including the Uniform Resource Locator (URL)), the Digital Object Identifier (DOI) and the International Standard Book Number (ISBN).
	 */
	public ParsedURL			identifier;
	// language
	// relation
	// coverage
	// rights
	
	// from http://memory.loc.gov/ammem/award/docs/dublin-examples.html#thirteen
	
	// public String		author;
	// public String otherAgent
	// public String		otherType;
	// public String		form; // instead of format
	   
	static final String[]		LEAF_ELEMENT_FIELD_NAMES	= 
	{"title", "creator", "description", "subject", "publisher", "type",
	 "format", "identifier"};
	
	static
	{
		defineLeafElementFieldNames(LEAF_ELEMENT_FIELD_NAMES);
	}
	
}
